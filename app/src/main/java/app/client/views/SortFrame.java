package app.client.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.io.*;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SortFrame extends BorderPane {

    private Button alphaButton, ralphaButton, chronoButton, rchronoButton, cancelButton;
    private Header header;
    private RecordingFooter footer;
    private Sort sort;

    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";

    public SortFrame() {
        header = new Header("Sorting Recipe");
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        sort = new Sort(" How would you like to sort your recipe: \n Alphabetically, Reverse Alphabetically, Chronologically, or Reverse Chronologically?");
        
        this.setTop(header);
        this.setCenter(sort);
        this.setBottom(footer);

        alphaButton = sort.getAlphaButton();
        ralphaButton = sort.getRAlphaButton();
        chronoButton = sort.getChronoButton();
        rchronoButton = sort.getRChronoButton();
        cancelButton = footer.getCancelButton();
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

    public Button getCancelButton() {
        return cancelButton;
    }

    // Getter to change Start/Stop button style
    public String getDefaultStyle() {
        return defaultButtonStyle;
    }

    public Sort getSort() {
        return sort;
    }

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