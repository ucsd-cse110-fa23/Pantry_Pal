package app.client.views;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Home Page Window after loggin in
public class HomeFrame extends BorderPane {

    private Header header;
    private HomeFooter footer;
    private RecipeList recipeList;
    private Button newRecipeButton, filterMealTypeButton, sortButton, autoLoginButton, signOutButton;

    /**
     * Initialize header, content (RecipeList), and footer
     */
    public HomeFrame() {
        
        // Initialize the Header Object
        header = new Header("PantryPal");
        // Initialize the Footer Object
        footer = new HomeFooter();

        // Create a RecipeList Object to hold the recipes
        recipeList = new RecipeList();
        
        // Add a Scroller to the recipeList
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(recipeList);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);

        // Add header, scroll, and footer to top, center, and bottom of the BorderPane respectively
        this.setTop(header);
        this.setCenter(scroll);
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        newRecipeButton = footer.getNewRecipeButton();
        filterMealTypeButton = footer.getFilterMealTypeButton();
        sortButton = footer.getSortButton();
        autoLoginButton = footer.getAutoLoginButton();
        signOutButton = footer.getSignOutButton();
        
    }

    // Get methods that allow access to the contents of HomeFrame
    public RecipeList getRecipeList() { return recipeList; }

    public Button getNewRecipeButton() { return newRecipeButton; }

    public Button getSortButton() { return sortButton; }

    public Button getAutoLoginButton() { return autoLoginButton; }

    // Sets the Buttons actions so they function when clicked
    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        newRecipeButton.setOnAction(eventHandler);
    }

    public void setFilterMealTypeButtonAction(EventHandler<ActionEvent> eventHandler) {
        filterMealTypeButton.setOnAction(eventHandler);
    }

    public void setSortButtonAction(EventHandler<ActionEvent> eventHandler) {
        sortButton.setOnAction(eventHandler);
    }

    public void setAutoLoginButtonAction(EventHandler<ActionEvent> eventHandler) {
        autoLoginButton.setOnAction(eventHandler);
    }

    public void setSignOutButtonAction(EventHandler<ActionEvent> eventHandler) {
        signOutButton.setOnAction(eventHandler);
    }

}