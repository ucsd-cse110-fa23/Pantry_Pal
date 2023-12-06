package app.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MockWhisper implements HttpHandler {
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

        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String data = URLDecoder.decode(scanner.nextLine() , "UTF-8");
        String mealType = data.split("\\&")[0]; // Gets mealType
        String ingredients = data.split("\\&")[1]; // Gets ingredients

        scanner.close();
        
        return "Make me a " + mealType + " recipe with " + ingredients;
    }
}