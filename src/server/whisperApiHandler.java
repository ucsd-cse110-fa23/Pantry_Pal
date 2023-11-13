package server;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 *  implementing a whisperAPI route that takes in audio files and translates them into text
 * 
 * 
 *  methods needed: 
 *  post -> give file and then returns a text version of the file.
 *  
 */


 

public class whisperApiHandler implements HttpHandler {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
  private static final String TOKEN = "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";
  private static final String MODEL = "whisper-1";

    public whisperApiHandler(){

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "Request Recieved";
        try {
            if (method.equals("POST")) {
              response = handlePost(httpExchange);
            }
             else {
              throw new Exception("Not Valid Request Method");
            }
          } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
          }
          //Sending back response to the client
          httpExchange.sendResponseHeaders(200, response.length());
          OutputStream outStream = httpExchange.getResponseBody();
          outStream.write(response.getBytes());
          outStream.close();
    }


    // need to feed this method the entire prompt before calling
    private String handlePost(HttpExchange httpExchange) throws IOException, InterruptedException{
      String generatedText = "no respons from whipser";
      
      return generatedText;
    }
}
