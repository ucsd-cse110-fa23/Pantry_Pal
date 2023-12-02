package app.client;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

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
    private String username, password, mealType, ingredients;
    private String fullRecipe;
    private RecipeList recipeList;

    public Controller(View view, Model model, Stage primaryStage) {
        this.view = view;
        this.model = model;
        frameController = new FrameController(primaryStage);
        recipeList = view.getHomeFrame().getRecipeList();

        // LoginFrame Event Listeners
        view.getSortFrame().setAlphaButtonAction(this::handleSortAlphaButton);
        view.getSortFrame().setRAlphaButtonAction(this::handleSortRAlphaButton);
        view.getSortFrame().setChronoButtonAction(this::handleSortChronoButton);
        view.getSortFrame().setRChronoButtonAction(this::handleSortRChronoButton);

        // LoginFrame Event Listeners
        view.getLoginFrame().setLoginButtonAction(this::handleLoginButton);
        view.getLoginFrame().setCreateAccountButtonAction(this::handleCreateAccountButton);

        // HomeFrame Event Listeners
        view.getHomeFrame().setNewRecipeButtonAction(this::handleNewRecipeButton);
        view.getHomeFrame().setSortButtonAction(this::handleSortButton);

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
        
    }

    public FrameController getFrameController() {
        return frameController;
    }

    //================ SortFrame Event Handlers ====================================================

    private void handleSortAlphaButton(ActionEvent event) {
        sortAlphabetically();

        // Redirect back to Home Page
        frameController.getFrame("home");;
    }

    private void handleSortRAlphaButton(ActionEvent event) {
        sortAlphabetically();
        reverse();

        // Redirect back to Home Page
        frameController.getFrame("home");;
    }

    private void handleSortChronoButton(ActionEvent event) {
        clearRecipes();
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", username, null, null, username, "loadRecipeHandler");
            loadRecipes(recipes);
            frameController.getFrame("home");
            System.out.println("|||Frame changed|||");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }

        // Redirect back to Home Page
        frameController.getFrame("home");;
    }

    private void handleSortRChronoButton(ActionEvent event) {
        clearRecipes();
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", username, null, null, username, "loadRecipeHandler");
            loadRecipes(recipes);
            frameController.getFrame("home");
            System.out.println("|||Frame changed|||");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }
        reverse();

        // Redirect back to Home Page
        frameController.getFrame("home");;
    }

    //================ LoginFrame Event Handlers ====================================================

    private void handleLoginButton(ActionEvent event) {
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        String response = model.performRequest("POST", username, password, null, null, "login");
        if (response.equals("SUCCESS")) {
            String recipes = model.performRequest("GET", username, null, null, username, "loadRecipeHandler");
            loadRecipes(recipes);
            frameController.getFrame("home");
            System.out.println("|||Frame changed|||");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }
    }

    private void handleCreateAccountButton(ActionEvent event) {
        username = view.getLoginFrame().getLoginContent().getUsername().getText();
        password = view.getLoginFrame().getLoginContent().getPassword().getText();

        String response = model.performRequest("POST", username, password, null, null, "signup");
        if (response.equals("SUCCESS")) {
            frameController.getFrame("home");
        } else {
            System.out.println("[LOGIN RESPONSE] " + response);
        }
    }

    //================ AppFrame Event Handlers ====================================================

    private void handleNewRecipeButton(ActionEvent event) {
        frameController.getFrame("meal");
    }

    private void handleSortButton(ActionEvent event) {
        frameController.getFrame("sort");
    }

    private void handleViewButton(ActionEvent event) {
        Button target = (Button) event.getTarget();
        Recipe recipe = (Recipe) target.getParent();
        String recipeTitle = recipe.getRecipe().getText();
        String recipeText = model.performRequest("GET", username, null, null, recipeTitle, "");
        displayRecipe(recipeText);

        frameController.getFrame("recipe");
    }

    //================ MealFrame and IngredientsFrame Event Handlers ===============================

    private void handleMealStartButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getClickedStyle());
        stopButton.setStyle(view.getMealFrame().getDefaultStyle());
        view.getMealFrame().getRecordingLabel().setVisible(true);

        model.startRecording();
    }

    private void handleMealStopButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getDefaultStyle());
        stopButton.setStyle(view.getMealFrame().getClickedStyle());

        model.stopRecording();

        mealType = model.performRequest("POST", null, null, null, null, "whisper");
        mealType = model.mealType(mealType);
        System.out.println("MEALTYPE CONTROLLER: " + mealType);

        if (mealType.equals("")) {
            view.getMealFrame().getPrompt().getText().setText("Invalid input. Please select either \n Breakfast, Lunch, or Dinner.");
        } else if (mealType.equals("breakfast") || mealType.equals("lunch") || mealType.equals("dinner")) {
            // Update prompt for IngredientsFrame to include meal type then change the frame
            view.getIngredientsFrame().getPrompt().getText().setText("You have selected " + mealType + "\n List your ingredients:");
            frameController.getFrame("ingredients");

            // Reset prompt and button styles
            view.getMealFrame().getPrompt().getText().setText("What meal type would you like: \n Breakfast, Lunch, or Dinner?");
            startButton.setStyle(view.getMealFrame().getDefaultStyle());
            stopButton.setStyle(view.getMealFrame().getDefaultStyle());
        }

    }

    private void handleMealCancelButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getDefaultStyle());
        stopButton.setStyle(view.getMealFrame().getDefaultStyle());

        model.stopRecording();

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    //=================== IngredientsFrame Event Handlers ==================================

    private void handleIngredientsStartButton(ActionEvent event) {
        Button startButton = view.getIngredientsFrame().getStartButton();
        Button stopButton = view.getIngredientsFrame().getStopButton();
        startButton.setStyle(view.getIngredientsFrame().getClickedStyle());
        stopButton.setStyle(view.getIngredientsFrame().getDefaultStyle());

        model.startRecording();
    }

    private void handleIngredientsStopButton(ActionEvent event) {
        Button startButton = view.getIngredientsFrame().getStartButton();
        Button stopButton = view.getIngredientsFrame().getStopButton();
        startButton.setStyle(view.getIngredientsFrame().getDefaultStyle());
        stopButton.setStyle(view.getIngredientsFrame().getClickedStyle());

        model.stopRecording();

        ingredients = model.performRequest("POST", null, null, null, null, "whisper");

        // Create prompt with mealType and ingredients and pass to ChatGPT API
        String prompt = "Make me a " + mealType + " recipe using " + ingredients + " presented in JSON format with the \"title\" as the first key with its value as one string, \"ingredients\" as another key with its value as one string, and \"instructions\" as the last key with its value as one string";
        System.out.println("PROMPT +++ " + prompt);
        String response = model.performRequest("POST", username, password, prompt, null, "chatgpt");
        fullRecipe = response;
        response = response.replace("+", "\n");
        view.getGptFrame().getRecipeText().setText(response);

        // Change scenes after getting response
        frameController.getFrame("gpt");

        startButton.setStyle(view.getIngredientsFrame().getDefaultStyle());
        stopButton.setStyle(view.getIngredientsFrame().getDefaultStyle());
    }

    private void handleIngredientsCancelButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getDefaultStyle());
        stopButton.setStyle(view.getMealFrame().getDefaultStyle());

        // Redirect back to Home Page
        frameController.getFrame("home");
    }
    
    //=============== GptFrame Event Handlers =============================

    private void handleGptSaveButton(ActionEvent event) {
        String recipeName = view.getGptFrame().getRecipeText().getText().split("\n")[0];
        Recipe newRecipe = new Recipe();
        newRecipe.getRecipe().setText(recipeName);
        newRecipe.setViewButtonAction(this::handleViewButton);
        
        recipeList.getChildren().add(0,newRecipe);
        updateRecipeIndices();
        
        model.performRequest("POST", username, password, fullRecipe, null, "");

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    // takes the same input for mealtype and ingredients,
    // tells ChatGPT to regenerate response with the set of ingredients
    private void handleGptRefreshButton(ActionEvent event) {
        String prompt = "Make me a " + mealType + " recipe using " + ingredients + " presented in JSON format with the \"title\" as the first key with its value as one string, \"ingredients\" as another key with its value as one string, and \"instructions\" as the last key with its value as one string";
        String response = model.performRequest("POST", username, password, prompt, null, "chatgpt");
        fullRecipe = response;
        displayRecipe(response);
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
        String response = model.performRequest("PUT", username, password, updatedRecipe, null, "");
        System.out.println("[PUT RESPONSE] " + response);
    }

    private void handleRecipeDeleteButton(ActionEvent event) {
        int delim = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().indexOf("\n");
        String recipeTitle = view.getRecipeFrame().getRecipeSteps().getTextArea().getText().substring(0, delim);
        String response = model.performRequest("DELETE", username, null, null, recipeTitle, "");
        updateRecipeIndices();
        System.out.println("[DELETE RESPONSE] " + response);
    }

    //================== HELPER METHODS ========================================
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

    public void loadRecipes(String recipes) {
        if (recipes != null) {
            String[] recipesArr = { recipes};
            if (recipes.contains("+")) {
                recipesArr = recipes.split("+");
            }
            for (int i = 0; i < recipesArr.length; i++) {
                Recipe newRecipe = new Recipe();
                newRecipe.getRecipe().setText(recipesArr[i]);
                newRecipe.setViewButtonAction(this::handleViewButton);
                recipeList.getChildren().add(0,newRecipe);
                updateRecipeIndices();
            }
        }
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

    public void clearRecipes() {
        int index = 0;
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(index) instanceof Recipe) {
                recipeList.getChildren().remove(index);
            } else {
                index++;
            }
        }
    }

    public void sortAlphabetically() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Recipe) {
                recipes.add(((Recipe) recipeList.getChildren().get(i)));
            }
        }
        clearRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            titles.add(recipes.get(i).getRecipe().toString());
        }
        Collections.sort(titles);
        for (int i = 0; i < titles.size(); i++) {
            for (int j = 0; j < recipes.size(); j++) {
                String re = recipes.get(j).getRecipe().toString();
                if (titles.get(i).equals(re)) {
                    Recipe newRecipe = new Recipe();
                    newRecipe.getRecipe().setText(re);
                    newRecipe.setViewButtonAction(this::handleViewButton);
                    recipeList.getChildren().add(0,newRecipe);
                    updateRecipeIndices();
                }
            }
        }
        updateRecipeIndices();
    }

    public void reverse() {
        ArrayList<String> recipes = new ArrayList<>();
        for (int i = 0; i < recipeList.getChildren().size(); i++) {
            if (recipeList.getChildren().get(i) instanceof Recipe) {
                recipes.add(((Recipe) recipeList.getChildren().get(i)).getRecipe().toString());
            }
        }
        clearRecipes();
        for(int i = recipes.size() - 1; i > -1; i--) {
            Recipe newRecipe = new Recipe();
            newRecipe.getRecipe().setText(recipes.get(i));
            newRecipe.setViewButtonAction(this::handleViewButton);
            recipeList.getChildren().add(0,newRecipe);
            updateRecipeIndices();
            // recipes.get(i).setViewButtonAction(this::handleViewButton);
            // recipeList.getChildren().add(0, r);
            // recipeList.getChildren().add(0, recipes.get(i));
            
        }
        updateRecipeIndices();
    }
}
