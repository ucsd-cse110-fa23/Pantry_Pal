package app.client;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button button;
    private String mealType;
    private String ingredients;

    public Controller(View view, Model model, Stage primaryStage) {
        this.view = view;
        this.model = model;
        frameController = new FrameController(primaryStage);

        // AppFrame Event Listeners
        view.getAppFrame().setNewRecipeButtonAction(this::handleNewRecipeButton);
        // view.getAppFrame().getRecipeList().setViewButton(this::handleViewButton);

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
        view.getRecipeFrame().setSaveButtonAction(null);
        view.getRecipeFrame().setDeleteButtonAction(null);
        view.getRecipeFrame().setCancelButtonAction(null);

    }

    public FrameController getFrameController() {
        return frameController;
    }

    //================ AppFrame Event Handler ====================================================

    private void handleNewRecipeButton(ActionEvent event) {
        frameController.getFrame("gpt");
    }

    //================ MealFrame and IngredientsFrame Event Handlers ===============================

    private void handleMealStartButton(ActionEvent event) {
        model.startRecording();

        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getClickedStyle());
        stopButton.setStyle(view.getMealFrame().getDefaultStyle());
        view.getMealFrame().getRecordingLabel().setVisible(true);
    }

    private void handleMealStopButton(ActionEvent event) {
        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getDefaultStyle());
        stopButton.setStyle(view.getMealFrame().getClickedStyle());
        model.stopRecording();

        // String response = model.performRequest("GET", null, "meal", "recording");

        // System.out.println("TRANSCRIPTION: " + mealType);
        mealType = meal();
        System.out.println("MEALTYPE: " + mealType);
        // Change scenes after getting response
        if (mealType.equals("breakfast") || mealType.equals("lunch") || mealType.equals("dinner")) {
            // Update prompt for IngredientsFrame to include meal type then change the frame
            view.getIngredientsFrame().getPrompt().getText().setText("You have selected " + mealType + "\n List your ingredients:");
            frameController.getFrame("ingredients");
        } else {
            // Keep frame if meal type not detected then prompt to try again
            view.getMealFrame().getPrompt().getText().setText("Invalid input. Please select either \n Breakfast, Lunch, or Dinner.");
        }
    }

    private String meal() {
        return model.performRequest("POST", null, null, "whisper");
    }

    private void handleMealCancelButton(ActionEvent event) {
        button = view.getMealFrame().getCancelButton();

        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getDefaultStyle());
        stopButton.setStyle(view.getMealFrame().getDefaultStyle());

        // Redirect back to Home Page
        frameController.getFrame("home");
    }

    //=================== IngredientsFrame Event Handlers ==================================

    private void handleIngredientsStartButton(ActionEvent event) {
    //     model.performRequest("POST", null, "ingredients", "recording");
    //     Button startButton = view.getMealFrame().getStartButton();
    //     Button stopButton = view.getMealFrame().getStopButton();
    //     startButton.setStyle(view.getMealFrame().getDefaultStyle());
    //     stopButton.setStyle(view.getMealFrame().getDefaultStyle());
    }

    private void handleIngredientsStopButton(ActionEvent event) {
    //     String response = model.performRequest("GET", "gpt", null, null);

    //     // Change scenes after getting response
    //     frameController.getFrame(response);
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
        String recipeText = view.getGptFrame().getRecipeText().getText();
        String recipeName = recipeText.split("\n")[0];
        Recipe newRecipe = new Recipe();
        newRecipe.getRecipe().setText(recipeName);
        
        view.getAppFrame().getRecipeList().getChildren().add(newRecipe);
        model.updateRecipeIndices(view.getAppFrame().getRecipeList());
    }

    // takes the same input for mealtype and ingredients,
    // tells ChatGPT to regenerate response with the set of ingredients
    private void handleGptRefreshButton(ActionEvent event) {
        // MOCK INPUTS
        mealType = "breakfast";
        ingredients = "bacon, eggs, sausage";
        //---------------------------
        String prompt = "Make me a " + mealType + " recipe using " + ingredients + " presented in JSON format with the \"title\" as the first key with its value as one string, \"ingredients\" as another key with its value as one string, and \"instructions\" as the last key with its value as one string";
        String response = model.performRequest("POST", prompt, null, "chatgpt");
        System.out.println("CONTROLLER RESPONSE: " + response);
        response = response.replace("+", "\n");
        view.getGptFrame().getRecipeText().setText(response);
    }

    // Cancels the request for ChatGPT, goes back to home screen to restart
    private void handleGptCancelButton(ActionEvent event){
        frameController.getFrame("home");
    }

    //================ RecipeFrame Event Handlers =============================

}
