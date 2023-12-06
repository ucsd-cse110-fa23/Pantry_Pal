package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

// Creates the Footer that is used in the GptFrame.
// Contains the save, cancel, and refresh buttons.
public class GptFooter extends HBox{

    private Button saveButton, cancelButton, refreshButton;

    String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

    public GptFooter() {

        // Sets the size of the GptFooter
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // Initialize the saveButton, cancelButton, and refreshButton
        // Sets the displayed texts on the buttons
        // Sets all of their styles to the default button style
        saveButton = new Button("Save Recipe"); 
        saveButton.setStyle(defaultButtonStyle);

        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);

        refreshButton = new Button("Refresh");
        refreshButton.setStyle(defaultButtonStyle);

        // Adds all of the buttons to the GptFooter
        this.getChildren().addAll(saveButton, cancelButton, refreshButton);
        this.setAlignment(Pos.CENTER);
        
    }

    // Sets the get methods that allow access to the buttons of GptFooter
    public Button getSaveButton() { return saveButton; }

    public Button getCancelButton() { return cancelButton; }

    public Button getRefreshButton() { return refreshButton; }

}