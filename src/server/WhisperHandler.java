package server;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 *  implementing a WhisperAPI route that takes in audio files and translates them into text
 *  
 *  I can filter the breakfast lunch or dinner through the actual model since it will know what file is being sent to server
 * 
 *  methods needed: 
 *  post -> give file and then returns a text version of the file.
 *  
 */
 

public class WhisperHandler implements HttpHandler {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "Request Recieved";

        try {
            if (method.equals("POST")) {
              response = handlePost(httpExchange);
            } else {
              throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }


    // need to feed this method the entire prompt before calling
    private String handlePost(HttpExchange httpExchange) throws IOException, InterruptedException, URISyntaxException{
        String generatedText = "post request at whisper";

        String CRLF = "\r\n";
        int fileSize = 0;
        
        String fileName = "recording.wav";
        File f = new File(fileName);
        if (!f.exists()) { f.createNewFile(); }
        
        InputStream input = httpExchange.getRequestBody();
        String nextLine = "";
        do {
            nextLine = readLine(input, CRLF);
            if (nextLine.startsWith("Content-Length:")) {
                fileSize = 
                    Integer.parseInt(
                        nextLine.replaceAll(" ", "").substring(
                            "Content-Length:".length()
                        )
                    );
            }
        } while (!nextLine.equals(""));
        
        byte[] midFileByteArray = new byte[fileSize];
        int readOffset = 0;
        while (readOffset < fileSize) {
            int bytesRead = input.read(midFileByteArray, readOffset, fileSize);
            readOffset += bytesRead;
        }
        
        // Writes bytes from midFileByteArray into File f
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName))) {
            bos.write(midFileByteArray, 0, fileSize);
            bos.flush();
        }

        generatedText = transcribeAudio(f);

        return generatedText;
    }

    /**
     *  Below here is all helper methods for the whisper to transcribe.
     * @param is
     * @param lineSeparator
     * @return
     * @throws IOException
     */
    private static String readLine(InputStream is, String lineSeparator) throws IOException {

        int off = 0, i = 0;
        byte[] separator = lineSeparator.getBytes("UTF-8");
        byte[] lineBytes = new byte[1024];
        
        while (is.available() > 0) {
            int nextByte = is.read();
            if (nextByte < -1) {
                throw new IOException(
                    "Reached end of stream while reading the current line!");
            }
            
            lineBytes[i] = (byte) nextByte;
            if (lineBytes[i++] == separator[off++]) {
                if (off == separator.length) {
                    return new String(
                        lineBytes, 0, i-separator.length, "UTF-8");
                }
            } else {
                off = 0;
            }
            
            if (i == lineBytes.length) {
                throw new IOException("Maximum line length exceeded: " + i);
            }
        }
        
        throw new IOException("Reached end of stream while reading the current line!");       
    }

    // Helper method to write a parameter to the output stream in multipart form data format
    private static void writeParameterToOutputStream(
            OutputStream outputStream,
            String parameterName,
            String parameterValue,
            String boundary
            ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
        (
            "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
        ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }
    
    private static void writeFileToOutputStream(OutputStream outputStream, File file, String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
        (
        "Content-Disposition: form-data; name=\"file\"; filename=\"" +
        file.getName() +
        "\"\r\n"
        ).getBytes()
            );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    // Helper method to handle a successful response
    private static String handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject responseJson = new JSONObject(response.toString());

        String generatedText = responseJson.getString("text");

        return generatedText;
    }

    // Helper method to handle an error response
    private static String handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
        return "Error Result: ";
    }

    public static String transcribeAudio(File file) throws IOException, URISyntaxException {
        String response;
        // Create file object from file path

        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();

        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);

        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        // Flush and close output stream
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = connection.getResponseCode();

        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            response = handleSuccessResponse(connection);
        } else {
            response = handleErrorResponse(connection);
        }

        // Disconnect connection
        connection.disconnect();
        return response;
    }

}