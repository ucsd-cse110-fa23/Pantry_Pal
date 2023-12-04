package app.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class LoginHandler implements HttpHandler {

    private String anthonyURI = "mongodb://azakaria:ILWaFDvRjUEUjpcJ@ac-ytzddhr-shard-00-00.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-01.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-02.rzzq5s2.mongodb.net:27017/?ssl=true&replicaSet=atlas-11uj01-shard-0&authSource=admin&retryWrites=true&w=majority";
    private String peterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
    private String URI = peterURI;

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
    
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String data = URLDecoder.decode(scanner.nextLine() , "UTF-8");
        String username = data.split("\\&")[0]; // Gets username
        String password = data.split("\\&")[1]; // Gets password
        String response = "Login Request Received";

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase recipeDatabase = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipeCollection = recipeDatabase.getCollection("recipes");

            Document loginData = recipeCollection.find(new Document("password", password)).first();
            System.out.println(username + "\n" + password);

            if (loginData != null && loginData.getString("password").equals(password)) { // Check database for username
                if (username.equals(loginData.getString("user"))) { // If the password for the username matches in the DB
                    response = "SUCCESS"; // Returns DB info...
                } else {
                    response = "USER NOT FOUND"; // Incorrect user/password.
                }
            } else {
                response = "INCORRECT CREDENTIALS"; // Non-existent name.
            }

            scanner.close();
        }
        
        return response;
    }

}