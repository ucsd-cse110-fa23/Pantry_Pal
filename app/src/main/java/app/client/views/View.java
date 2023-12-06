package app.client.views;

import javafx.scene.control.Alert;

public class View {

    LoginFrame login;
    HomeFrame home;
    MealFrame meal;
    IngredientsFrame ingredients;
    GptFrame gpt;
    RecipeFrame recipe;
    FilterFrame filter;
    ShareFrame share;
    SortFrame sort;

    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    String clickedButtonStyle = "-fx-background-color: #0174BE; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    
    public View () {

        // Setting the Layout of the Window- Should contain a Header, Footer and content for each Frame
        login = new LoginFrame();
        home = new HomeFrame();
        meal = new MealFrame();
        ingredients = new IngredientsFrame();
        gpt = new GptFrame();
        recipe = new RecipeFrame();
        share = new ShareFrame();
        filter = new FilterFrame();
        sort = new SortFrame();
        
    }
    
    // Sets the get methods that allow access to all the frames of View
    public LoginFrame getLoginFrame() { return login; }

    public HomeFrame getHomeFrame() { return home; }

    public MealFrame getMealFrame() { return meal; }

    public IngredientsFrame getIngredientsFrame() { return ingredients; }

    public GptFrame getGptFrame() { return gpt; }

    public RecipeFrame getRecipeFrame() { return recipe; }

    public ShareFrame getShareFrame(){ return share; }

    public FilterFrame getFilterFrame() { return filter; }

    public SortFrame getSortFrame() { return sort; }

    public String getDefaultButtonStyle() { return defaultButtonStyle; }

    public String getClickedButtonStyle() { return clickedButtonStyle; }
    
    // Method that shows alerts in case of errors
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}