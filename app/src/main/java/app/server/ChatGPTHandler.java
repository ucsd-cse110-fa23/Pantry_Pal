package app.server;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 *  implementing a handle api route so that we can send audio files to server to transcribe, also call chatgpt with strings to retrun a prompt.
 * 
 *  This is the handler for the third party api calls needed to chat gpt.
 * 
 *  methods needed: 
 *  post -> give it prompt and gives back the generate recipe 
 *  
 */
public class ChatGPTHandler implements HttpHandler {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "Request Recieved";
        try {
            if (method.equals("POST")) {
              response = handlePost(httpExchange);
            }
             else {
              throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
        System.out.println("An erroneous request");
        response = e.toString();
        e.printStackTrace();
        }
        //Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }


    // need to feed this method the entire prompt before calling
    // prompt: Make me a breakfast recipe using eggs with the recipe in json format with ingredients as one string and instructions as one string 

    private String handlePost(HttpExchange httpExchange) throws IOException, InterruptedException{
        String generatedText = "no response from chatgptHandler";
        // get the prompt from the body
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        
        String prompt = scanner.nextLine();

        System.out.println("prompt: " + prompt);
        
        int tokens = 500;  
        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", tokens);
        requestBody.put("temperature", 1.0);
        // Create the HTTP Client=
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
        JSONArray choices = responseJson.getJSONArray("choices");
        generatedText = choices.getJSONObject(0).getString("text");
        JSONObject toJson = new JSONObject(generatedText);

        String res = toJson.getString("title");
        res += "+" + toJson.getString("ingredients");
        res += "+" + toJson.getString("instructions");

        System.out.println("responsebody:" + responseBody);
        System.out.println("generated text:" + generatedText);
        System.out.println("return" + res);
        scanner.close();

        return generatedText;
    }
}
