package app.controller;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

import app.view.*;
import app.client.*;

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