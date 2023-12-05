package app.client;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
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
        
        // FilterFrame Event Listerners
        view.getFilterFrame().setBreakfastButtonAction(this::handleFilterBreakfastButton);
        view.getFilterFrame().setLunchButtonAction(this::handleFilterLunchButton);
        view.getFilterFrame().setDinnerButtonAction(this::handleFilterDinnerButton);

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
        } else {
            System.out.println("[ LOGIN RESPONSE ] " + response);
        }

        // String recipes = model.performRequest("GET", username, null, null, username, "mock-route");
    }

    private void handleCreateAccountButton(ActionEvent event) {
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

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
        displayRecipe(recipeText);

        frameController.getFrame("recipe");
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

        ingredients = model.performRequest("POST", null, null, null, null, "whisper");

        // Create prompt with mealType and ingredients and pass to ChatGPT API, Dall-E API for the picture
        String prompt = "Make me a " + mealType + " recipe using " + ingredients + " presented in JSON format with the \"title\" as the first key with its value as one string, \"ingredients\" as another key with its value as one string, and \"instructions\" as the last key with its value as one string";
        System.out.println("PROMPT +++ " + prompt);
        String response = model.performRequest("POST", null, null, prompt, null, "chatgpt");
        fullRecipe = response;

        recipeParts = response.split("\\+");
        recipeTitle = recipeParts[0];
        response = response.replace("+", "\n");

        String dallePrompt = "Generate a real picture of " + recipeTitle;
        String dalleResponse = model.performRequest("POST", null, null, dallePrompt, null, "dalle");

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

        fullRecipe += "+" + mealType;
        String fullRecipeList = model.performRequest("GET", null, null, null, username, "load-recipe");
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
        mealType = "breakfast";
        ingredients = "croissants";
        String prompt = "Make me a " + mealType + " recipe using " + ingredients + " presented in JSON format with the \"title\" as the first key with its value as one string, \"ingredients\" as another key with its value as one string, and \"instructions\" as the last key with its value as one string";
        String response = model.performRequest("POST", null, null, prompt, null, "chatgpt");
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
        //Make PUT request and save updatedRecipe as second param
        String response = model.performRequest("PUT", username, null, updatedRecipe, null, "");
        System.out.println("[PUT RESPONSE] " + response);
        frameController.getFrame("home");
    }

    private void handleRecipeDeleteButton(ActionEvent event) {
        int delim = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().indexOf("\n");
        String recipeTitle = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().substring(0, delim);
        String response = model.performRequest("DELETE", username, null, null, recipeTitle, "");
        System.out.println("[DELETE RESPONSE] " + response);
        frameController.getFrame("home");
    }

    //===================== FilterFrame Handlers ================================

    public void handleFilterBreakfastButton(ActionEvent event) {
        String response = model.performRequest("GET", username, null, null, "breakfast", "mealtype");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    public void handleFilterLunchButton(ActionEvent event) {
        String response = model.performRequest("GET", username, null, null, "lunch", "mealtype");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
    }

    public void handleFilterDinnerButton(ActionEvent event) {
        String response = model.performRequest("GET", username, null, null, "dinner", "mealtype");
        clearRecipes();
        loadRecipes(response);
        frameController.getFrame("home");
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
            recipeText = recipeText.replace("\\+", "\n");
            view.getRecipeFrame().getRecipeSteps().getRecipeName().setText(recipeName);
            view.getRecipeFrame().getRecipeSteps().getTextArea().setText(recipeText);
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
}
