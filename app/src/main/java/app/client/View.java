package app.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.io.*;

import javax.xml.crypto.dsig.spec.XPathType.Filter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//===================== HEADER ===================================================

// Header across all Frames
class Header extends HBox {

    Text titleText;

    Header(String text) {
        this.setPrefSize(500, 70); // Size of the header
        this.setStyle("-fx-background-color: #9EB8D9;");

        titleText = new Text(text); // Text of the Header
        titleText.setStyle("-fx-font: 24 arial; -fx-text-fill: #FFFFFF;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }

}

//===================== FOOTERS ===================================================

// Home Page Footer
class HomeFooter extends HBox {

    private Button newRecipeButton, filterMealTypeButton;

    HomeFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        newRecipeButton = new Button("New Recipe"); // text displayed on add button
        newRecipeButton.setStyle(defaultButtonStyle); // styling the button

        filterMealTypeButton = new Button("Filter Meal");
        filterMealTypeButton.setStyle(defaultButtonStyle);
        
        this.getChildren().addAll(newRecipeButton, filterMealTypeButton); // adding button to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getNewRecipeButton() {
        return newRecipeButton;
    }

    public Button getFilterMealTypeButton() {
        return filterMealTypeButton;
    }

}

// MealFrame and IngredientsFrame Footer
class RecordingFooter extends HBox{

    private Button cancelButton;

    // set a default style for buttons - background color, font size, italics
    String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 12 monaco;";

    RecordingFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        cancelButton = new Button("Cancel"); // text displayed on add button
        cancelButton.setStyle(defaultButtonStyle); // styling the button
        
        this.getChildren().add(cancelButton); // adding button to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getCancelButton() {
        return cancelButton;
    }

}

// ChatGPT response Footer
class GptFooter extends HBox{

    private Button saveButton, cancelButton, refreshButton;

    String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

    GptFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        saveButton = new Button("Save Recipe");
        saveButton.setStyle(defaultButtonStyle);

        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);

        refreshButton = new Button("Refresh");
        refreshButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(saveButton, cancelButton, refreshButton);
        this.setAlignment(Pos.CENTER);
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

}

// Full recipe, RecipeFrame Footer
class RecipeFooter extends HBox {

    private Button cancelButton, saveButton, deleteButton;

    RecipeFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        cancelButton = new Button("Cancel");
        cancelButton.setStyle(defaultButtonStyle);
        saveButton = new Button("Save");
        saveButton.setStyle(defaultButtonStyle);
        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);
        
        this.getChildren().addAll(cancelButton, saveButton, deleteButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

}

//===================== MISCELLANEOUS ===================================================

// Fields for Login Page
class LoginContent extends VBox {

    private TextField username;
    private PasswordField password;
    private Button createAccountButton, loginButton;

    LoginContent() {

        this.setPrefWidth(10);

        username = new TextField();
        password = new PasswordField();

        username.setPromptText("Username");
        password.setPromptText("Password");

        createAccountButton = new Button("Create Account");
        loginButton = new Button("Login");

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(loginButton, createAccountButton);

        this.getChildren().addAll(username, password, buttonContainer);

    }
    
    public TextField getUsername() {
        return username;
    }

    public PasswordField getPassword() {
        return password;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

}

// Recipe Title Displayed on Home Page
class Recipe extends VBox {

    private Label index;
    private Label mealType;
    private HBox container = new HBox();
    private TextField recipe;
    private Button viewButton;
    
    Recipe() {
        this.setPrefSize(500, 20); // sets size of recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of recipe
        
        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10)); // adds some padding to the recipe
        this.getChildren().add(index); // add index label to recipe

        recipe = new TextField(); // create recipe name text field
        recipe.setPrefSize(300, 20); // set size of text field
        recipe.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        recipe.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        
        mealType = new Label("B");
        mealType.setStyle("-fx-background-color: #39A7FF; -fx-font-size: 14; -fx-border-radius: 20; -fx-text-fill: white;");
        mealType.setPadding(new Insets(0, 5, 0, 5));
        
        viewButton = new Button("View");
        viewButton.setPrefSize(50, 20);
        viewButton.setPrefHeight(Double.MAX_VALUE);
        viewButton.setStyle("-fx-background-color: #FAE5EA; -fx-border-width: 0;"); // sets style of button

        HBox.setMargin(mealType, new Insets(5));
        container.setAlignment(Pos.CENTER_LEFT);
        container.getChildren().addAll(mealType, recipe, viewButton);
        this.getChildren().add(container); // add textlabel to recipe

        // this.getChildren().add(viewButton);
    }

    public void setRecipeIndex(int num) {
        index.setText(num + ""); // num to String
        recipe.setPromptText("Recipe ");
    }

    public TextField getRecipe() {
        return recipe;
    }

    public Label getMealType() {
        return mealType;
    }

    public Button getViewButton() {
        return viewButton;
    }

    public int getIndex() {
        return Integer.valueOf(index.getText().toString());
    }
    
    // View button opens full description corresponding to that recipe
    public void setViewButtonAction(EventHandler<ActionEvent> eventHandler) {
        viewButton.setOnAction(eventHandler);
    }

}

// Container on Home Page that holds Recipe objects
class RecipeList extends VBox {

    RecipeList() {
        this.setSpacing(5); // sets spacing between recipe
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

}

// Content of full recipe description
class RecipeSteps extends VBox {

    private Label recipeName;
    private TextArea recipeSteps;

    RecipeSteps() {
        // this.setPrefSize(500, 500);
        // this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        recipeName = new Label();
        recipeSteps = new TextArea();
        recipeSteps.setEditable(true);
        recipeSteps.setPrefSize(400, 500); // set size of text field
        recipeSteps.setWrapText(true);
        recipeSteps.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        
        this.getChildren().addAll(recipeName, recipeSteps);
    }

    public Label getRecipeName() {
        return recipeName;
    }

    public TextArea getTextArea() {
        return recipeSteps;
    }

}

// Message on MealFrame and IngredientsFrame
class Prompt extends VBox {

    private Label text;
    private Button startButton, stopButton;
    private HBox buttonContainer = new HBox(5);
    private Label recordingLabel;
    
    // Set a default style for buttons and fields - background color, font size, italics
    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";

    // Set a default style for buttons and fields - background color, font size, italics
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    Prompt(String text) {
        this.setSpacing(50);
        this.text = new Label();
        this.text.setText(text);
        this.text.setStyle("-fx-font: 13 arial;");

        // Add Start and Stop Buttons
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        // Label to be set visible upon starting to record
        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);
        recordingLabel.setAlignment(Pos.CENTER);
        
        HBox.setMargin(startButton, new Insets(5));

        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(startButton, stopButton);
        this.getChildren().addAll(this.text, buttonContainer, recordingLabel);
        this.setAlignment(Pos.CENTER);
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Label getRecordingLabel() {
        return recordingLabel;
    }

    public Label getText() {
        return text;
    }

}

class FilterPrompt extends VBox {

    private Label text;
    private Button breakfastButton, lunchButton, dinnerButton;
    private HBox buttonContainer = new HBox(5);

    FilterPrompt() {

        text = new Label("Select Meal Type Filter");

        breakfastButton = new Button("Breakfast");
        breakfastButton.setStyle("-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px");

        lunchButton = new Button("Lunch");
        lunchButton.setStyle("-fx-background-color: #79AC78; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px");

        dinnerButton = new Button("Dinner");
        dinnerButton.setStyle("-fx-background-color: #BE3144; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px");

        buttonContainer.getChildren().addAll(breakfastButton, lunchButton, dinnerButton);
        this.getChildren().addAll(text, buttonContainer);

    }

    public Button getBreakfastButton() {
        return breakfastButton;
    }
    
    public Button getLunchButton() {
        return lunchButton;
    }

    public Button getDinnerButton() {
        return dinnerButton;
    }

}

//=============================== FRAMES =========================================

// Logic Page Window Upon App Start
class LoginFrame extends BorderPane {

    private Header header;
    private LoginContent loginContent;
    private Button loginButton, createAccountButton;

    LoginFrame() {
        
        this.setPrefSize(370, 120);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
            
        header = new Header("PantryPal");
        loginContent = new LoginContent();

        this.setTop(header);
        this.setCenter(loginContent);

        loginButton = loginContent.getLoginButton();
        createAccountButton = loginContent.getCreateAccountButton();

    }

    public LoginContent getLoginContent() {
        return loginContent;
    }

    public void setLoginButtonAction(EventHandler<ActionEvent> eventHandler) {
        loginButton.setOnAction(eventHandler);
    }

    public void setCreateAccountButtonAction(EventHandler<ActionEvent> eventHandler) {
        createAccountButton.setOnAction(eventHandler);
    }

}

// Home Page Window
class HomeFrame extends BorderPane {

    private Header header;
    private HomeFooter footer;
    private RecipeList recipeList;
    private Button newRecipeButton, filterMealTypeButton;

    HomeFrame() {

        // Initialize the Header Object
        header = new Header("PantryPal");
        // Initialize the HomeFooter Object
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
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }

    public Button getNewRecipeButton() {
        return newRecipeButton;
    }

    public Button getFilterMealTypeButton() {
        return filterMealTypeButton;
    }

    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        newRecipeButton.setOnAction(eventHandler);
    }

    public void setFilterMealTypeButtonAction(EventHandler<ActionEvent> eventHandler) {
        filterMealTypeButton.setOnAction(eventHandler);
    }

}

// Filter Recipe Window to Display Recipes Based on a Specific Meal Type
class FilterFrame extends BorderPane {
    
    private Header header;
    private FilterPrompt filterPrompt;
    private RecordingFooter footer;
    private Button breakfastButton, lunchButton, dinnerButton;

    FilterFrame() {

        header = new Header("Filter Meal Type");
        filterPrompt = new FilterPrompt();
        footer = new RecordingFooter();

        this.setTop(header);
        this.setCenter(filterPrompt);
        this.setBottom(footer);

        breakfastButton = filterPrompt.getBreakfastButton();
        lunchButton = filterPrompt.getLunchButton();
        dinnerButton = filterPrompt.getDinnerButton();

    }

    public void setBreakfastButtonAction(EventHandler<ActionEvent> eventHandler) {
        breakfastButton.setOnAction(eventHandler);
    }

    public void setLunchButtonAction(EventHandler<ActionEvent> eventHandler) {
        lunchButton.setOnAction(eventHandler);
    }

    public void setDinnerButtonAction(EventHandler<ActionEvent> eventHandler) {
        dinnerButton.setOnAction(eventHandler);
    }

}
// Saved Full Recipe Window
class RecipeFrame extends BorderPane {

    private Header header;
    private RecipeFooter footer;
    private RecipeSteps recipeSteps;
    private Button cancelButton, saveButton, deleteButton;

    RecipeFrame() {
        header = new Header("Recipe");
        recipeSteps = new RecipeSteps();
        footer = new RecipeFooter();

        cancelButton = footer.getCancelButton();
        saveButton = footer.getSaveButton();
        deleteButton = footer.getDeleteButton();

        ScrollPane s = new ScrollPane(recipeSteps);
        s.setFitToHeight(true);
        s.setFitToWidth(true);

        // Add header to the top, recipe content to center, and footer to bottom of Border Pane
        this.setTop(header);
        this.setCenter(s);
        this.setBottom(footer);
    }

    public RecipeSteps getRecipeSteps() {
        return recipeSteps;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

    // Save updates then redirect to Home Page
    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler) {
        saveButton.setOnAction(eventHandler);
    }

    // Delete Recipe from database and app then redirect to Home Page
    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
        deleteButton.setOnAction(eventHandler);
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

// Record Meal Type Window
class MealFrame extends BorderPane {

    private Button startButton, stopButton, cancelButton;
    private Label recordingLabel;
    private Header header;
    private RecordingFooter footer;
    private Prompt prompt;

    MealFrame() {
        header = new Header("Record Meal Type");
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        prompt = new Prompt("What meal type would you like: \n Breakfast, Lunch, or Dinner?");
        
        this.setTop(header);
        this.setCenter(prompt);
        this.setBottom(footer);

        startButton = prompt.getStartButton();
        stopButton = prompt.getStopButton();
        recordingLabel = prompt.getRecordingLabel();
        cancelButton = footer.getCancelButton();
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Label getRecordingLabel() {
        return recordingLabel;
    }

    public Prompt getPrompt() {
        return prompt;
    }

    // Writes audio into "recording.wav"
    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    // Needs to detect either "Breakfast," "Lunch," or "Dinner" to move to next Frame
    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopButton.setOnAction(eventHandler);
    }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

// Record Ingredients Window
class IngredientsFrame extends BorderPane {

    private Button startButton, stopButton, cancelButton;
    private Label recordingLabel;
    private Header header;
    private RecordingFooter footer;
    private Prompt prompt;
    public String mealType;
    
    IngredientsFrame() {
        header = new Header("Record Ingredients");
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        prompt = new Prompt("");
        
        this.setTop(header);
        this.setCenter(prompt);
        this.setBottom(footer);

        startButton = prompt.getStartButton();
        stopButton = prompt.getStopButton();
        recordingLabel = prompt.getRecordingLabel();
        cancelButton = footer.getCancelButton();
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Label getRecordingLabel() {
        return recordingLabel;
    }

    public Prompt getPrompt() {
        return prompt;
    }

    public String setMealType(String mealType) {
        return this.mealType = mealType;
    }

    // Writes audio into "recording.wav"
    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    // Needs to detect either "Breakfast," "Lunch," or "Dinner" to move to next Frame
    public void setStopButtonAction(EventHandler<ActionEvent> eventHandler) {
        stopButton.setOnAction(eventHandler);
    }

    // Cancel Button goes to Home Page
    public void setCancelButtonAction(EventHandler<ActionEvent> eventHandler) {
        cancelButton.setOnAction(eventHandler);
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

// ChatGPT Generated Recipe Window
class GptFrame extends BorderPane {

    private Header header;
    private GptFooter footer;
    private Button saveButton, cancelButton, refreshButton;
    private String generatedText = "TWO Bacon, Eggs, and Sausage Breakfast+4 slices bacon, 2 eggs, 2 sausage links+1. In a medium skillet over medium heat, cook the bacon until crispy. 2. Remove bacon from skillet, leaving renderings in the pan. Add sausage and cook until browned on both sides. 3. Push sausage to one side and crack two eggs into the other side. Fry over medium heat until desired doneness. 4. Serve bacon, eggs, and sausage together.";
    private Recipe newRecipe;
    private TextArea recipeText = new TextArea();
    private ImageView imageView = new ImageView();
    private Image image;
    //String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 75px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    
    GptFrame() {
        this.setPrefSize(370, 120);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        header = new Header("New Recipe");
        footer = new GptFooter();
        
        recipeText.setText(generatedText);
        recipeText.setWrapText(true);
        recipeText.setMaxWidth(350);
        recipeText.setPadding(new Insets(5));

        VBox vboxContainer = new VBox();
        vboxContainer.getChildren().addAll(imageView, recipeText);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(vboxContainer);
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

    public ImageView getImageView(){
        return imageView;
    }

    public Image getImage(){
        return image;
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

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}


//=============================== VIEW ======================================

public class View {

    LoginFrame login;
    HomeFrame home;
    MealFrame meal;
    IngredientsFrame ingredients;
    GptFrame gpt;
    RecipeFrame recipe;
    FilterFrame filter;

    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    String clickedButtonStyle = "-fx-background-color: #0174BE; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    
    public View () {
        // // Setting the Layout of the Window- Should contain a Header, Footer and content for each Frame
        login = new LoginFrame();
        home = new HomeFrame();
        meal = new MealFrame();
        ingredients = new IngredientsFrame();
        gpt = new GptFrame();
        recipe = new RecipeFrame();
        filter = new FilterFrame();
    }

    public LoginFrame getLoginFrame() {
        return login;
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

    public FilterFrame getFilterFrame() {
        return filter;
    }

    public String getDefaultButtonStyle() {
        return defaultButtonStyle;
    }

    public String getClickedButtonStyle() {
        return clickedButtonStyle;
    }
    
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}