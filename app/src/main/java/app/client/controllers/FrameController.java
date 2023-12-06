package app.client.controllers;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FrameController
 * 
 * Used to change between various scenes of the app
 */
public class FrameController {
    private Map<String, Scene> frameMap;
    private Stage primaryStage;

    /**
     * Constructor use's primaryStage from App.java to set and switch scenes
     * 
     * @param primaryStage
     */
    public FrameController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        frameMap = new HashMap<>();
    }

    // Add scenes to map structure
    public void addFrame(String name, Scene scene) {
        frameMap.put(name, scene);
    }

    // Gets scene corresponding to its set name then changes current scene
    public void getFrame(String name) {
        primaryStage.setScene(frameMap.get(name));
    }
}