package app.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
    
public class App extends Application {
    Scene homeScene, mealScene, ingredScene, gptScene, recipeScene, loginScene;
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Set the title of the a
        primaryStage.setTitle("PantryPal");
        
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(view, model, primaryStage);

        // Create new Scenes for each Page
        loginScene = new Scene(view.getLoginFrame(), 400, 500);
        homeScene = new Scene(view.getHomeFrame(), 400, 500);
        mealScene = new Scene(view.getMealFrame(), 400, 500);
        ingredScene = new Scene(view.getIngredientsFrame(), 400, 500);
        gptScene = new Scene(view.getGptFrame(), 400, 500);
        recipeScene = new Scene(view.getRecipeFrame(), 400, 500);

        // Add each scene to the frameController to switch scenes on button click
        controller.getFrameController().addFrame("login", loginScene);
        controller.getFrameController().addFrame("home", homeScene);
        controller.getFrameController().addFrame("meal", mealScene);
        controller.getFrameController().addFrame("ingredients", ingredScene);
        controller.getFrameController().addFrame("gpt", gptScene);
        controller.getFrameController().addFrame("recipe", recipeScene);
        
        // Create scene of mentioned size/ with the border pane
        primaryStage.setScene(loginScene);

        // Make window non-resizable
        primaryStage.setResizable(true);

        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
