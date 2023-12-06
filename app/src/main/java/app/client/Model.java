package app.client;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Arrays;
import java.io.*;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import java.net.URLEncoder;
import java.net.URL;

/**
 * Model.java
 * 
 * Backend for the code that talks to server and passes responses to controller
 */
public class Model {
    final private int RECORD_TYPE = 1;
    private File audioFile;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    String errorMessage;
    boolean isLoggedInBool = false;
    String savedName,savedPass = "";
    public static String IPHOST = "100.81.33.231";

    /**
     * Initialize file to record audio
     */
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
    public String performRequest(String method, String username, String password, String data, String query, String route) {

        // Implement HTTP request logic here and return the response
        try {
            String urlString = "http://" + IPHOST + ":8100/" + route;

            if (username != null && query != null) {
                query = URLEncoder.encode("u=" + username + "&q=" + query, "UTF-8");
                urlString += "?" + query;
            } else if (query != null) {
                query = URLEncoder.encode("q=" + query, "UTF-8");
                urlString += "?" + query;
            }

            // Establish HTTP connection
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (route.equals("whisper")) {
                sendPOSTWhisper(conn);
            } else {
                conn.setRequestMethod(method);
                conn.setDoOutput(true);
            }

            // Write any data arguments to OS if they are passed in
            if (method.equals("POST") || method.equals("PUT")) {
                if (username != null) {  
                    String reqBody = username;
                    if (password != null) {
                        reqBody += "&" + password;
                    }
                    if (data != null) {
                        reqBody += "+" + data;
                    }

                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    out.write(URLEncoder.encode(reqBody, "UTF-8"));
                    out.flush();
                    out.close();
                } else if (data != null) {
                    String reqBody = data;
                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    out.write(URLEncoder.encode(reqBody, "UTF-8"));
                    out.flush();
                    out.close();
                }
                
            }

            // Read OS once handlers have written response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            System.out.println("[ MODEL RESPONSE ]: " + response);

            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }

    }

    // Given transcribed Whisper string, filter for meal type
    public String transcribeMealType(String res) {
        String mealType = res.toLowerCase();

        if (mealType.contains("breakfast")) { return "breakfast"; }
        else if (mealType.contains("lunch")) { return "lunch"; }
        else if (mealType.contains("dinner")) { return "dinner"; }
        else { return ""; }
    }

    // Client-side whisper file transfer
    public static void sendPOSTWhisper(HttpURLConnection connection) throws IOException {
        final File uploadFile = new File("recording.wav");

        String boundary = Long.toHexString(System.currentTimeMillis()); 
        String CRLF = "\r\n";
        String charset = "UTF-8";

        connection.setRequestMethod("POST");
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

    public void stopRecording() {  
        // if targetDataLine == null, recording never started so no need to stop
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }

    public String sortAlphabetically(String recipes) {
        if (recipes != null) {
            String[] recipesArr = {recipes};
            if (recipes.contains("_")) {
                recipesArr = recipes.split("_");
                Arrays.sort(recipesArr);
            }

            String fin = "";
            for (int i = 0; i < recipesArr.length; i++) {
                fin = fin + "_" + recipesArr[i];
            }
            fin = fin.substring(1);
            return fin;
        }
        return null;
    }

    public String sortRAlphabetically(String recipes) {
        if (recipes != null) {
            String[] reverseRecipesArr = { recipes };
            if (recipes.contains("_")) {
                reverseRecipesArr = recipes.split("_");
                Arrays.sort(reverseRecipesArr);
            }

            String fin = "";
            for (int i = reverseRecipesArr.length-1; i >= 0; i--) {
                fin += "_" + reverseRecipesArr[i];
            }
            fin = fin.substring(1);
            return fin;
        }
        return null;
    }

    public String sortChronological(String recipes) {
        if (recipes != null) {
            String[] recipesArr = {recipes};
            if (recipes.contains("_")) {
                recipesArr = recipes.split("_");
            }

            String fin = "";
            for(int i = 0; i < recipesArr.length; i++){
                fin = fin + "_" + recipesArr[i];
            }
            fin = fin.substring(1);
            return fin;
        }

        return null;
    }

    public String sortRChronological(String recipes) {
        if (recipes != null) {
            String[] recipesArr = {recipes};
            if (recipes.contains("_")) {
                recipesArr = recipes.split("_");
            }

            String fin = "";
            for(int i = recipesArr.length - 1; i >= 0; i--){
                fin = fin + "_" + recipesArr[i];
            }
            fin = fin.substring(1);
            return fin;
        }

        return null;
    }

    public boolean getAutoLoginStatus() throws IOException {
        try (
            BufferedReader output = new BufferedReader(new FileReader("preferences.csv"));
        ) {
            boolean returnValue;
            String option = output.readLine();
            if(option.equals("true")) {
                returnValue = true;
            } else {
                returnValue = false;
            }
            output.close();
            return returnValue;
        }
    }

    public boolean getIsLoggedIn() {
        return isLoggedInBool;
    }

    public String getLogInDetails() {
        if(savedPass.equals("")) {
            return "";
        }
        return "\n"+savedName+"\n"+savedPass;
    }

    public void setLogInDetails(String name, String pass) {
        savedName = name;
        savedPass = pass;
    }

    public void setIsLoggedIn(boolean val) {
        isLoggedInBool = val;
    }

    public String getAutoLoginDetails() throws IOException {
        try (
            BufferedReader output = new BufferedReader(new FileReader("preferences.csv"));
        ) {
            String option = output.readLine();
            if(option.equals("false")) {
                output.close();
                return "";
            }
            String savedName = output.readLine();
            String savedPassword = output.readLine();
            if(savedPassword == null) {
                output.close();
                return "";
            }
            output.close();
            return savedName + "\n" + savedPassword;
        }
    }

    public void setAutoLoginDetails(String username, String password) throws IOException {
        try (
            BufferedReader output = new BufferedReader(new FileReader("preferences.csv"));
        ) {
            String option = output.readLine();
            output.close();
            BufferedWriter input = new BufferedWriter(new FileWriter("preferences.csv"));
            input.write(option+"\n"+username+"\n"+password);
            input.close();
        }
    }

    public void setAutoLoginStatus(boolean value) throws IOException {
        try (
            BufferedReader output = new BufferedReader(new FileReader("preferences.csv"));
        ) {
            String option = output.readLine();
            String savedName = output.readLine();
            String savedPassword = output.readLine();
            output.close();
            BufferedWriter input = new BufferedWriter(new FileWriter("preferences.csv"));
            if(value == true) {
                option = "true";
                if(savedPassword != null) {
                    input.write(option+"\n"+savedName+"\n"+savedPassword);
                } else {
                    input.write(option+getLogInDetails());
                }
            } else {
                option = "false";
                input.write(option);
            }
            input.close();
        }
    }
    
    public static void main(String[] args) throws IOException {
        // sendPOSTWhisper();
    }
}