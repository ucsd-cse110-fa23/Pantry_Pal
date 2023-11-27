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
        this.view.getAppFrame().setNewRecipeButtonAction(this::handleNewRecipeButton);
        // this.view.getAppFrame().getRecipeList().setViewButton(this::handleViewButton);

        // MealFrame Event Listeners
        this.view.getMealFrame().setStartButtonAction(this::handleMealStartButton);
        this.view.getMealFrame().setStopButtonAction(this::handleMealStopButton);
        this.view.getMealFrame().setCancelButtonAction(this::handleMealCancelButton);

        // IngredientsFrame Event Listeners
        // this.view.getIngredientsFrame().setStartButtonAction(this::handleIngredientsStartButton);
        // this.view.getIngredientsFrame().setStopButtonAction(this::handleIngredientsStopButton);
        // this.view.getIngredientsFrame().setCancelButtonAction(this::handleIngredientsCancelButton);

        // GptFrame Event Listeners
        // this.view.getGptFrame().setSaveButtonAction(this::handleGptSaveButton);
        // this.view.getGptFrame().setCancelButtonAction(this::handleGptCancelButton);

        // this.view.getRecipeFrame().set
    }

    public FrameController getFrameController() {
        return frameController;
    }

    //================ AppFrame Event Handler ====================================================

    private void handleNewRecipeButton(ActionEvent event) {
        frameController.getFrame("meal");
    }

    //================ MealFrame and IngredientsFrame Event Handlers ===============================

    private void handleMealStartButton(ActionEvent event) {
        model.performRequest("POST", null, null, "recording");

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

        String response = model.performRequest("GET", null, "meal", "recording");
        mealType = model.performRequest("POST", null, null, "whisper");

        // System.out.println("TRANSCRIPTION: " + mealType);

        // Change scenes after getting response
        if (mealType != null) {
            // Update prompt for IngredientsFrame to include meal type then change the frame
            view.getIngredientsFrame().getPrompt().getText().setText("You have selected " + mealType + "\n List your ingredients:");
            frameController.getFrame(response);
        } else {
            // Keep frame if meal type not detected then prompt to try again
            view.getMealFrame().getPrompt().getText().setText("Invalid input. Please select either \n Breakfast, Lunch, or Dinner.");
        }
    }

    private void handleMealCancelButton(ActionEvent event) {
        button = view.getMealFrame().getCancelButton();

        Button startButton = view.getMealFrame().getStartButton();
        Button stopButton = view.getMealFrame().getStopButton();
        startButton.setStyle(view.getMealFrame().getDefaultStyle());
        stopButton.setStyle(view.getMealFrame().getDefaultStyle());

        // Redirect back to Main Page
        frameController.getFrame("home");
    }

    //=================== IngredientsFrame Event Listen ==================================

    // private void handleIngredientsStartButton(ActionEvent event) {
    //     model.performRequest("POST", null, "ingredients", "recording");
    //     Button startButton = view.getMealFrame().getStartButton();
    //     Button stopButton = view.getMealFrame().getStopButton();
    //     startButton.setStyle(view.getMealFrame().getDefaultStyle());
    //     stopButton.setStyle(view.getMealFrame().getDefaultStyle());
    // }

    // private void handleIngredientsStopButton(ActionEvent event) {
    //     String response = model.performRequest("GET", "gpt", null, null);

    //     // Change scenes after getting response
    //     frameController.getFrame(response);
    // }

    // private void handleIngredientsCancelButton(ActionEvent event) {
    //     button = view.getIngredientsFrame().getCancelButton();

    //     Button startButton = view.getMealFrame().getStartButton();
    //     Button stopButton = view.getMealFrame().getStopButton();
    //     startButton.setStyle(view.getMealFrame().getDefaultStyle());
    //     stopButton.setStyle(view.getMealFrame().getDefaultStyle());

    //     // Redirect back to Main Page
    //     frameController.getFrame("home");
    // }
    
    
    //=============== GptFrame Event Handlers =============================

    //================ RecipeFrame Event Handlers =============================

}
