import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.*;

public class MockGPT extends BorderPane {
    private Header header;
    private Button saveButton;
    public String response;
    public Stage primaryStage;
    public Scene homeScene;

    MockGPT(Stage primaryStage, Scene homeScene) {

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        header = new Header();
        // this.setT
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        saveButton = new Button("Save Recipe");
        saveButton.setStyle(defaultButtonStyle);
        this.setBottom(saveButton);
        
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
        this.response = "Here is my recipe for baked hot dogs";
        Label recipe = new Label();
        recipe.setText(response);
        this.setTop(header);
        this.setCenter(recipe);

        this.primaryStage = primaryStage;
        this.homeScene = homeScene;

        addListeners();
    }

    public void saveRecipe(String response) {
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

            FileWriter writer = new FileWriter("recipes.csv");
            // Write new recipe at the top of the csv
            writer.write(response + "\\$");

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
            saveRecipe(response);
        }); 
    }
}
