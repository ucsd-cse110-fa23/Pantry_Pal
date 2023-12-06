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

        // Initializes the Header Object
        header = new Header("Sorting Recipe");
        // Initializes the RecordingFooter Object
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        sortContent = new SortContent(" How would you like to sort your recipe: \n Alphabetically, Reverse Alphabetically, Chronologically, or Reverse Chronologically?");
        
        // Add header to the top, sortContent to center, and footer to bottom of Border Pane
        this.setTop(header);
        this.setCenter(sortContent);
        this.setBottom(footer);

        // Initialise Button Variables through the getters in sortContent and footer
        alphaButton = sortContent.getAlphaButton();
        ralphaButton = sortContent.getRAlphaButton();
        chronoButton = sortContent.getChronoButton();
        rchronoButton = sortContent.getRChronoButton();
        cancelButton = footer.getCancelButton();

    }

    // Sets the get methods that allow access to the contents of SortFrame
    public Button getAlphaButton() { return alphaButton; }

    public Button getRAlphaButton() { return ralphaButton; }

    public Button getChronoButton() { return chronoButton; }

    public Button getRChronoButton() { return rchronoButton; }

    public Button getCancelButton() { return cancelButton; }

    public SortContent getSort() { return sortContent; }

    // Sets the Buttons actions so they function when clicked
    public void setAlphaButtonAction(EventHandler<ActionEvent> eventHandler) { alphaButton.setOnAction(eventHandler); }

    public void setRAlphaButtonAction(EventHandler<ActionEvent> eventHandler) { ralphaButton.setOnAction(eventHandler); }

    public void setChronoButtonAction(EventHandler<ActionEvent> eventHandler) { chronoButton.setOnAction(eventHandler); }

    public void setRChronoButtonAction(EventHandler<ActionEvent> eventHandler) { rchronoButton.setOnAction(eventHandler); }

    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) { cancelButton.setOnAction(eventHandler); }

}