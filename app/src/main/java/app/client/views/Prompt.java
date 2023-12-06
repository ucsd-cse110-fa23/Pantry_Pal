package app.client.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Message on MealFrame and IngredientsFrame
public class Prompt extends VBox{

    private Label text;
    private Button startButton, stopButton;
    private HBox buttonContainer = new HBox(5);
    private Label recordingLabel;
    
    // Set a default style for buttons and fields - background color, font size, italics
    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";

    // Set a default style for buttons and fields - background color, font size, italics
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    public Prompt(String text) {

        // Initializes the spacing and size of the label
        this.setSpacing(50);
        this.text = new Label();
        this.text.setText(text);
        this.text.setStyle("-fx-font: 13 arial;");

        // Add Start and Stop Buttons
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        // Label to be set visible upon starting to record
        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);
        recordingLabel.setAlignment(Pos.CENTER);
        
        HBox.setMargin(startButton, new Insets(5));

        // Adds the buttons to the container and adds everything to the prompt
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(startButton, stopButton);
        this.getChildren().addAll(this.text, buttonContainer, recordingLabel);
        this.setAlignment(Pos.CENTER);
        
    }

    // Sets the get methods that allow access to the contents of Prompt
    public Button getStartButton() { return startButton; }

    public Button getStopButton() { return stopButton; }

    public Label getRecordingLabel() { return recordingLabel; }

    public Label getText() { return text; }

}