package app.client.views;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Saved Full Recipe Window
public class RecipeFrame extends BorderPane {

    private Header header;
    private RecipeFooter footer;
    private RecipeSteps recipeSteps;
    private Button cancelButton, saveButton, deleteButton, shareButton;

    RecipeFrame() {
        
        // Initializes the Header Object
        header = new Header("Recipe");
        // Initializes the RecipeSteps Object
        recipeSteps = new RecipeSteps();
        // Initializes the RecipeFooter Object
        footer = new RecipeFooter();

        // Initialise Button Variables through the getters in footer
        cancelButton = footer.getCancelButton();
        saveButton = footer.getSaveButton();
        deleteButton = footer.getDeleteButton();
        shareButton = footer.getShareButton();

        ScrollPane s = new ScrollPane(recipeSteps);
        s.setFitToHeight(true);
        s.setFitToWidth(true);

        // Add header to the top, recipe content to center, and footer to bottom of Border Pane
        this.setTop(header);
        this.setCenter(s);
        this.setBottom(footer);

    }

    // The method to get the recipe steps
    public RecipeSteps getRecipeSteps() { return recipeSteps; }

    // Sets the get methods that allow access to the buttons of RecipeFrame
    public Button getCancelButton() { return cancelButton; }

    public Button getSaveButton() { return saveButton; }

    public Button getDeleteButton() { return deleteButton; }

    public Button getShareButton() { return shareButton; }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) { cancelButton.setOnAction(eventHandler); }

    // Save updates then redirect to Home Page
    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler) { saveButton.setOnAction(eventHandler); }

    // Delete Recipe from database and app then redirect to Home Page
    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) { deleteButton.setOnAction(eventHandler); }

    public void setShareButtonAction(EventHandler<ActionEvent> eventHandler){ shareButton.setOnAction(eventHandler); }

}