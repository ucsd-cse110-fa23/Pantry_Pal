import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import javax.sound.sampled.*;

class Recipe extends VBox {

    private Label index;
    private TextField recipe;
    
    Recipe() {
        this.setPrefSize(500, 20); // sets size of recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        
        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10)); // adds some padding to the recipe
        this.getChildren().add(index); // add index label to recipe

        recipe = new TextField(); // create recipe name text field
        recipe.setPrefSize(380, 20); // set size of text field
        recipe.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        recipe.setPadding(new Insets(10)); // adds some padding to the text field
        this.getChildren().add(recipe); // add textlabel to recipe
    }

    public void setRecipeIndex(int num) {
        this.index.setText(""); // num to String
        this.recipe.setPromptText("Recipe ");
    }

    public TextField getRecipe() {
        return this.recipe;
    }
}

class RecipeList extends VBox {
    RecipeList() {
        this.setSpacing(5); // sets spacing between recipe
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }
}

class Footer extends HBox {

    private Button newRecipeButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        newRecipeButton = new Button("New Recipe"); // text displayed on add button
        newRecipeButton.setStyle(defaultButtonStyle); // styling the button
        
        this.getChildren().add(newRecipeButton); // adding button to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getNewRecipeButton() {
        return newRecipeButton;
    }
}

class Header extends HBox {

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("PantryPal"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane {

    private Header header;
    private Footer footer;
    public RecipeList recipeList;
    private Button newRecipeButton;
    private Stage primaryStage;
    public Scene homeScene;

    // IngredientsFrame ingredients = new IngredientsFrame();
    // Scene recordIngredients = new Scene(ingredients, 500, 600);
    AppFrame() {
        // Initialise the header Object
        header = new Header();

        // Create a recipeListist Object to hold the recipes
        recipeList = new RecipeList();
        
        // Initialise the Footer Object
        footer = new Footer();

        // Add a Scroller to the recipe List
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(recipeList);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroll);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        newRecipeButton = footer.getNewRecipeButton();
        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchScene(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
    } 

    public void addListeners()
    {
        // Add button functionality
        newRecipeButton.setOnAction(e -> {
            MealFrame mealType = new MealFrame(this.primaryStage, this.primaryStage.getScene(), recipeList);
            Scene recordMeal = new Scene(mealType, 500, 600);
            switchScene(this.primaryStage, recordMeal);
            // Create a new recipe
            // Recipe recipe = new Recipe();
            // Add recipe to recipeList
            // recipeList.getChildren().add(recipe);
        });
    }
}

class MockGPT extends BorderPane {
    private Header header;
    private Button saveButton;
    private Recipe newRecipe;
    private String recipeName;
    private RecipeList recipeList;

    public String response;
    public Stage primaryStage;
    public Scene homeScene;

    MockGPT(Stage primaryStage, Scene homeScene, RecipeList recipeList) {

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
        this.response = "Baked hot dogs \n Ingredients: \n Hot dogs";
        Label recipe = new Label();
        recipe.setText(response);
        this.setTop(header);
        this.setCenter(recipe);

        this.recipeName = response.split("\n")[0];
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
        this.recipeList = recipeList;

        addListeners();
    }

    // bryan: can tell server to save the to csv
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

            newRecipe = new Recipe();
            newRecipe.getRecipe().setText(recipeName);
            // recipeList.getChildren().add(newRecipe);
            recipeList.getChildren().add(newRecipe);


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
            System.out.println(e);
            System.out.println("SAVE FAIL");
        }
        
    }
    public void addListeners() {
        saveButton.setOnAction(e -> {
            saveRecipe(response);
        }); 
    }
}
    
public class App extends Application {
    Scene homeScene;
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Set the title of the a
        primaryStage.setTitle("PantryPal");

        // Setting the Layout of the Window- Should contain a Header, Footer and the recipeList
        AppFrame home = new AppFrame();
        home.setStage(primaryStage);

        // Set up Home Page and Record Recipe pages
        homeScene = new Scene(home, 500, 600);

        // Create scene of mentioned size/ with the border pane
        primaryStage.setScene(homeScene);
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
