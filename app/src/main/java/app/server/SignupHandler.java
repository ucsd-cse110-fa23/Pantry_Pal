package app.server;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SignupHandler implements HttpHandler {

    private String anthonyURI = "mongodb://azakaria:ILWaFDvRjUEUjpcJ@ac-ytzddhr-shard-00-00.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-01.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-02.rzzq5s2.mongodb.net:27017/?ssl=true&replicaSet=atlas-11uj01-shard-0&authSource=admin&retryWrites=true&w=majority";
    private String peterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
    private String uri = peterURI;

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
            e.printStackTrace();
        }
    }
    
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String data = URLDecoder.decode(scanner.nextLine(), "UTF-8"); 
        String[] credentials = data.split("\\&");
        String username = credentials[0]; // Gets username
        String password = credentials[1]; // Gets password
        String response = "Sign Up Request Received";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase recipeDatabase = mongoClient.getDatabase("recipesdbasd");
            MongoCollection<Document> recipeCollection = recipeDatabase.getCollection(username);

            Document loginData = recipeCollection.find(new Document("username", username)).first();
            System.out.println(username + "\n" + password);
            // Check database for existing username
            if (loginData != null && loginData.getString("username").equals(username)) { 
                response =  "NAME TAKEN";
                scanner.close();
                return response;
            }
            loginData = new Document("_id", new ObjectId());
            loginData.append("username", username);
            loginData.append("password", password);
            recipeCollection.insertOne(loginData);
            response = "SUCCESS";

            scanner.close();
        }
        return response;
    }

}