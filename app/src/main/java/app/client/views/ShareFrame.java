package app.client.views;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Window that displays share recipe URL
public class ShareFrame extends BorderPane {
    
    private Button cancelButton;
    private Header header;
    private RecordingFooter footer;
    private TextArea shareLink = new TextArea();

    ShareFrame() {

        // Initializes the Header Object
        header = new Header("Share Link");
        // Initializes the RecordingFooter Object
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        shareLink = new TextArea("");

        // Add header to the top, sharelink to center, and footer to bottom of Border Pane
        this.setTop(header);
        this.setCenter(shareLink);
        this.setBottom(footer);

        // Initialise Button Variables through the getter in footer
        cancelButton = footer.getCancelButton();

    } 

    // Sets the get methods that allow access to the contents of ShareFrame
    public TextArea getShareArea() { return shareLink; }

    public Button getCancelButton() { return cancelButton; }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) { cancelButton.setOnAction(eventHandler); }

}
