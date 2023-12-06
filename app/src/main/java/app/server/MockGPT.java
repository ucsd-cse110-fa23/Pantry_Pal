package app.server;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/*
 * This class mocks ChatGPT for testing
 * 
 */

public class MockGPT implements HttpHandler {
    String url;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String response = "Request Recieved";
        try {
            if (method.equals("POST")) {
              response = handlePost(httpExchange);
            } else {
              throw new Exception("Not Valid Request Method");
            }

            //Sending back response to the client
            try {
                byte[] bs = response.getBytes("UTF-8");
                httpExchange.sendResponseHeaders(200, bs.length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(bs);
                os.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

    }

    private String handlePost(HttpExchange httpExchange) throws IOException, InterruptedException, URISyntaxException{
        // Set request parameters
        String response = "Bacon egg and ham cheese sandwhich + 1/2 pound bacon 2 eggs 1 slice cheese + 1. Cook the bacon 2. cook the egg";

        return response;
    }
}