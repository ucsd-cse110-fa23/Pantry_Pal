package client;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;

import java.net.URL;

public class Model {
    /**
     * 
     * @param method 
     * @param query true = parameters in http, false = no parameters
     * @param function csv, chatgpt, whisper
     * @return
     */
    public String performRequest(String method, String fileName, String query, String route) {

        // Implement your HTTP request logic here and return the response
        try {
            String urlString = "http://localhost:8100/" + route;
            if (query != null) {
                urlString += "?=" + query;
            }

            if (route.equals("chatgpt")) {
                
            } else if (route.equals("whisper")) {
                sendPOSTWhisper();
            }

            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            

            // if (method.equals("POST") || method.equals("PUT")) {
            //     OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            //     // out.write(fileName);
            //     out.flush();
            //     out.close();
            // }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();

            if (route.equals("whisper")) {
                response = mealType(response);
            }

            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }

    }

    private String mealType(String res) {
        String mealType = res.toLowerCase();

        if (mealType.contains("breakfast")) { return "breakfast"; }
        else if (mealType.contains("lunch")) { return "lunch"; }
        else if (mealType.contains("dinner")) { return "dinner"; }
        else { return null; }
    }

    public static void sendPOSTWhisper() throws IOException {
        final String POST_URL = "http://localhost:8100/whisper";
        final File uploadFile = new File("recording.wav");

        String boundary = Long.toHexString(System.currentTimeMillis()); 
        String CRLF = "\r\n";
        String charset = "UTF-8";
        URLConnection connection = new URL(POST_URL).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        
        try (
            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + uploadFile.getName() + "\"").append(CRLF);
            writer.append("Content-Length: " + uploadFile.length()).append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(uploadFile.toPath(), output);
            output.flush();
            writer.append("--" + boundary + "--").append(CRLF).flush(); // End of multipart/form-data.

            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            System.out.println("Response code: [" + responseCode + "]");
        }
    }
    
    public static void main(String[] args) throws IOException {
        // sendPOSTWhisper();
    }
}