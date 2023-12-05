package app.server;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DallEHandler implements HttpHandler{
    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String API_KEY = "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";
    private static final String MODEL = "dall-e-2";

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

            //Sending back response to the client
            try {
                byte[] bs = response.getBytes("UTF-8");
                httpExchange.sendResponseHeaders(200, bs.length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(bs);
                os.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

    }

    private String handlePost(HttpExchange httpExchange) throws IOException, InterruptedException, URISyntaxException{
        // Set request parameters
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        
        String prompt = scanner.nextLine();
        
        int n = 1;
        
        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
    
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("n", n);
        requestBody.put("size", "256x256");

        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create(API_ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", API_KEY))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();


        // Send the request and receive the response
        HttpResponse<String> response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        // Process the response
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);
        System.out.println(responseBody);

        String generatedImageURL = responseJson.getJSONArray("data").getJSONObject(0).getString("url");

        System.out.println("DALL-E Response:");
        System.out.println(generatedImageURL);

        // Download the Generated Image to Current Directory
        try (InputStream in = new URI(generatedImageURL).toURL().openStream()) {
            // Generate a unique file name using timestamp
            String timestamp = String.valueOf(System.currentTimeMillis());
            String imagePath = "/resources/image_" + timestamp + ".jpg"; 
    
            Path imagePathObj = Paths.get(imagePath);
            Files.copy(in, imagePathObj, StandardCopyOption.REPLACE_EXISTING);
        }
    
        scanner.close();
    
        return generatedImageURL;
        
    }

}
