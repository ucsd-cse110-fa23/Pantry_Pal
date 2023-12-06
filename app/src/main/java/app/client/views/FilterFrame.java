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

        // Initialize the Header Object
        header = new Header("Filter Meal Type");
        // Initialize the FilterPrompt Object
        filterPrompt = new FilterPrompt();
        // INitialize the RecordingFooter Object
        footer = new RecordingFooter();

        // Add Header, FilterPrompt, and RecordingFooter to top, center, and bottom of the FilterFrame respectively
        this.setTop(header);
        this.setCenter(filterPrompt);
        this.setBottom(footer);

        // Initialise Button Variables through the getters in FilterPrompt and Footer
        breakfastButton = filterPrompt.getBreakfastButton();
        lunchButton = filterPrompt.getLunchButton();
        dinnerButton = filterPrompt.getDinnerButton();
        allButton = filterPrompt.getAllButton();
        cancelButton = footer.getCancelButton();

    }

    // Sets all of the button actions
    public void setBreakfastButtonAction(EventHandler<ActionEvent> eventHandler) { breakfastButton.setOnAction(eventHandler); }

    public void setLunchButtonAction(EventHandler<ActionEvent> eventHandler) { lunchButton.setOnAction(eventHandler); }

    public void setDinnerButtonAction(EventHandler<ActionEvent> eventHandler) { dinnerButton.setOnAction(eventHandler); }

    public void setAllButtonAction(EventHandler<ActionEvent> eventHandler) { allButton.setOnAction(eventHandler); }

    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) { cancelButton.setOnAction(eventHandler); }
    
}
