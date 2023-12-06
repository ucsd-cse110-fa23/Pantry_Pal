package app.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

import app.client.*;
import app.client.views.*;
import app.server.ServerChecker;

public class Controller {
    private View view;
    private Model model;
    private FrameController frameController;
    private String[] recipeParts;
    private String username, password, mealType, ingredients, fullRecipe, recipeTitle;
    private RecipeList recipeList;
    private String dalleResponse;

    public Controller(View view, Model model, Stage primaryStage) throws IOException {
        this.view = view;
        this.model = model;
        frameController = new FrameController(primaryStage);
        recipeList = view.getHomeFrame().getRecipeList();

        // LoginFrame Event Listeners
        view.getLoginFrame().setLoginButtonAction(event -> {
            try {
                handleLoginButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        view.getLoginFrame().setCreateAccountButtonAction(event -> {
            try {
                handleCreateAccountButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        view.getLoginFrame().setAutoLoginButtonAction(event -> {
            try {
                handleAutoLoginButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // HomeFrame Event Listeners
        view.getHomeFrame().setNewRecipeButtonAction(this::handleNewRecipeButton);
        view.getHomeFrame().setFilterMealTypeButtonAction(this::handleFilterMealTypeButton);
        view.getHomeFrame().setSortButtonAction(this::handleSortButton);
        view.getHomeFrame().setAutoLoginButtonAction(event -> {
            try {
                handleAutoLoginButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        view.getHomeFrame().setSignOutButtonAction(event -> {
            try {
                handleSignOutButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
        
        // FilterFrame Event Listeners
        view.getFilterFrame().setBreakfastButtonAction(this::handleFilterBreakfastButton);
        view.getFilterFrame().setLunchButtonAction(this::handleFilterLunchButton);
        view.getFilterFrame().setDinnerButtonAction(this::handleFilterDinnerButton);
        view.getFilterFrame().setAllButtonAction(this::handleFilterAllButton);
        view.getFilterFrame().setCancelButtonAction(this::handleFilterCancelButton);

        // ShareFrame Event Listeners
        view.getShareFrame().setCancelButtonAction(this::handleShareCancelButton);

        // SortFrame Event Listeners
        view.getSortFrame().setAlphaButtonAction(this::handleSortAlphaButton);
        view.getSortFrame().setRAlphaButtonAction(this::handleSortRAlphaButton);
        view.getSortFrame().setChronoButtonAction(this::handleSortChronoButton);
        view.getSortFrame().setRChronoButtonAction(this::handleSortRChronoButton);
        view.getSortFrame().setCancelButtonAction(this::handleSortCancelButton);

        // Auto Login Initializer
        boolean autoLoginEnabled = model.getAutoLoginStatus();
        if(autoLoginEnabled) {
            view.getLoginFrame().getAutoLoginButton().setStyle("-fx-text-fill: green;");
            view.getLoginFrame().getAutoLoginButton().setText("ON");
            view.getHomeFrame().getAutoLoginButton().setStyle("-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial; -fx-text-fill: green;");
            view.getHomeFrame().getAutoLoginButton().setText("ON");
            String[] loginDetails = model.getAutoLoginDetails().split("\n");
            if(loginDetails[0].equals("") == false) {
                handleLogin(loginDetails[0], loginDetails[1]);
            }
        } else {
            view.getLoginFrame().getAutoLoginButton().setStyle("-fx-text-fill: red;");
            view.getLoginFrame().getAutoLoginButton().setText("OFF");
            view.getHomeFrame().getAutoLoginButton().setStyle("-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial; -fx-text-fill: red;");
            view.getHomeFrame().getAutoLoginButton().setText("OFF");
        }
        
        
    }


    public FrameController getFrameController() {
        return frameController;
    }

    //================ LoginFrame Event Handlers ====================================================

    private void handleLoginButton(ActionEvent event) throws IOException {
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();
        if(!model.getIsLoggedIn()) {
        if (username.equals("") || password.equals("")) {
            view.showAlert("Input Error", "Required field(s) missing!");
        } else {
            handleLogin(username, password);
            if(model.getAutoLoginStatus() && model.getIsLoggedIn()) {
                model.setAutoLoginDetails(username, password);
            }
        }
    }
    }

    private void handleAutoLoginButton(ActionEvent event) throws IOException {
        boolean autoLoginEnabled = model.getAutoLoginStatus();
        autoLoginEnabled = !autoLoginEnabled;
        model.setAutoLoginStatus(autoLoginEnabled);
        if(autoLoginEnabled) {
            view.getLoginFrame().getAutoLoginButton().setStyle("-fx-text-fill: green;");
            view.getLoginFrame().getAutoLoginButton().setText("ON");
            view.getHomeFrame().getAutoLoginButton().setStyle("-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial; -fx-text-fill: green;");
            view.getHomeFrame().getAutoLoginButton().setText("ON");
            String[] loginDetails = model.getAutoLoginDetails().split("\n");
            if(loginDetails[0].equals("") == false) {
                handleLogin(loginDetails[0], loginDetails[1]);
            }
        } else {
            view.getLoginFrame().getAutoLoginButton().setStyle("-fx-text-fill: red;");
            view.getLoginFrame().getAutoLoginButton().setText("OFF");
            view.getHomeFrame().getAutoLoginButton().setStyle("-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial; -fx-text-fill: red;");
            view.getHomeFrame().getAutoLoginButton().setText("OFF");
        }
    }

    private void handleCreateAccountButton(ActionEvent event) throws IOException {
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        if (username.equals("") || password.equals("")) {
            view.showAlert("Input Error", "Required field(s) missing!");
        } else {
            String response = model.performRequest("POST", username, password, null, null, "signup");
            if (response.equals("NEW USER CREATED")) {
                // Redirect back to Login Page if new user successfully created
                frameController.getFrame("login");
                if (model.getAutoLoginStatus()) {
                    model.setAutoLoginDetails(username, password);
                    model.setLogInDetails(username, password);
                    handleLogin(username, password);
                }   
            } else {
                System.out.println("[LOGIN RESPONSE] " + response);
            }
        }
    }

    //================ AppFrame Event Handlers ====================================================

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
        
        checkServer(); 
        // Displays the image and the recipe
        displayImage(imgString);
        displayRecipe(recipeText);

        frameController.getFrame("recipe");
    }
    
    private void handleSortButton(ActionEvent event) {
        frameController.getFrame("sort");
    }

    private void handleSignOutButton(ActionEvent event) throws IOException {
        view.getLoginFrame().getLoginContent().getUsername().setText("");
        view.getLoginFrame().getLoginContent().getPassword().setText("");
        view.getLoginFrame().getLoginContent().getUsername().setPromptText("Username");
        view.getLoginFrame().getLoginContent().getPassword().setPromptText("Password");
        username = "";
        password = "";
        model.setIsLoggedIn();
        model.setLogInDetails(username, password);
        if(model.getAutoLoginStatus()) {
            model.setAutoLoginStatus(false);
            model.setAutoLoginStatus(true);
        }
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
        checkServer();

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
        checkServer();

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
        displayMealTypeTag(newRecipe, mealType);
        newRecipe.setViewButtonAction(this::handleViewButton);

        fullRecipe += "+" + mealType + "+" + dalleResponse;
        String fullRecipeList = model.performRequest("GET", null, null, null, username, "load-recipe");

        checkServer();

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
        checkServer();

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
        checkServer();
        String updatedRecipe = view.getRecipeFrame().getRecipeSteps().getTextArea().getText();
        updatedRecipe = updatedRecipe.replace("\n\n","+");
        System.out.println("CLEANED newlines"+ updatedRecipe);
        //Make PUT request and save updatedRecipe as second param
        String response = model.performRequest("PUT", username, null, updatedRecipe, null, "");
        System.out.println("[PUT RESPONSE] " + response);
        frameController.getFrame("home");
    }


    private void handleRecipeDeleteButton(ActionEvent event) {
        checkServer();
        int delim = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().indexOf("\n");
        String recipeTitle = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().substring(0, delim);
        String response = model.performRequest("DELETE", username, null, null, recipeTitle, "");

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
        checkServer();
        String response = model.performRequest("GET", username, null, null, "dinner", "mealtype");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    private void handleFilterAllButton(ActionEvent event) {
        checkServer();
        String response = model.performRequest("GET", null, null, null, username, "load-recipe");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    private void handleFilterCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }
    
    //=================== ShareFrame Event Handler ==============

    private void handleShareCancelButton(ActionEvent event) {
        frameController.getFrame("recipe");
    }

    //================ SortFrame Event Handlers ====================================================

    private void handleSortAlphaButton(ActionEvent event) {
        if(recipeList.getChildren().size() == 0) {
            frameController.getFrame("home");
            return;
        }

        clearRecipes();

        String recipes = model.performRequest("GET", null, null, null, username, "load-recipe");

        String order = model.sortAlphabetically(recipes);
        System.out.println("NEW SORTED: " + order);
        loadRecipes(order);
        updateRecipeIndices();
        // updateViewButton();

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    private void handleSortRAlphaButton(ActionEvent event) {
        if(recipeList.getChildren().size() == 0){
            frameController.getFrame("home");
            return;
        }
        
        clearRecipes();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", username, null, null, username, "load-recipe");

            String order = model.sortRAlphabetically(recipes);
            System.out.println("NEW SORTED: " + order);
            loadRecipes(order);
            updateRecipeIndices();
            // updateViewButton();
            frameController.getFrame("home");
            System.out.println("|||Frame changed|||");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    private void handleSortChronoButton(ActionEvent event) {
        if(recipeList.getChildren().size() == 0){
            frameController.getFrame("home");
            return;
        }
        recipeList.getChildren().clear();
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", username, null, null, username, "load-recipe");
            String order = model.sortChronological(recipes);
            System.out.println("NEW SORTED: " + order);
            loadRecipes(order);
            updateRecipeIndices();
            // updateViewButton();
            frameController.getFrame("home");
            System.out.println("|||Frame changed|||");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    private void handleSortRChronoButton(ActionEvent event) {
        if(recipeList.getChildren().size() == 0){
            frameController.getFrame("home");
            return;
        }
        recipeList.getChildren().clear();
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", username, null, null, username, "load-recipe");
            
            String order = model.sortRChronological(recipes);
            System.out.println("NEW SORTED: " + order);
            loadRecipes(order);
            updateRecipeIndices();
            // updateViewButton();
            frameController.getFrame("home");
            System.out.println("|||Frame changed|||");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    private void handleSortCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }

    //================== HELPER METHODS ========================================

    private void displayMealTypeTag(Recipe recipe, String res) {
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
            if (recipes.contains("_")) {
                recipesArr = recipes.split("_");
            }
            for (int i = 0; i < recipesArr.length; i++) {
                Recipe newRecipe = new Recipe();
                newRecipe.getRecipe().setText(recipesArr[i].split("\\+")[0]);
                newRecipe.setViewButtonAction(this::handleViewButton);
                recipeList.getChildren().add(0, newRecipe);
                String meal = recipesArr[i].split("\\+")[1];
                displayMealTypeTag(newRecipe, meal);
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

    private void handleLogin(String username, String password) {
        if(!model.getIsLoggedIn()) {
            String response = model.performRequest("POST", username, password, null, null, "login");
            if (response.equals("SUCCESS")) {
                model.setIsLoggedIn();
                model.setLogInDetails(username, password);
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
    }

}
