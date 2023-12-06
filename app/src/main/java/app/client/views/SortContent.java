package app.client.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class SortContent extends VBox{

    private Label text;
    private Button alphaButton, ralphaButton, chronoButton, rchronoButton;
    private HBox buttonContainer1 = new HBox(5);
    private HBox buttonContainer2 = new HBox(5);
    
    // Set a default style for buttons and fields - background color, font size, italics
    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 10 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";

    // Set a default style for buttons and fields - background color, font size, italics
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    public SortContent(String text) {
        this.setSpacing(50);
        this.text = new Label();
        this.text.setText(text);
        this.text.setStyle("-fx-font: 13 arial;");

        // Add Buttons
        alphaButton = new Button("Alphabetically");
        alphaButton.setStyle(defaultButtonStyle);

        ralphaButton = new Button("Reverse Alphabetically");
        ralphaButton.setStyle(defaultButtonStyle);

        chronoButton = new Button("Chronologically");
        chronoButton.setStyle(defaultButtonStyle);

        rchronoButton = new Button("Reverse Chronologically");
        rchronoButton.setStyle(defaultButtonStyle);
        
        HBox.setMargin(alphaButton, new Insets(5));

        buttonContainer1.setAlignment(Pos.CENTER);
        buttonContainer1.getChildren().addAll(alphaButton, ralphaButton);
        buttonContainer2.setAlignment(Pos.CENTER);
        buttonContainer2.getChildren().addAll(chronoButton, rchronoButton);
        this.getChildren().addAll(this.text, buttonContainer1, buttonContainer2);
        this.setAlignment(Pos.CENTER);
        
    }

    public Button getAlphaButton() {
        return alphaButton;
    }

    public Button getRAlphaButton() {
        return ralphaButton;
    }

    public Button getChronoButton() {
        return chronoButton;
    }

    public Button getRChronoButton() {
        return rchronoButton;
    }

    public Label getText() {
        return text;
    }

}