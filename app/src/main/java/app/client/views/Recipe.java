package app.client.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Recipe Title Displayed on Home Page
public class Recipe extends VBox {

    private Label index;
    private Label mealType;
    private HBox container = new HBox();
    private Label recipe;
    private Button viewButton;
    
    /**
     * List items saved in RecipeList from home page
     */
    public Recipe() {
        
        // Sets the size of Recipe
        this.setPrefSize(500, 20); // sets size of recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        
        // Initializes the label object for index
        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10)); // adds some padding to the recipe
        this.getChildren().add(index); // add index label to recipe

        recipe = new Label(); // create recipe name text field
        recipe.setPrefSize(400, 20); // set size of text field
        recipe.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        recipe.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        
        // Initializes the mealType object for index
        mealType = new Label();
        mealType.setPadding(new Insets(0, 5, 0, 5)); // Adds padding to the label
        
        // Initializes the viewButton object
        viewButton = new Button("View");
        viewButton.setPrefSize(50, 20); // Sets the size of the viewButton
        viewButton.setPrefHeight(Double.MAX_VALUE); // Sets the height of the viewButton
        viewButton.setStyle("-fx-background-color: #FAE5EA; -fx-border-width: 0;"); // sets style of button

        // Place Meal Type Tag, recipe name, and view button on same line
        HBox.setMargin(mealType, new Insets(5));
        container.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().addAll(mealType, recipe, viewButton);
        this.getChildren().add(container); // add textlabel to recipe

    }

    // Sets the get methods that allow access to the contents of Prompt
    public Label getRecipe() { return recipe; }

    public Label getMealType() { return mealType; }

    public Button getViewButton() { return viewButton; }

    public int getIndex() { return Integer.valueOf(index.getText().toString()); }

    // Method to update the indice of the recipe
    public void setRecipeIndex(int num) {
        index.setText(num + ""); // num to String
    }
    
    // View button opens full description corresponding to that recipe
    public void setViewButtonAction(EventHandler<ActionEvent> eventHandler) {
        viewButton.setOnAction(eventHandler);
    }

}