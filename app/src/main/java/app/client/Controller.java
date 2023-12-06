package app.client;

import java.util.HashMap;
import java.util.Map;

import app.server.ServerChecker;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

// Handles switching Scenes upon clicking buttons
class FrameController {

    private Map<String, Scene> frameMap;
    private Stage primaryStage;

    FrameController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        frameMap = new HashMap<>();
    }

    public void addFrame(String name, Scene scene) {
        frameMap.put(name, scene);
    }

    public void getFrame(String name) {
        primaryStage.setScene(frameMap.get(name));
    }

}

public class Controller {

    private View view;
    private Model model;
    private FrameController frameController;
    private String[] recipeParts;
    private String username, password, mealType, ingredients, fullRecipe, recipeTitle;
    private RecipeList recipeList;
    private String dalleResponse;

    public Controller(View view, Model model, Stage primaryStage) {
        this.view = view;
        this.model = model;
        frameController = new FrameController(primaryStage);
        recipeList = view.getHomeFrame().getRecipeList();

        // LoginFrame Event Listeners
        view.getLoginFrame().setLoginButtonAction(this::handleLoginButton);
        view.getLoginFrame().setCreateAccountButtonAction(this::handleCreateAccountButton);

        // HomeFrame Event Listeners
        view.getHomeFrame().setNewRecipeButtonAction(this::handleNewRecipeButton);
        view.getHomeFrame().setFilterMealTypeButtonAction(this::handleFilterMealTypeButton);
        view.getHomeFrame().setSignOutButtonAction(this::handleSignOutButton);

        // MealFrame Event Listeners
        view.getMealFrame().setStartButtonAction(this::handleMealStartButton);
        view.getMealFrame().setStopButtonAction(this::handleMealStopButton);
        view.getMealFrame().setCancelButtonAction(this::handleMealCancelButton);

        // IngredientsFrame Event Listeners
        view.getIngredientsFrame().setStartButtonAction(this::handleIngredientsStartButton);
        view.getIngredientsFrame().setStopButtonAction(this::handleIngredientsStopButton);
        view.getIngredientsFrame().setCancelButtonAction(this::handleIngredientsCancelButton);

        // GptFrame Event Listeners
        view.getGptFrame().setSaveButtonAction(this::handleGptSaveButton);
        view.getGptFrame().setCancelButtonAction(this::handleGptCancelButton);
        view.getGptFrame().setRefreshButtonAction(this::handleGptRefreshButton);

        // RecipeFrame Event Listeners
        view.getRecipeFrame().setCancelButtonAction(this::handleRecipeCancelButton);
        view.getRecipeFrame().setSaveButtonAction(this::handleRecipeSaveButton);
        view.getRecipeFrame().setDeleteButtonAction(this::handleRecipeDeleteButton);
        view.getRecipeFrame().setShareButtonAction(this::handleShareButton);
        
        // FilterFrame Event Listerners
        view.getFilterFrame().setBreakfastButtonAction(this::handleFilterBreakfastButton);
        view.getFilterFrame().setLunchButtonAction(this::handleFilterLunchButton);
        view.getFilterFrame().setDinnerButtonAction(this::handleFilterDinnerButton);
        view.getFilterFrame().setAllButtonAction(this::handleFilterAllButton);
        view.getFilterFrame().setCancelButtonAction(this::handleFilterCancelButton);


        // ShareFrame Event Listeners
        view.getShareFrame().setCancelButtonAction(this::handleShareCancelButton);

    }

    public FrameController getFrameController() {
        return frameController;
    }

    //================ LoginFrame Event Handlers ====================================================

    private void handleLoginButton(ActionEvent event) {
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", null, null, null, username, "load-recipe");
            clearRecipes();
            loadRecipes(recipes);
            frameController.getFrame("home");
            System.out.println("[ Frame changed ]");
        } else if (response.equals("INVALID CREDENTIALS") || response.equals("USER NOT FOUND")){
            System.out.println("[ LOGIN RESPONSE ] " + response);
        } else {
            view.showAlert("Error", response);
        }

    }

    private void handleCreateAccountButton(ActionEvent event) {
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();
        // checks if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }
        
        String response = model.performRequest("POST", username, password, null, null, "signup");


        if (response.equals("NEW USER CREATED")) {
            // Redirect back to Login Page if new user successfully created
            frameController.getFrame("login");
        } else {
            System.out.println("[ SIGNUP RESPONSE ] " + response);
        }
    }

    //================ HomeFrame Event Handlers ====================================================

    private void handleNewRecipeButton(ActionEvent event) {
        frameController.getFrame("meal");
    }

    private void handleFilterMealTypeButton(ActionEvent event) {
        frameController.getFrame("filter");
    }

    private void handleViewButton(ActionEvent event) {
        Button target = (Button) event.getTarget();
        recipeTitle = (String) ((TextField) ((HBox) target.getParent()).getChildren().get(1)).getText();
        String recipeText = model.performRequest("GET", username, null, null, recipeTitle, "");
        //recipeText = recipeText.replace();
        String imgString = model.performRequest("GET", username, null, null, recipeTitle, "picture");
        

        // checks if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false) {
            view.showAlert("Error", "Server connection was interrupted");
        } 
        // Displays the image and the recipe
        displayImage(imgString);
        displayRecipe(recipeText);

        frameController.getFrame("recipe");
    }

    private void handleSignOutButton(ActionEvent event) {
        view.getLoginFrame().getLoginContent().getUsername().setText("");
        view.getLoginFrame().getLoginContent().getPassword().setText("");
        view.getLoginFrame().getLoginContent().getUsername().setPromptText("Username");
        view.getLoginFrame().getLoginContent().getPassword().setPromptText("Password");
        username = "";
        password = "";

        frameController.getFrame("login");
    }
    //================ MealFrame and IngredientsFrame Event Handlers ===============================

    private void handleMealStartButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getClickedButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());
        view.getMealFrame().getRecordingLabel().setVisible(true);

        model.startRecording();
    }

    private void handleMealStopButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getDefaultButtonStyle());
        stopButton.setStyle(view.getClickedButtonStyle());
        view.getMealFrame().getRecordingLabel().setVisible(false);

        model.stopRecording();
         // checks if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }

        mealType = model.performRequest("POST", null, null, null, null, "whisper");
        mealType = model.transcribeMealType(mealType);
        
        System.out.println("MEALTYPE CONTROLLER: " + mealType);

        if (mealType.equals("")) {
            view.getMealFrame().getPrompt().getText().setText("Invalid input. Please select either \n Breakfast, Lunch, or Dinner.");
        } else if (mealType.equals("breakfast") || mealType.equals("lunch") || mealType.equals("dinner")) {
            // Update prompt for IngredientsFrame to include meal type then change the frame
            view.getIngredientsFrame().getPrompt().getText().setText("You have selected " + mealType + "\n List your ingredients:");
            frameController.getFrame("ingredients");

            // Reset prompt and button styles
            view.getMealFrame().getPrompt().getText().setText("What meal type would you like: \n Breakfast, Lunch, or Dinner?");
            startButton.setStyle(view.getDefaultButtonStyle());
            stopButton.setStyle(view.getDefaultButtonStyle());
        }

    }

    private void handleMealCancelButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getDefaultButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());

        model.stopRecording();

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    //=================== IngredientsFrame Event Handlers ==================================

    private void handleIngredientsStartButton(ActionEvent event) {
        Button startButton = view.getIngredientsFrame().getStartButton();
        Button stopButton = view.getIngredientsFrame().getStopButton();
        startButton.setStyle(view.getClickedButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());
        view.getIngredientsFrame().getRecordingLabel().setVisible(true);

        model.startRecording();
    }

    private void handleIngredientsStopButton(ActionEvent event) {
        Button startButton = view.getIngredientsFrame().getStartButton();
        Button stopButton = view.getIngredientsFrame().getStopButton();
        startButton.setStyle(view.getDefaultButtonStyle());
        stopButton.setStyle(view.getClickedButtonStyle());
        view.getMealFrame().getRecordingLabel().setVisible(false);

        model.stopRecording();
         // checks if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }

        ingredients = model.performRequest("POST", null, null, null, null, "whisper");
        System.out.println(ingredients);

        // Create prompt with mealType and ingredients and pass to ChatGPT API, Dall-E API for the picture
        String prompt = "Please provide a" + mealType + " recipe using" + ingredients
             + ". Can you format the response to have zero newlines with fields \"Title:\",\"Ingredients:\", and \"Instructions:\" .These" 
             + " fields will be formated such that it looks like \"Title:\"+\"Ingredients:\"+\"Instructions\" with one newline between each field";
        System.out.println("PROMPT +++ " + prompt);
        String response = model.performRequest("POST", null, null, prompt, null, "chatgpt");
        fullRecipe = response;

        recipeParts = response.split("\\+");
        recipeTitle = recipeParts[0];
        response = response.replace("+", "\n");

        String dallePrompt = "Generate a real picture of " + recipeTitle;
        dalleResponse = model.performRequest("POST", null, null, dallePrompt, null, "dalle");

        Image image = new Image(dalleResponse);

        view.getGptFrame().getImageView().setImage(image);
        view.getGptFrame().getRecipeText().setText(response);

        // Change scenes after getting response
        frameController.getFrame("gpt");
        // Reset buttons to origin style
        startButton.setStyle(view.getDefaultButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());
    }

    private void handleIngredientsCancelButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getDefaultButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());

        // Redirect back to Home Page
        frameController.getFrame("home");
    }
    
    //=============== GptFrame Event Handlers =============================

    private void handleGptSaveButton(ActionEvent event) {
        String recipeName = view.getGptFrame().getRecipeText().getText().split("\n")[0];
        Recipe newRecipe = new Recipe();
        newRecipe.getRecipe().setText(recipeName);
        displayMealType(newRecipe, mealType);
        newRecipe.setViewButtonAction(this::handleViewButton);

        fullRecipe += "+" + mealType + "+" + dalleResponse;
        String fullRecipeList = model.performRequest("GET", null, null, null, username, "load-recipe");


        //check if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }

        clearRecipes();
        loadRecipes(fullRecipeList);
        recipeList.getChildren().add(0, newRecipe);
        updateRecipeIndices();
        
        model.performRequest("POST", username, null, fullRecipe, null, "");
        


        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    // takes the same input for mealtype and ingredients,
    // tells ChatGPT to regenerate response with the set of ingredients
    private void handleGptRefreshButton(ActionEvent event) {
        
        String prompt = "Please provide a" + mealType + " recipe using" + ingredients
             + ". Can you format the response to have zero newlines with fields \"Title:\",\"Ingredients:\", and \"Instructions:\" .These" 
             + " fields will be formated such that it looks like \"Title:\"+\"Ingredients:\"+\"Instructions\" with one newline between each field";
        String response = model.performRequest("POST", null, null, prompt, null, "chatgpt");
        //check if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }

        fullRecipe = response;

        recipeParts = response.split("\\+");
        recipeTitle = recipeParts[0];
        response = response.replace("+", "\n");

        String dallePrompt = "Generate a real picture of " + recipeTitle;
        String dalleResponse = model.performRequest("POST", null, null, dallePrompt, null, "dalle");
        
        Image image = new Image(dalleResponse); 

        response = response.replace("+", "\n");

        view.getGptFrame().getImageView().setImage(image);
        view.getGptFrame().getRecipeText().setText(response);

    }

    // Cancels the request for ChatGPT, goes back to home screen to restart
    private void handleGptCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }

    //================ RecipeFrame Event Handlers =============================

    private void handleRecipeCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }

    private void handleRecipeSaveButton(ActionEvent event) {
       
        String updatedRecipe = view.getRecipeFrame().getRecipeSteps().getTextArea().getText();
        updatedRecipe = updatedRecipe.replace("\n\n","+");
        System.out.println("CLEANED newlines"+ updatedRecipe);
        //Make PUT request and save updatedRecipe as second param
        String response = model.performRequest("PUT", username, null, updatedRecipe, null, "");
        
        //check if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }
        System.out.println("[PUT RESPONSE] " + response);
        frameController.getFrame("home");
    }


    private void handleRecipeDeleteButton(ActionEvent event) {
        int delim = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().indexOf("\n");
        String recipeTitle = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().substring(0, delim);
        String response = model.performRequest("DELETE", username, null, null, recipeTitle, "");
       
        //check if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }

        System.out.println("[DELETE RESPONSE] " + response);
        frameController.getFrame("home");
        String recipes = model.performRequest("GET", null, null, null, username, "load-recipe");
        clearRecipes();
        loadRecipes(recipes);
    }

    private void handleShareButton(ActionEvent event) {
        String recipeTitle = view.getRecipeFrame().getRecipeSteps().getRecipeName().getText();
        String response = "http://localhost:8100/share/?u=" + username + "&q="+ recipeTitle;
        System.out.println("[SHARE RESPONSE] " + response);
        view.getShareFrame().getShareArea().setText(response);
        frameController.getFrame("share");
    }


    //===================== FilterFrame Handlers ================================

    private void handleFilterBreakfastButton(ActionEvent event) {
        String response = model.performRequest("GET", username, null, null, "breakfast", "mealtype");
        checkServer();
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    private void handleFilterLunchButton(ActionEvent event) {
        checkServer();
        String response = model.performRequest("GET", username, null, null, "lunch", "mealtype");

        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    private void handleFilterDinnerButton(ActionEvent event) {
        String response = model.performRequest("GET", username, null, null, "dinner", "mealtype");
        //check if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }

        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    private void handleFilterAllButton(ActionEvent event) {
        String response = model.performRequest("GET", null, null, null, username, "load-recipe");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    private void handleFilterCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }
    
    //=================== ShareFrame EventListner ==============

    private void handleShareCancelButton(ActionEvent event) {
        frameController.getFrame("recipe");
    }

    //=================== HELPER FUNCTIONS ====================
    
    private void displayMealType(Recipe recipe, String res) {
        if (res.equals("breakfast")) {
            recipe.getMealType().setText("B");
            recipe.getMealType().setStyle("-fx-background-color: #39A7FF; -fx-font-size: 14; -fx-border-radius: 20; -fx-text-fill: white;");
        } else if (res.equals("lunch")) {
            recipe.getMealType().setText("L");
            recipe.getMealType().setStyle("-fx-background-color: #79AC78; -fx-font-size: 14; -fx-border-radius: 20; -fx-text-fill: white;");
        } else if (res.equals("dinner")) {
            recipe.getMealType().setText("D");
            recipe.getMealType().setStyle("-fx-background-color: #BE3144; -fx-font-size: 14; -fx-border-radius: 20; -fx-text-fill: white;");
        } else { 
            // Display error message
        }
    }

    private void displayRecipe(String recipe) {
        try {
            String recipeName = recipe.split("\\+")[0];
            String recipeText = recipe.substring(recipe.indexOf("\\+") + 1);
            recipeText = recipeText.replace("+", "\n\n");
            System.out.println("RECIPE TEXT ON GET:" + recipeText);
            view.getRecipeFrame().getRecipeSteps().getRecipeName().setText(recipeName);
            view.getRecipeFrame().getRecipeSteps().getTextArea().setText(recipeText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayImage(String img){
        try {
            Image image = new Image(img);
            view.getRecipeFrame().getRecipeSteps().getImageView().setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load Recipes into Home Page once User has signed in
    public void loadRecipes(String recipes) {
        if (recipes != null) {
            String[] recipesArr = { recipes };
            if (recipes.contains("+")) {
                recipesArr = recipes.split("\\+");
            }
            int i = 0;
            while (i < recipesArr.length) {
                Recipe newRecipe = new Recipe();
                newRecipe.getRecipe().setText(recipesArr[i++]);
                newRecipe.setViewButtonAction(this::handleViewButton);
                recipeList.getChildren().add(0, newRecipe);
                String meal = recipesArr[i++];
                displayMealType(newRecipe, meal);
                updateRecipeIndices();
            }
        }
    }

    public void clearRecipes() {
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Recipe) {
                ((Recipe) recipeList.getChildren().get(i)).setRecipeIndex(0);
            }
        }
        recipeList.getChildren().clear();
    }

    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Recipe) {
                ((Recipe) recipeList.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    public void checkServer() {
        //check if server is still running
        boolean checker = ServerChecker.isServerRunning("localhost", 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }
    }

}
