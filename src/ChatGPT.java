import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.*;

class gptFooter extends HBox{
    private Button saveButton;
    private Button cancelButton;

    String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

    gptFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        saveButton = new Button("Save Recipe");
        saveButton.setStyle(defaultButtonStyle);

        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(saveButton, cancelButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}
public class ChatGPT extends BorderPane{
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";

    private String prompt;
    private int maxTokens;

    private Header header;
    private gptFooter footer;
    private Button saveButton;
    private Button cancelButton;
    private Recipe newRecipe;
    public String recipeName;
    private RecipeList recipeList;

    public String generatedText;
    public Stage primaryStage;
    public Scene homeScene;

    ChatGPT(String mealType, String ingredients, int maxTokens, Stage primaryStage, Scene homeScene, RecipeList recipeList) throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        this.prompt = "Make me a " + mealType + " recipe " + "using " + ingredients + " with the recipe name in the first line";
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

        header = new Header();
        footer = new gptFooter();
  
        this.setPrefSize(370, 120);
        
        this.setBottom(footer);
        
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
        Label recipe = new Label();
        recipe.setText(generatedText);
        this.setTop(header);
        this.setCenter(recipe);

        this.saveButton = footer.getSaveButton();
        this.cancelButton = footer.getCancelButton();

        this.recipeName = generatedText.split("\n")[0];
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
        this.recipeList = recipeList;
        addListeners();
    }  

    public void saveRecipe(String generatedText) {
        try {
            // Read and temporarily story old recipes
            BufferedReader in = new BufferedReader(new FileReader("recipes.csv"));
            String line = in.readLine();
            String combine = "";
            while (line != null) {
                if (combine.equals("")) {
                    combine = combine + line;
                } else {
                    combine = combine + "\n" + line;
                }
                line = in.readLine();
            }
            String[] recipes = combine.split("\\$");

            newRecipe = new Recipe(primaryStage);
            newRecipe.getRecipe().setText(recipeName);
            System.out.println("RECIPE____" + recipeName);
            System.out.println("____LIST" + generatedText);
            recipeList.getChildren().add(newRecipe);

            FileWriter writer = new FileWriter("recipes.csv");
            // Write new recipe at the top of the csv
            writer.write(generatedText + "\\$");

            // Rewrite the rest of the recipes below the newly added one
            for (int i = 0; i < recipes.length - 1; i++) {
                writer.write(recipes[i] + "\\$");
            }

            in.close();
            writer.close();

            primaryStage.setScene(homeScene);
        }
        catch(Exception e) {
            System.out.println("SAVE FAIL");
        }
    }

    public void addListeners() {
        saveButton.setOnAction(e -> {
            saveRecipe(generatedText);
        }); 
        cancelButton.setOnAction(e -> {
            primaryStage.setScene(homeScene);
        });
    }
}
