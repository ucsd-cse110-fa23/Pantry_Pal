import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPT {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";

    private String prompt;
    private int maxTokens;

    ChatGPT(String mealType, String ingredients, int maxTokens) throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        this.prompt = "Make me a " + mealType + " recipe " + "with " + ingredients;
        this.maxTokens = maxTokens;
        
        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", this.prompt);
        requestBody.put("max_tokens", this.maxTokens);
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
        String generatedText = choices.getJSONObject(0).getString("text");
        
        System.out.println(generatedText);
    }  
}