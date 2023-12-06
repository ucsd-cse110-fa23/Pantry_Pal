package app.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

import app.client.*;
import app.client.views.*;
import app.server.ServerChecker;

/**
 * Controller.java
 * 
 * Presenter passing data from model and updating view UI
 */
public class Controller {
    private View view;
    private Model model;
    private FrameController frameController;
    private String[] recipeParts;
    private String username, password, mealType, ingredients, fullRecipe, recipeTitle;
    private RecipeList recipeList;
    private String dalleResponse;

    /**
     * Constructor composes view and model and takes primaryStage to switch scenes
     * 
     * @param view
     * @param model
     * @param primaryStage
     * @throws IOException
     */
    public Controller(View view, Model model, Stage primaryStage) throws IOException {
        this.view = view;
        this.model = model;
        frameController = new FrameController(primaryStage);
        recipeList = view.getHomeFrame().getRecipeList();

        // LoginFrame Event Listeners - All read from csv file so they need IOException
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
        if (autoLoginEnabled) {
            view.getLoginFrame().getAutoLoginButton().setStyle("-fx-text-fill: green;");
            view.getLoginFrame().getAutoLoginButton().setText("ON");
            view.getHomeFrame().getAutoLoginButton().setStyle("-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial; -fx-text-fill: green;");
            view.getHomeFrame().getAutoLoginButton().setText("ON");
            String[] loginDetails = model.getAutoLoginDetails().split("\n");
            if(loginDetails[0].equals("") == false) {
                username = loginDetails[0];
                password = loginDetails[1];
                handleLogin(username, password);
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

        // Shows alert if username/password fields are not filled out
        if (username.equals("") || password.equals("")) {
            view.showAlert("Input Error", "Required field(s) missing!");
        } else {
            handleLogin(username, password);
            if (model.getAutoLoginStatus() && model.getIsLoggedIn()) {
                model.setAutoLoginDetails(username, password);
            }
        }
    }

    private void handleAutoLoginButton(ActionEvent event) throws IOException {
        boolean autoLoginEnabled = model.getAutoLoginStatus();
        autoLoginEnabled = !autoLoginEnabled;
        model.setAutoLoginStatus(autoLoginEnabled);
        // If enable, style button to have green ON on login and home screen and pull credentials from csv file
        if (autoLoginEnabled) {
            view.getLoginFrame().getAutoLoginButton().setStyle("-fx-text-fill: green;");
            view.getLoginFrame().getAutoLoginButton().setText("ON");
            view.getHomeFrame().getAutoLoginButton().setStyle("-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial; -fx-text-fill: green;");
            view.getHomeFrame().getAutoLoginButton().setText("ON");
            String[] loginDetails = model.getAutoLoginDetails().split("\n");
            if(loginDetails[0].equals("") == false) {
                username = loginDetails[0];
                password = loginDetails[1];
                handleLogin(username, password);
            }
        // Else, button on Login and Home page have red OFF button
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

        // Throw same alert for missing fields as with Login Button
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

    //================ HomeFrame Event Handlers ====================================================

    // Redirect to page to record meal type
    private void handleNewRecipeButton(ActionEvent event) {
        frameController.getFrame("meal");
    }

    // Redirect to page to filter meal type
    private void handleFilterMealTypeButton(ActionEvent event) {
        frameController.getFrame("filter");
    }

    // Connect view button OnClick of specific recipe to its corresponding content
    private void handleViewButton(ActionEvent event) {
        // Source button of recipe where view was fired
        Button target = (Button) event.getTarget();
        // Grab container holding button then move to child holding title of the recipe
        recipeTitle = (String) ((Label) ((HBox) target.getParent()).getChildren().get(1)).getText();
        String recipeText = model.performRequest("GET", username, null, null, recipeTitle, "");
        String imgString = model.performRequest("GET", username, null, null, recipeTitle, "picture");
        
        checkServer(); 
        // Displays the image and the recipe
        displayImage(imgString);
        displayRecipe(recipeText);

        frameController.getFrame("recipe");
    }
    
    // Redirect to sort button
    private void handleSortButton(ActionEvent event) {
        frameController.getFrame("sort");
    }

    // Clears cached credentials in controller and from model
    private void handleSignOutButton(ActionEvent event) {
        view.getLoginFrame().getLoginContent().getUsername().setText("");
        view.getLoginFrame().getLoginContent().getPassword().setText("");
        view.getLoginFrame().getLoginContent().getUsername().setPromptText("Username");
        view.getLoginFrame().getLoginContent().getPassword().setPromptText("Password");
        username = "";
        password = "";
        
        model.setIsLoggedIn(false);

        frameController.getFrame("login");
    }

    //================ MealFrame Event Handlers ==========================================

    // Change button style and allow audio file to start receiving audio
    private void handleMealStartButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getClickedButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());
        view.getMealFrame().getRecordingLabel().setVisible(true);

        model.startRecording();
    }

    // Change button style, stop recording, and pass to whisper
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

        // Issues new prompt if Whisper (then model) could not detect meal type
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

    // Clear styling, and return Home
    private void handleMealCancelButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getDefaultButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());

        // Stops recording if start button was pressed then cancel
        model.stopRecording();

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    //=================== IngredientsFrame Event Handlers ==================================

    // Change button style the start recording
    private void handleIngredientsStartButton(ActionEvent event) {
        Button startButton = view.getIngredientsFrame().getStartButton();
        Button stopButton = view.getIngredientsFrame().getStopButton();
        startButton.setStyle(view.getClickedButtonStyle());
        stopButton.setStyle(view.getDefaultButtonStyle());
        view.getIngredientsFrame().getRecordingLabel().setVisible(true);

        model.startRecording();
    }

    // Change button style then feeds input into ChatGPT API to create a new recipe
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

        // Cancels recording if started but never stopped
        model.stopRecording();

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

        // Grab recipe name from UI in case it was edited before saving, the separate from the rest of the text
        fullRecipe = view.getGptFrame().getRecipeText().getText().replace("\n","+");
        fullRecipe += "+" + mealType + "+" + dalleResponse;

        String fullRecipeList = model.performRequest("GET", null, null, null, username, "load-recipe");

        checkServer();
        clearRecipes();
        loadRecipes(fullRecipeList);

        // Add newly saved recipe to top of recipeList then update indices
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
        dalleResponse = model.performRequest("POST", null, null, dallePrompt, null, "dalle");
        
        Image image = new Image(dalleResponse); 

        // Change delimiter to make UI readible
        response = response.replace("+", "\n");

        view.getGptFrame().getImageView().setImage(image);
        view.getGptFrame().getRecipeText().setText(response);

    }

    // Cancels the request for ChatGPT, goes back to home screen to restart
    private void handleGptCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }

    //================ RecipeFrame Event Handlers =============================

    // Exits content of the recipe clicked
    private void handleRecipeCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }

    // Detects changes to recipe content then save in database
    private void handleRecipeSaveButton(ActionEvent event) {
        checkServer();
        String updatedRecipe = view.getRecipeFrame().getRecipeSteps().getTextArea().getText();
        updatedRecipe = updatedRecipe.replace("\n","+");
        //Make PUT request and save updatedRecipe as second param
        String response = model.performRequest("PUT", username, null, updatedRecipe, null, "");
        System.out.println("[ PUT RESPONSE ] " + response);
        frameController.getFrame("home");
    }

    // Deletes recipe from database and updates UI to no longer show it
    private void handleRecipeDeleteButton(ActionEvent event) {
        checkServer();
        int delim = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().indexOf("\n");
        String recipeTitle = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().substring(0, delim);
        String response = model.performRequest("DELETE", username, null, null, recipeTitle, "");

        System.out.println("[ DELETE RESPONSE ] " + response);
        frameController.getFrame("home");
        String recipes = model.performRequest("GET", null, null, null, username, "load-recipe");
        clearRecipes();
        loadRecipes(recipes);
    }

    // Generate URL that displays recipe on web
    private void handleShareButton(ActionEvent event) {
        String recipeTitle = view.getRecipeFrame().getRecipeSteps().getRecipeName().getText();
        String response = "http://"+Model.IPHOST+":8100/share/?u=" + username + "&q="+ recipeTitle;
        System.out.println("[ SHARE RESPONSE ] " + response);
        view.getShareFrame().getShareArea().setText(response);
        frameController.getFrame("share");
    }


    //===================== FilterFrame Handlers ================================

    // Display recipes only with "B" tag
    private void handleFilterBreakfastButton(ActionEvent event) {
        String response = model.performRequest("GET", username, null, null, "breakfast", "mealtype");
        checkServer();
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    // Displays recipes only with "L" tag
    private void handleFilterLunchButton(ActionEvent event) {
        checkServer();
        String response = model.performRequest("GET", username, null, null, "lunch", "mealtype");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    // Displays recipes with only "D" tag
    private void handleFilterDinnerButton(ActionEvent event) {
        checkServer();
        String response = model.performRequest("GET", username, null, null, "dinner", "mealtype");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    // Reverts list to show all recipes
    private void handleFilterAllButton(ActionEvent event) {
        checkServer();
        String response = model.performRequest("GET", null, null, null, username, "load-recipe");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    // Cancel page and return Home
    private void handleFilterCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }
    
    //=================== ShareFrame Event Handler ==============

    private void handleShareCancelButton(ActionEvent event) {
        frameController.getFrame("recipe");
    }

    //================ SortFrame Event Handlers ====================================================

    // Sort list with A-Z top-to-bottom
    private void handleSortAlphaButton(ActionEvent event) {
        if (recipeList.getChildren().size() == 0) {
            frameController.getFrame("home");
            return;
        }

        clearRecipes();

        String recipes = model.performRequest("GET", null, null, null, username, "load-recipe");

        String order = model.sortAlphabetically(recipes);
        loadSorted(order);
        updateRecipeIndices();

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    // Sorts Z-A top-to-bottom
    private void handleSortRAlphaButton(ActionEvent event) {
        if (recipeList.getChildren().size() == 0) {
            frameController.getFrame("home");
            return;
        }
        
        clearRecipes();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", username, null, null, username, "load-recipe");

            String order = model.sortRAlphabetically(recipes);
            System.out.println("NEW SORTED: " + order);
            loadSorted(order);
            updateRecipeIndices();

            frameController.getFrame("home");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    // Sorts Oldest-Newest top-to-bottom
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
            loadSorted(order);
            updateRecipeIndices();
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    // Sorts Newest-Oldest top-to-bottom
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
            loadSorted(order);
            updateRecipeIndices();
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    private void handleSortCancelButton(ActionEvent event) {
        frameController.getFrame("home");
    }

    //================== HELPER METHODS ===========================================================

    // Update meal type tags based on meal type, already handles that res must be on of the three types
    private void displayMealTypeTag(Recipe recipe, String res) {
        if (res.equals("breakfast")) {
            recipe.getMealType().setText("B");
            recipe.getMealType().setStyle(
                "-fx-background-color: #39A7FF; -fx-font-size: 14; -fx-border-radius: 20; -fx-text-fill: white;");
        } else if (res.equals("lunch")) {
            recipe.getMealType().setText("L");
            recipe.getMealType().setStyle(
                "-fx-background-color: #79AC78; -fx-font-size: 14; -fx-border-radius: 20; -fx-text-fill: white;");
        } else {
            recipe.getMealType().setText("D");
            recipe.getMealType().setStyle(
                "-fx-background-color: #BE3144; -fx-font-size: 14; -fx-border-radius: 20; -fx-text-fill: white;");
        } 
    }

    // Display content when view a certain recipe
    private void displayRecipe(String recipe) {
        try {
            String recipeName = recipe.split("\\+")[0];
            String recipeText = recipe.substring(recipe.indexOf("\\+") + 1);
            recipeText = recipeText.replace("+", "\n\n");
            view.getRecipeFrame().getRecipeSteps().getRecipeName().setText(recipeName);
            view.getRecipeFrame().getRecipeSteps().getTextArea().setText(recipeText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Display image corresponding to recipe and show next to content
    private void displayImage(String img) {
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

    // Loads top down instea of the normal bottom-up
    public void loadSorted(String recipes) {
        if (recipes != null) {
            String[] recipesArr = { recipes };
            if (recipes.contains("_")) {
                recipesArr = recipes.split("_");
            }
            for (int i = 0; i < recipesArr.length; i++) {
                Recipe newRecipe = new Recipe();
                newRecipe.getRecipe().setText(recipesArr[i].split("\\+")[0]);
                newRecipe.setViewButtonAction(this::handleViewButton);
                recipeList.getChildren().add(newRecipe);
                String meal = recipesArr[i].split("\\+")[1];
                displayMealTypeTag(newRecipe, meal);
                updateRecipeIndices();
            }
        }
    }

    // Updates UI to clear ALL recipes in recipeList
    public void clearRecipes() {
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Recipe) {
                ((Recipe) recipeList.getChildren().get(i)).setRecipeIndex(0);
            }
        }
        recipeList.getChildren().clear();
    }

    // Updates visual index of recipes in list
    public void updateRecipeIndices() {
        int index = 1;
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Recipe) {
                ((Recipe) recipeList.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }

    // Check if server is still running or throw alert otherwise
    public void checkServer() {
        boolean checker = ServerChecker.isServerRunning(model.IPHOST, 8100);
        if(checker == false){
            view.showAlert("Error", "Server connection was interrupted");
        }
    }

    // Determines whether autoLogin feature is enabled and skips login if so
    private void handleLogin(String username, String password) {
        if(!model.getIsLoggedIn()) {
            String response = model.performRequest("POST", username, password, null, null, "login");
            if (response.equals("SUCCESS")) {
                model.setIsLoggedIn(true);
                model.setLogInDetails(username, password);
                String recipes = model.performRequest("GET", null, null, null, username, "load-recipe");
                clearRecipes();
                loadRecipes(recipes);
                frameController.getFrame("home");
            } else if (response.equals("INVALID CREDENTIALS") || response.equals("USER NOT FOUND")){
                System.out.println("[ LOGIN RESPONSE ] " + response);
            } else {
                view.showAlert("Error", response);
            }
        }
    }

}