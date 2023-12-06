package app.client.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class IngredientsFrame extends BorderPane {

    private Button startButton, stopButton, cancelButton;
    private Label recordingLabel;
    private Header header;
    private RecordingFooter footer;
    private Prompt prompt;

    public IngredientsFrame() {
        
        // Initializes the Header Object
        header = new Header("Record Ingredients");
        // Initializes the RecordingFooter Object
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        prompt = new Prompt("");
        
        // Add header, scroll, and footer to top, center, and bottom of the BorderPane respectively
        this.setTop(header);
        this.setCenter(prompt);
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Prompt and Footer
        startButton = prompt.getStartButton();
        stopButton = prompt.getStopButton();
        recordingLabel = prompt.getRecordingLabel();
        cancelButton = footer.getCancelButton();

    }

    // Sets the get methods that allow access to the buttons of IngredientsFrame
    public Button getStartButton() { return startButton; }

    public Button getStopButton() { return stopButton; }

    public Button getCancelButton() { return cancelButton; }

    public Label getRecordingLabel() { return recordingLabel; }

    public Prompt getPrompt() { return prompt; }

    // Writes audio into "recording.wav"
    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    // Needs to detect either "Breakfast," "Lunch," or "Dinner" to move to next Frame
    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopButton.setOnAction(eventHandler);
    }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

}