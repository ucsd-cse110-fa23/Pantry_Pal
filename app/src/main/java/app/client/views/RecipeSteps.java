package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Content of full recipe description
public class RecipeSteps extends VBox {

     private Label recipeName;
    private TextArea recipeSteps = new TextArea();
    private ImageView imageView = new ImageView();

    public RecipeSteps() {
        recipeName = new Label();
        recipeName.setAlignment(Pos.CENTER);
        //recipeName.setStyle("-fx-font-size: 20");
        
        recipeSteps.setEditable(true);
        recipeSteps.setPrefSize(400, 500); // set size of text field
        recipeSteps.setWrapText(true);
        recipeSteps.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        HBox container = new HBox();
        container.getChildren().addAll(imageView,recipeSteps);
        
        this.getChildren().addAll(recipeName, container);
        // this.getChildren().add(recipeSteps);
    }

    public Label getRecipeName() { return recipeName; }

    public TextArea getTextArea() { return recipeSteps; }

    public ImageView getImageView() { return imageView; }

}