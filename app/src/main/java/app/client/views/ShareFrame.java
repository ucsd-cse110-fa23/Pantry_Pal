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
        header = new Header("Share Link");
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        shareLink = new TextArea("");

        
        this.setTop(header);
        this.setCenter(shareLink);
        this.setBottom(footer);

        cancelButton = footer.getCancelButton();
    } 

    public TextArea getShareArea() { return shareLink; }

    public Button getCancelButton() { return cancelButton; }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }
}
