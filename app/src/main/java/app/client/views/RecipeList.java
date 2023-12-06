package app.client.views;

import javafx.scene.layout.VBox;

// Container on Home Page that holds Recipe objects
public class RecipeList extends VBox {

    public RecipeList() {

        // Sets the size of RecipeList objects
        this.setSpacing(5); // sets spacing between recipe
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
        
    }

}