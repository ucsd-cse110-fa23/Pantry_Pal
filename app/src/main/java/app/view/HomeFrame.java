package app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.io.*;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HomeFrame extends BorderPane {

    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private Button newRecipeButton;
    private Button sortButton;

    public HomeFrame() {
        // Initialize the Header Object
        header = new Header("PantryPal");
        // Initialize the Footer Object
        footer = new Footer();

        // Create a RecipeList Object to hold the recipes
        recipeList = new RecipeList();
        
        // Add a Scroller to the recipeList
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(recipeList);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);

        // Add header, scroll, and footer to top, center, and bottom of the BorderPane respectively
        this.setTop(header);
        this.setCenter(scroll);
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        newRecipeButton = footer.getNewRecipeButton();
        sortButton = footer.getSortButton();
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }

    public Button getNewRecipeButton() {
        return newRecipeButton;
    }

    public Button getSortButton() {
        return sortButton;
    }

    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        newRecipeButton.setOnAction(eventHandler);
    }

    public void setSortButtonAction(EventHandler<ActionEvent> eventHandler) {
        sortButton.setOnAction(eventHandler);
    }

}