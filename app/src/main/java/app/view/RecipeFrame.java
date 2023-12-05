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

public class RecipeFrame extends BorderPane {

    private Header header;
    private RecipeFooter footer;
    private RecipeSteps recipeSteps;
    private Button cancelButton, saveButton, deleteButton;

    public RecipeFrame() {
        header = new Header("Recipe");
        recipeSteps = new RecipeSteps();
        footer = new RecipeFooter();

        cancelButton = footer.getCancelButton();
        saveButton = footer.getSaveButton();
        deleteButton = footer.getDeleteButton();

        ScrollPane s = new ScrollPane(recipeSteps);
        s.setFitToHeight(true);
        s.setFitToWidth(true);

        // Add header to the top, recipe content to center, and footer to bottom of Border Pane
        this.setTop(header);
        this.setCenter(s);
        this.setBottom(footer);
    }

    public RecipeSteps getRecipeSteps() {
        return recipeSteps;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler) {
        saveButton.setOnAction(eventHandler);
    }

    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
        deleteButton.setOnAction(eventHandler);
    }

}