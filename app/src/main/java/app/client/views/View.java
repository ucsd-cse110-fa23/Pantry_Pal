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

public class View {

    SortFrame sort;
    LoginFrame login;
    HomeFrame home;
    MealFrame meal;
    IngredientsFrame ingredients;
    GptFrame gpt;
    RecipeFrame recipe;
    
    public View () {

        // // Setting the Layout of the Window- Should contain a Header, Footer and content for each Frame
        sort = new SortFrame();
        login = new LoginFrame();
        home = new HomeFrame();
        meal = new MealFrame();
        ingredients = new IngredientsFrame();
        gpt = new GptFrame();
        recipe = new RecipeFrame();
        
    }

    public SortFrame getSortFrame() {
        return sort;
    }

    public HomeFrame getHomeFrame() {
        return home;
    }

    public MealFrame getMealFrame() {
        return meal;
    }

    public IngredientsFrame getIngredientsFrame() {
        return ingredients;
    }

    public GptFrame getGptFrame() {
        return gpt;
    }

    public RecipeFrame getRecipeFrame() {
        return recipe;
    }

    public LoginFrame getLoginFrame() {
        return login;
    }

}