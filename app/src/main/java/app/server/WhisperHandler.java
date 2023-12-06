package app.server;

import java.io.*;
import java.net.*;

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

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "Request Received";

        try {
            if (method.equals("POST")) {
                System.out.println("request is being RECEIVED");
                response = handlePost(httpExchange);
                System.out.println("===HANDLE RESPONSE===: " + response);
            } else {
              throw new Exception("Not Valid Request Method");
            }
    
            // Sending back response to the client
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();

        } catch (Exception e) {
            System.out.println("An erroneous request");
            e.printStackTrace();
        }
    }

    private String handlePost(HttpExchange httpExchange) throws IOException, InterruptedException, URISyntaxException{
        String generatedText = "post request at whisper";

        String CRLF = "\r\n";
        int fileSize = 0;
        
        String fileName = "recording.wav";
        File f = new File(fileName);
        if (!f.exists()) { f.createNewFile(); }
        
        InputStream input = httpExchange.getRequestBody();
        String nextLine = "";
        System.out.println("STARTING BYTES: " + input.available());

        // Parse header until we find Content-Length then update fileSize with data received
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
            System.out.println("--NextLine--: " + nextLine);
        // Only read until empty line, the end of the header
        } while (!nextLine.equals(""));
        
        byte[] midFileByteArray = new byte[fileSize];
        int readOffset = 0;

        // All remaining bytes belong to audioFile, to be read into the midFileByteArray
        while (readOffset < fileSize) {
            int bytesRead = input.read(midFileByteArray, readOffset, fileSize);
            readOffset += bytesRead;
        }
        
        // Writes bytes from midFileByteArray into File f
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName))) {
            bos.write(midFileByteArray, 0, fileSize);
            bos.flush();
        }

        generatedText = Whisper.transcribeAudio(f);
        input.close();

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

}