package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class RecordingFooter extends HBox {

    private Button cancelButton;

    // set a default style for buttons - background color, font size, italics
    String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 12 monaco;";

    public RecordingFooter() {

        // Sets the size of the Recording Footer
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        cancelButton = new Button("Cancel"); // text displayed on add button
        cancelButton.setStyle(defaultButtonStyle); // styling the button
        
        this.getChildren().add(cancelButton); // adding button to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

    }

    // Sets the method to get the cancelButton for RecordingFooter
    public Button getCancelButton() { return cancelButton; }

}