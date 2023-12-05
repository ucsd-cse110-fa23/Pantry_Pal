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

public class GptFrame extends BorderPane {

    private Header header;
    private GptFooter footer;
    private Button saveButton, cancelButton, refreshButton;
    private String generatedText = "TWO Bacon, Eggs, and Sausage Breakfast+4 slices bacon, 2 eggs, 2 sausage links+1. In a medium skillet over medium heat, cook the bacon until crispy. 2. Remove bacon from skillet, leaving renderings in the pan. Add sausage and cook until browned on both sides. 3. Push sausage to one side and crack two eggs into the other side. Fry over medium heat until desired doneness. 4. Serve bacon, eggs, and sausage together.";
    private Recipe newRecipe;
    private TextArea recipeText = new TextArea();
    //String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 75px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    
    public GptFrame() {
        this.setPrefSize(370, 120);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        header = new Header("New Recipe");
        footer = new GptFooter();
        
        recipeText.setText(generatedText);
        recipeText.setWrapText(true);
        recipeText.setMaxWidth(350);
        recipeText.setPadding(new Insets(5));

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(recipeText);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        
        this.setTop(header);
        this.setCenter(scroll);
        this.setBottom(footer);

        saveButton = footer.getSaveButton();
        cancelButton = footer.getCancelButton();
        refreshButton = footer.getRefreshButton();

    }

    public TextArea getRecipeText() {
        return recipeText;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Recipe getNewRecipe() {
        return newRecipe;
    }

    // possible need for this method
    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler){
        saveButton.setOnAction(eventHandler);
    }

    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

    public void setRefreshButtonAction(EventHandler<ActionEvent> eventHandler){
        refreshButton.setOnAction(eventHandler);
    }

}