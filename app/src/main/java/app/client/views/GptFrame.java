package app.client.views;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// ChatGPT Generated Recipe Window
public class GptFrame extends BorderPane {

    private Header header;
    private GptFooter footer;
    private Button saveButton, cancelButton, refreshButton;
    private TextArea recipeText = new TextArea();
    private ImageView imageView = new ImageView();
    
    GptFrame() {

        // Initialize the Header Object
        header = new Header("New Recipe");
        // Initialize the GptFooter Object
        footer = new GptFooter();
        
        // Sets the size of the textbox that contains the recipe
        recipeText.setWrapText(true);
        recipeText.setMaxWidth(350);
        recipeText.setPadding(new Insets(5));

        // Adds the imageView and recipeText to the HBox
        HBox container = new HBox();
        container.getChildren().addAll(imageView, recipeText);

        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        // Add a Scroller to the recipe
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(container);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        
        // Add header, scroll, and footer to top, center, and bottom of the BorderPane respectively
        this.setTop(header);
        this.setCenter(scroll);
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        saveButton = footer.getSaveButton();
        cancelButton = footer.getCancelButton();
        refreshButton = footer.getRefreshButton();

    }

    // Sets the get methods that allow access to the contents of GptFrame
    public TextArea getRecipeText() {return recipeText; }

    public ImageView getImageView() { return imageView; }

    public Button getSaveButton() { return saveButton; }

    public Button getCancelButton() { return cancelButton; }

    public Button getRefreshButton() { return refreshButton; }

    // Sets the Buttons actions so they function when clicked
    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler) {
        saveButton.setOnAction(eventHandler);
    }

    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

    public void setRefreshButtonAction(EventHandler<ActionEvent> eventHandler) {
        refreshButton.setOnAction(eventHandler);
    }
    
}