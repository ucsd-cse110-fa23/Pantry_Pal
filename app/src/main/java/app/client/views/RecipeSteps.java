package app.client.views;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

// Content of full recipe description
public class RecipeSteps extends VBox {

    private Label recipeName;
    private TextArea recipeSteps;

    public RecipeSteps() {
        // this.setPrefSize(500, 500);
        // this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        recipeName = new Label();
        recipeSteps = new TextArea();
        recipeSteps.setEditable(true);
        recipeSteps.setPrefSize(400, 500); // set size of text field
        recipeSteps.setWrapText(true);
        recipeSteps.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        this.getChildren().addAll(recipeName, recipeSteps);
        // this.getChildren().add(recipeSteps);
    }

    public Label getRecipeName() { return recipeName; }

    public TextArea getTextArea() { return recipeSteps; }

}