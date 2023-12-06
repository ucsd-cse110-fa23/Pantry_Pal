package app.client.views;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SortFrame extends BorderPane {

    private Button alphaButton, ralphaButton, chronoButton, rchronoButton, cancelButton;
    private Header header;
    private RecordingFooter footer;
    private SortContent sortContent;

    public SortFrame() {
        header = new Header("Sorting Recipe");
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        sortContent = new SortContent(" How would you like to sort your recipe: \n Alphabetically, Reverse Alphabetically, Chronologically, or Reverse Chronologically?");
        
        this.setTop(header);
        this.setCenter(sortContent);
        this.setBottom(footer);

        alphaButton = sortContent.getAlphaButton();
        ralphaButton = sortContent.getRAlphaButton();
        chronoButton = sortContent.getChronoButton();
        rchronoButton = sortContent.getRChronoButton();
        cancelButton = footer.getCancelButton();
    }

    public Button getAlphaButton() { return alphaButton; }

    public Button getRAlphaButton() { return ralphaButton; }

    public Button getChronoButton() { return chronoButton; }

    public Button getRChronoButton() { return rchronoButton; }

    public Button getCancelButton() { return cancelButton; }

    public SortContent getSort() { return sortContent; }

    // Writes audio into "recording.wav"
    public void setAlphaButtonAction(EventHandler<ActionEvent> eventHandler) {
        alphaButton.setOnAction(eventHandler);
    }

    public void setRAlphaButtonAction(EventHandler<ActionEvent> eventHandler) {
        ralphaButton.setOnAction(eventHandler);
    }

    // Needs to detect either "Breakfast," "Lunch," or "Dinner" to move to next Frame
    public void setChronoButtonAction(EventHandler<ActionEvent> eventHandler) {
        chronoButton.setOnAction(eventHandler);
    }

    public void setRChronoButtonAction(EventHandler<ActionEvent> eventHandler) {
        rchronoButton.setOnAction(eventHandler);
    }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }
}