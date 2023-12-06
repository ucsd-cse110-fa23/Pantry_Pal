package app.client.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Content with buttons to filter for Breakfast, Lunch, Dinner, or All meal types
public class FilterPrompt extends VBox {

    private Label text;
    private Button breakfastButton, lunchButton, dinnerButton, allButton;
    private HBox buttonContainerRow1 = new HBox(5);
    private HBox buttonContainerRow2 = new HBox(5);

    FilterPrompt() {

        // Initialize the Label Object
        text = new Label("Select Meal Type Filter");

        // Initialize the breakfastButton, lunchButton, dinnerButton, and allButton
        // Sets the displayed texts on the buttons
        // Sets all of their styles to the default button style
        breakfastButton = new Button("Breakfast");
        breakfastButton.setStyle("-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px");

        lunchButton = new Button("Lunch");
        lunchButton.setStyle("-fx-background-color: #79AC78; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px");

        dinnerButton = new Button("Dinner");
        dinnerButton.setStyle("-fx-background-color: #BE3144; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px");

        allButton = new Button("All");
        allButton.setStyle("-fx-background-color: #7D7C7C; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px");

        HBox.setMargin(breakfastButton, new Insets(5));

        // Top row of Filter Buttons
        buttonContainerRow1.setAlignment(Pos.CENTER);
        buttonContainerRow1.getChildren().addAll(breakfastButton, lunchButton);
        // Bottom row of Filter Buttons
        buttonContainerRow2.setAlignment(Pos.CENTER);
        buttonContainerRow2.getChildren().addAll(dinnerButton, allButton);

        this.getChildren().addAll(this.text, buttonContainerRow1, buttonContainerRow2);
        this.setAlignment(Pos.CENTER);

    }

    // Sets the get methods that allow access to the buttons of FilterPrompt
    public Button getBreakfastButton() { return breakfastButton; }
    
    public Button getLunchButton() { return lunchButton; }

    public Button getDinnerButton() { return dinnerButton; }

    public Button getAllButton() { return allButton; }

}
