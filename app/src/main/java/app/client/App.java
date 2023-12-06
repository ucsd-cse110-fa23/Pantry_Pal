package app.client;

import app.client.controllers.*;
import app.client.views.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
    
public class App extends Application {
    Scene homeScene, mealScene, ingredScene, gptScene, recipeScene, loginScene, filterScene, shareScene, sortScene;
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        
        // Compose view and model in controller
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model, primaryStage);

        // Create new Scenes for each Page
        loginScene = new Scene(view.getLoginFrame(), 700, 500);
        homeScene = new Scene(view.getHomeFrame(), 700, 500);
        mealScene = new Scene(view.getMealFrame(), 700, 500);
        ingredScene = new Scene(view.getIngredientsFrame(), 700, 500);
        gptScene = new Scene(view.getGptFrame(), 700, 500);
        recipeScene = new Scene(view.getRecipeFrame(), 700, 500);
        filterScene = new Scene(view.getFilterFrame(), 700, 500);
        shareScene = new Scene(view.getShareFrame(), 700, 500);
        sortScene = new Scene(view.getSortFrame(), 700, 500);

        // Add each scene to the frameController to switch scenes on button click
        controller.getFrameController().addFrame("login", loginScene);
        controller.getFrameController().addFrame("home", homeScene);
        controller.getFrameController().addFrame("meal", mealScene);
        controller.getFrameController().addFrame("ingredients", ingredScene);
        controller.getFrameController().addFrame("gpt", gptScene);
        controller.getFrameController().addFrame("recipe", recipeScene);
        controller.getFrameController().addFrame("share",shareScene);
        controller.getFrameController().addFrame("filter", filterScene);
        controller.getFrameController().addFrame("sort", sortScene);
        
        // Create scene of mentioned size/ with the border pane
        primaryStage.setScene(loginScene);

        // Make window non-resizable
        primaryStage.setResizable(true);

        // Show the app
        primaryStage.show();

        // Check if auto-login succeeded
        if (model.getIsLoggedIn()) {
            controller.getFrameController().getFrame("home");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
