package app.server;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MockDallE implements HttpHandler{
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
        
        url = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Ff%2Ffa%2FHam_and_eggs_over_easy.jpg%2F1200px-Ham_and_eggs_over_easy.jpg&tbnid=jL-bcwE1AkYVvM&vet=12ahUKEwjm75GvxvSCAxWwJEQIHRB_BbYQMygBegQIARBW..i&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FHam_and_eggs&docid=2WM6ZYnDhyPs5M&w=1200&h=789&q=bacon%20eggs%20and%20ham&ved=2ahUKEwjm75GvxvSCAxWwJEQIHRB_BbYQMygBegQIARBW";
        
        return url;
    }


}
