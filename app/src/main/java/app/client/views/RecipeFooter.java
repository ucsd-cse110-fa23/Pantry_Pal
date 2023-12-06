package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class RecipeFooter extends HBox {

    private Button cancelButton, saveButton, deleteButton, shareButton;

    public RecipeFooter() {
        
        // Sets the size of the footer
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        // Initializes the cancel, save, delete, and share buttons and sets their style
        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);

        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);

        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);

        shareButton = new Button("Share");
        shareButton.setStyle(defaultButtonStyle);
        
        // Adds all of the buttons to the footer
        this.getChildren().addAll(cancelButton, saveButton, deleteButton, shareButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

    }

    // Sets the get methods that allow access to the buttons of RecipeFooter
    public Button getCancelButton() { return cancelButton; }

    public Button getSaveButton() { return saveButton; }

    public Button getDeleteButton() { return deleteButton; }

    public Button getShareButton() { return shareButton; }

}