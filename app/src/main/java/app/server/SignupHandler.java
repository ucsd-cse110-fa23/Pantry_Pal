package app.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SignupHandler implements HttpHandler {

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
            byte[] bs = response.getBytes("UTF-8");
            httpExchange.sendResponseHeaders(200, bs.length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(bs);
            os.close();
        } catch (Exception e) {
            System.out.println("An erroneous request");
            e.printStackTrace();
        }
    }
    
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String data = URLDecoder.decode(scanner.nextLine() , "UTF-8");
        String username = data.split("\\&")[0]; // Gets username
        String password = data.split("\\&")[1]; // Gets password
        String response = "Signup Request Received";

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase recipeDatabase = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> credentialsCollection = recipeDatabase.getCollection("credentials");

            Document loginData = credentialsCollection.find(new Document("user", username)).first();

            if (loginData != null) { // Check database for username
                response =  "USERNAME TAKEN";
                scanner.close();
                return response;
            }

            loginData = new Document("_id", new ObjectId());
            loginData.append("user", username);
            loginData.append("password", password);
            credentialsCollection.insertOne(loginData);

            response = "NEW USER CREATED";

            scanner.close();
        }
        return response;
    }

}