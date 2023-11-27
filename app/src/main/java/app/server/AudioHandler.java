package app.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AudioHandler implements HttpHandler {

    final private int RECORD_TYPE = 1;
    private File audioFile;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;

    AudioHandler() {

        audioFile = new File("recording.wav");
        audioFormat = getAudioFormat();

    }

    // general method and calls certain methods to handle http request
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
              response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
              response = handlePost(httpExchange);
            } else {
              throw new Exception("Not Valid Request Method");
            }
            // Sending back response to the client
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {

        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();

        if (query != null) {
            String frame = query.substring(query.indexOf("=") + 1);
            stopRecording();
            if (frame.equals("meal")) {
                response = "ingredients";
            } else if (frame.equals("ingredients")) {
                response = "gpt";
            }
            System.out.println("Stopped Recording.");
        }

        return response;

    }
    
    private String handlePost(HttpExchange httpExchange) throws IOException {

        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        startRecording();
        String response = "Recording...";
        System.out.println(response);
        scanner.close();
        return response;

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

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}
