package app.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/*
 * This class handles the login request.
 * When the user is attempting to login,
 * the request gets sent through here.
 * 
 * returns: status of login
 * uses: POST
 * 
 */


public class LoginHandler implements HttpHandler {

    private String URI = MyServer.MONGO_URI;

    // general method and calls certain methods to handle http request
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("POST")) {
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
    
    /*
     * Goes into the database to check if the user+pass
     * combo is in there
     * 
     * Returns Success if the user+pass combo exists
     * Returns Incorrect or User not found otherwise
     */

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String data = URLDecoder.decode(scanner.nextLine() , "UTF-8");
        String username = data.split("\\&")[0]; // Gets username
        String password = data.split("\\&")[1]; // Gets password
        String response = "Login Request Received";

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase recipeDatabase = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> credentialsCollection = recipeDatabase.getCollection("credentials");

            Document loginData = credentialsCollection.find(new Document("user", username)).first();

            // Found username, looking for password
            if (loginData != null) {
                // Username and password match
                if (loginData.getString("password").equals(password)) {
                    response = "SUCCESS";
                } else {
                    // password doesn't match username
                    response = "INCORRECT CREDENTIALS";
                }
            } else {
                // No username in db
                response = "USER NOT FOUND";
            }

            scanner.close();
        }
        
        return response;
    }

}