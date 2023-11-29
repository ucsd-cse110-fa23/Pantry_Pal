package app.client;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import java.net.URL;

public class Model {
    final private int RECORD_TYPE = 1;
    private File audioFile;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;

    public Model() {
        audioFile = new File("recording.wav");
        audioFormat = getAudioFormat();
    }

    /**
     * 
     * @param method 
     * @param query true = parameters in http, false = no parameters
     * @param function csv, chatgpt, whisper
     * @return
     */
    public String performRequest(String method, String data, String query, String route) {

        // Implement HTTP request logic here and return the response
        try {
            String urlString = "http://localhost:8100/" + route;
            if (query != null) {
                urlString += "?=" + query;
            }

            if (route.equals("whisper")) {
                sendPOSTWhisper();
            }

            // Establish HTTP connection
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            // Write any data arguments to OS if they are passed in
            if (method.equals("POST") || method.equals("PUT")) {
                if (data != null) {    
                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    out.write(data);
                    out.flush();
                    out.close();
                }
            }

            // Read OS once handlers have written response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();

            if (route.equals("whisper")) {
                response = mealType(response);
            }
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }

    }

    // Given transcribed Whisper string, filter for meal type
    private String mealType(String res) {
        String mealType = res.toLowerCase();

        if (mealType.contains("breakfast")) { return "breakfast"; }
        else if (mealType.contains("lunch")) { return "lunch"; }
        else if (mealType.contains("dinner")) { return "dinner"; }
        else { return null; }
    }

    // Client-side whisper file transfer
    public static void sendPOSTWhisper() throws IOException {
        final String POST_URL = "http://localhost:8100/whisper";
        final File uploadFile = new File("recording.wav");

        String boundary = Long.toHexString(System.currentTimeMillis()); 
        String CRLF = "\r\n";
        String charset = "UTF-8";
        URLConnection connection = new URL(POST_URL).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        
        try (
            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + uploadFile.getName() + "\"").append(CRLF);
            writer.append("Content-Length: " + uploadFile.length()).append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(uploadFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(uploadFile.toPath(), output);
            output.flush();
            writer.append("--" + boundary + "--").append(CRLF).flush(); // End of multipart/form-data.

            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            System.out.println("Response code: [" + responseCode + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void startRecording() {
        Thread t = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {   
                        // the format of the TargetDataLine
                        DataLine.Info dataLineInfo = new DataLine.Info(
                                TargetDataLine.class,
                                audioFormat);
                        // the TargetDataLine used to capture audio data from the microphone
                        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                        targetDataLine.open(audioFormat);
                        targetDataLine.start();

                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        AudioSystem.write(
                                audioInputStream,
                                AudioFileFormat.Type.WAVE,
                                audioFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        );
        t.start();
    }

    public void stopRecording() {notify();      
        targetDataLine.stop();
        targetDataLine.close();
    }

    public void saveRecipe(String recipeText) {
        // String recipeName = recipeText.split("\n")[0];
        // recipeList.getChildren().add(newRecipe);
        // recipeList.updateRecipeIndices();
    }

    public void updateRecipeIndices(RecipeList rl) {
        int index = 1;
        for (int i = 0; i < rl.getChildren().size(); i++) {
            if (rl.getChildren().get(i) instanceof Recipe) {
                ((Recipe) rl.getChildren().get(i)).setRecipeIndex(index);
                index++;
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        // sendPOSTWhisper();
    }
}