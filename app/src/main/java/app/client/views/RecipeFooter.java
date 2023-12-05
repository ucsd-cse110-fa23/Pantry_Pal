package app.client.views;

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

public class RecipeFooter extends HBox {

    private Button cancelButton, saveButton, deleteButton;

    public RecipeFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);
        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);
        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);
        
        this.getChildren().addAll(cancelButton, saveButton, deleteButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
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

}