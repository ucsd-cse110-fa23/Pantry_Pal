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

class Prompt extends VBox{
    private Label text;
    private Button startButton;
    private Button stopButton;
    private HBox buttonContainer = new HBox(5);
    private Label recordingLabel;
    
    // Set a default style for buttons and fields - background color, font size, italics
    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";

    // Set a default style for buttons and fields - background color, font size, italics
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    Prompt(String text) {
        this.setSpacing(50);
        this.text = new Label();
        this.text.setText(text);
        this.text.setStyle("-fx-font: 13 arial;");

        // Add the buttons and text fields
        startButton = new Button("Start");
        startButton.setStyle(defaultButtonStyle);

        stopButton = new Button("Stop");
        stopButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);
        
        HBox.setMargin(startButton, new Insets(5));
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(startButton, stopButton);
        this.getChildren().addAll(this.text, buttonContainer);
        this.setAlignment(Pos.CENTER);
    }

    public Button getStartButton() {
        return this.startButton;
    }

    public Button getStopButton() {
        return this.stopButton;
    }

    public Label getRecordingLabel() {
        return this.recordingLabel;
    }

    public Label getText() {
        return this.text;
    }
}

class RecordingFooter extends HBox{
    
    private Button cancelButton;

    RecordingFooter() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 12 monaco;";

        cancelButton = new Button("Cancel"); // text displayed on add button
        cancelButton.setStyle(defaultButtonStyle); // styling the button
        
        this.getChildren().add(cancelButton); // adding button to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}
class MealFrame extends BorderPane {
    private Button startButton;
    private Button stopButton;
    private Button cancelButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private Header header;
    private RecordingFooter footer;
    private Prompt prompt;
    Stage primaryStage;
    Scene homeScene;
    RecipeList recipeList;

    final private int RECORD_TYPE = 1;

    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    String clickedButtonStyle = "-fx-background-color: #0174BE; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";

    MealFrame(Stage primaryStage, Scene homeScene, RecipeList recipeList) {
        header = new Header();
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        prompt = new Prompt("What meal type would you like: \n Breakfast, Lunch, or Dinner?");
        
        this.setTop(header);
        this.setCenter(prompt);
        this.setBottom(footer);

        startButton = prompt.getStartButton();
        stopButton = prompt.getStopButton();
        recordingLabel = prompt.getRecordingLabel();
        cancelButton = footer.getCancelButton();

        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
        this.recipeList = recipeList;

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            stopButton.setStyle(defaultButtonStyle);
            startButton.setStyle(clickedButtonStyle);
            recordingLabel.setVisible(true);
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            startButton.setStyle(defaultButtonStyle);
            stopButton.setStyle(clickedButtonStyle);
            stopRecording();
            try {
                File mealTypeAudio = new File("mealType.wav");
                MealType.transcribeMeal(mealTypeAudio);
                if (MealType.mealString != null) {
                    IngredientsFrame ingredients = new IngredientsFrame(primaryStage, homeScene, recipeList);
                    Scene recordIngredients = new Scene(ingredients, 400, 500);
                    this.primaryStage.setScene(recordIngredients);
                } else {
                    prompt.getText().setText("Invalid meal type. Please choose only \n Breakfast, Lunch, or Dinner.");
                    this.setCenter(prompt);
                }
                
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        cancelButton.setOnAction(e -> {
            primaryStage.setScene(homeScene);
        });
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
                        prompt.getRecordingLabel().setVisible(false);
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

class IngredientsFrame extends BorderPane {
    private final int RECORD_TYPE = 1;

    private Button startButton;
    private Button stopButton;
    private Button cancelButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private Header header;
    private RecordingFooter footer;
    private Prompt prompt;

    public Stage primaryStage;
    public Scene homeScene;
    public RecipeList recipeList;

    // Set a default style for buttons and fields - background color, font size, italics
    String defaultButtonStyle = "-fx-background-color: #39A7FF; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";
    String clickedButtonStyle = "-fx-background-color: #0174BE; -fx-font: 13 monaco; -fx-text-fill: #FFFFFF; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-border-radius: 10px";

    IngredientsFrame(Stage primaryStage, Scene homeScene, RecipeList recipeList) {
        header = new Header();
        footer = new RecordingFooter();
        
        // Set properties for the page
        this.setPrefSize(370, 120);
        prompt = new Prompt("You have selected " + MealType.mealString + "\n List your ingredients:");
        
        this.setTop(header);
        this.setCenter(prompt);
        this.setBottom(footer);

        startButton = prompt.getStartButton();
        stopButton = prompt.getStopButton();
        recordingLabel = prompt.getRecordingLabel();
        cancelButton = footer.getCancelButton();

        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
        this.recipeList = recipeList;

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void addListeners() {
        // Start Button
        startButton.setOnAction(e -> {
            stopButton.setStyle(defaultButtonStyle);
            startButton.setStyle(clickedButtonStyle);
            startRecording();
        });

        // Stop Button
        stopButton.setOnAction(e -> {
            startButton.setStyle(defaultButtonStyle);
            stopButton.setStyle(clickedButtonStyle);
            stopRecording();
            try {
                Ingredients.transcribeIngredients();
                if (Ingredients.ingredientsString != null) {
                    Scene gptScene;
                    try {
                        gptScene = new Scene(new ChatGPT(MealType.mealString, Ingredients.ingredientsString, 100, primaryStage, homeScene, recipeList), 400, 500);
                        this.primaryStage.setScene(gptScene);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        cancelButton.setOnAction(e -> {
            primaryStage.setScene(homeScene);
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
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
