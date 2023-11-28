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


import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SignupHandler implements HttpHandler {

    final private int RECORD_TYPE = 1;
    private File audioFile;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private MongoClient mongoClient;
    private MongoDatabase recipeDatabase;
    private String uri = "mongodb://azakaria:ILWaFDvRjUEUjpcJ@ac-ytzddhr-shard-00-00.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-01.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-02.rzzq5s2.mongodb.net:27017/?ssl=true&replicaSet=atlas-11uj01-shard-0&authSource=admin&retryWrites=true&w=majority";

    SignupHandler() {
        // Move the creation of resources inside the constructor
        try {
            mongoClient = MongoClients.create(uri);
            recipeDatabase = mongoClient.getDatabase("recipesdbasd");
        } catch(Exception err) {
            System.out.println("MongoDB failed");
        }
    }

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
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();

    }
    
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String username = scanner.nextLine(); // Gets username
        String password = scanner.nextLine(); // Gets password
        MongoCollection<Document> recipeCollection = recipeDatabase.getCollection(username);
        Document loginData = recipeCollection.find(new Document("username", username)).first();
        System.out.println(username + "\n" + password);
        if (loginData != null && loginData.getString("username").equals(username)) { // Check database for username
            return "NAME TAKEN";
        }
        loginData = new Document("_id", new ObjectId());
        loginData.append("username", username);
        loginData.append("password", password);
        recipeCollection.insertOne(loginData);
        return "SUCCESS";
    }

}
