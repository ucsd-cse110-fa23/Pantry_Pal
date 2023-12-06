package app.client.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

// Footer for Home Page
public class HomeFooter extends HBox {

    private Button newRecipeButton, filterMealTypeButton, sortButton, autoLoginButton, signOutButton;

    public HomeFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        newRecipeButton = new Button("New Recipe"); // text displayed on add button
        newRecipeButton.setStyle(defaultButtonStyle); // styling the button

        filterMealTypeButton = new Button("Filter Meal");
        filterMealTypeButton.setStyle(defaultButtonStyle);

        sortButton = new Button("Sort Recipes"); // text displayed on add button
        sortButton.setStyle(defaultButtonStyle); // styling the button

        signOutButton = new Button("Sign Out");
        signOutButton.setStyle(defaultButtonStyle);

        Label autoLoginLabel = new Label("Automatic Login: ");
        autoLoginButton = new Button("Loading");
        autoLoginButton.setStyle(defaultButtonStyle);

        HBox autoLoginContainer = new HBox();
        autoLoginContainer.setAlignment(Pos.CENTER);
        autoLoginContainer.getChildren().addAll(autoLoginLabel, autoLoginButton);
        
        this.getChildren().addAll(newRecipeButton, filterMealTypeButton, sortButton, autoLoginContainer, signOutButton); // adding button to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getNewRecipeButton() { return newRecipeButton; }

    public Button getFilterMealTypeButton() { return filterMealTypeButton; }

    public Button getSortButton() { return sortButton; }

    public Button getSignOutButton() { return signOutButton; }

    public Button getAutoLoginButton() { return autoLoginButton; }

}