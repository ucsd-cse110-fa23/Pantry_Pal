import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import java.io.*;
import java.net.URISyntaxException;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.*;

import javax.sound.sampled.*;

class MealFrame extends FlowPane {
    private Button startButton;
    private Button stopButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private Header header;
    private Footer footer;
    private Label prompt;
    Stage primaryStage;
    Scene homeScene;
    RecipeList recipeList;

    final private int RECORD_TYPE = 1;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    MealFrame(Stage primaryStage, Scene homeScene, RecipeList recipeList) {
        // header = new Header();
        // footer = new Footer();
        // this.setTop(header);
        // this.setBottom(footer);
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        prompt = new Label();
        prompt.setText("What meal type would you like: Breakfast, Lunch, or Dinner?");
        this.getChildren().add(prompt);
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(170);

        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
        this.recipeList = recipeList;

        // Add the buttons and text fields
        startButton = new Button("Record Meal Type");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(startButton, stopButton, recordingLabel);

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            stopRecording();
            // try {
                // mealType.transcribeMeal();
                MealType.mealString = "Breakfast";
                if (MealType.mealString != null) {
                    IngredientsFrame ingredients = new IngredientsFrame(primaryStage, homeScene, recipeList);
                    Scene recordIngredients = new Scene(ingredients, 500, 600);
                    switchScene(this.primaryStage, recordIngredients);
                }
                
            // } catch (IOException e1) {
            //     e1.printStackTrace();
            // } catch (URISyntaxException e1) {
            //     e1.printStackTrace();
            // }
        });
    }

    public void switchScene(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
    } 

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        // CHANGE TO 1 IF U ON MAC AND 2 ON WINDOWS <-----------------------------------
        int channels = RECORD_TYPE;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        Thread t = new Thread(
            new Runnable() {
                @Override
                public void run(){
                    try {   
                        // the format of the TargetDataLine
                        DataLine.Info dataLineInfo = new DataLine.Info(
                                TargetDataLine.class,
                                audioFormat);
                        // the TargetDataLine used to capture audio data from the microphone
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                        targetDataLine.open(audioFormat);
                        targetDataLine.start();
                        recordingLabel.setVisible(true);

                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        File audioFile = new File("mealType.wav");
                        AudioSystem.write(
                                audioInputStream,
                                AudioFileFormat.Type.WAVE,
                                audioFile);
                        recordingLabel.setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        );
        t.start();
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}

class IngredientsFrame extends FlowPane {
    private final int RECORD_TYPE = 1;

    private Button startButton;
    private Button stopButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    Ingredients ingredients;
    public Stage primaryStage;
    public Scene homeScene;
    public RecipeList recipeList;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    IngredientsFrame(Stage primaryStage, Scene homeScene, RecipeList recipeList) {
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setVgap(10);
        this.setHgap(10);
        this.setPrefWrapLength(170);

        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
        this.recipeList = recipeList;

        // Add the buttons and text fields
        startButton = new Button("Record Ingredients");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(startButton, stopButton, recordingLabel);

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            stopRecording();
            // try {
                // Ingredients.transcribeIngredients();
                Ingredients.ingredientsString = "eggs, sausage, onions";
                if (Ingredients.ingredientsString != null) {
                    Scene gptScene = new Scene(new MockGPT(primaryStage, homeScene, recipeList), 500, 600);
                    switchScene(this.primaryStage, gptScene);
                }
            // } catch (IOException e1) {
            //     e1.printStackTrace();
            // } catch (URISyntaxException e1) {
            //     e1.printStackTrace();
            // }
        });
    }

    public void switchScene(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
    } 

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = RECORD_TYPE;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    private void startRecording() {
        Thread t = new Thread(
            new Runnable() {
                @Override
                public void run(){
                    try {   
                        // the format of the TargetDataLine
                        DataLine.Info dataLineInfo = new DataLine.Info(
                                TargetDataLine.class,
                                audioFormat);
                        // the TargetDataLine used to capture audio data from the microphone
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                        targetDataLine.open(audioFormat);
                        targetDataLine.start();
                        recordingLabel.setVisible(true);

                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        File audioFile = new File("ingredients.wav");
                        AudioSystem.write(
                                audioInputStream,
                                AudioFileFormat.Type.WAVE,
                                audioFile);
                        recordingLabel.setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        );
        t.start();

    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}

public class Recording extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window (Flow Pane)
        // MealFrame root = new MealFrame(primaryStage, primaryStage.getScene(), recipeList);

        // // Set the title of the app
        // primaryStage.setTitle("Record Meal Type");
        // // Create scene of mentioned size with the border pane
        // primaryStage.setScene(new Scene(root, 500, 600));
        // // Make window non-resizable
        // primaryStage.setResizable(false);
        // // Show the app
        // primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
