package app.client.views;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Filter Recipe Window to Display Recipes Based on a Specific Meal Type
public class FilterFrame extends BorderPane {
    
    private Header header;
    private RecordingFooter footer;
    private FilterPrompt filterPrompt;
    private Button breakfastButton, lunchButton, dinnerButton, allButton, cancelButton;

    FilterFrame() {

        header = new Header("Filter Meal Type");
        filterPrompt = new FilterPrompt();
        footer = new RecordingFooter();

        this.setTop(header);
        this.setCenter(filterPrompt);
        this.setBottom(footer);

        breakfastButton = filterPrompt.getBreakfastButton();
        lunchButton = filterPrompt.getLunchButton();
        dinnerButton = filterPrompt.getDinnerButton();
        allButton = filterPrompt.getAllButton();
        cancelButton = footer.getCancelButton();

    }

    public void setBreakfastButtonAction(EventHandler<ActionEvent> eventHandler) {
        breakfastButton.setOnAction(eventHandler);
    }

    public void setLunchButtonAction(EventHandler<ActionEvent> eventHandler) {
        lunchButton.setOnAction(eventHandler);
    }

    public void setDinnerButtonAction(EventHandler<ActionEvent> eventHandler) {
        dinnerButton.setOnAction(eventHandler);
    }

    public void setAllButtonAction(EventHandler<ActionEvent> eventHandler) {
        allButton.setOnAction(eventHandler);
    }

    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

}
