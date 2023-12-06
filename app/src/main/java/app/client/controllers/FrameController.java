package app.client.controllers;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FrameController {
    private Map<String, Scene> frameMap;
    private Stage primaryStage;

    public FrameController(Stage primaryStage) {
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