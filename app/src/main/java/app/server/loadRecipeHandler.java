package app.server;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class loadRecipeHandler implements HttpHandler{
    
    private String URI = MyServer.MONGO_URI;

    // general method and calls certain methods to handle http request
    public void handle(HttpExchange httpExchange) throws IOException {
      String response = "Request Received";
      String method = httpExchange.getRequestMethod();
      try {
        if (method.equals("GET")) {
          response = handleGet(httpExchange);
        } else {
          throw new Exception("Not Valid Request Method");
        }

        //Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();

      } catch (Exception e) {
        System.out.println("An erroneous request");
        e.printStackTrace();
      }

    }

    /**
   * 
   *  Finds the recipes with user attached to them
   *  expects http://localhost:8100/?mealtype=mealtype&user=user
   *  return: title_1 + mealtype_1 + ... + title_n + mealtype_n 
   */
  private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "Invalid Load Recipe GET Request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();

    if (query != null) {
      // gets the query from the url 
      String value = query.substring(query.indexOf("?") + 1);
      value = URLDecoder.decode(value, "UTF-8");
      Map<String, String> paramMap = QueryParser.parseQuery(value);
      String user = paramMap.get("q");
      System.out.println("USER " + user);

      try (MongoClient mongoClient = MongoClients.create(URI)) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("recipes");

        long recipeCount = collection.countDocuments(eq("user", user));

        // Only the login credentials for user were found in the collection so no recipes
        if (recipeCount == 0) {
          System.out.println("NO RECIPES SAVED");
          return "";
        }

        FindIterable<Document> recipe = collection.find(new Document("user", user));

        if (recipe != null) {
            response = "";
            for(Document a : recipe) {
                response += "+" + a.getString("title") + "+" + a.getString("mealtype");
            }
            // Taking out the first + 
            response = response.substring(1);
            System.out.println(response);
        } else {
          System.out.println("NO RECIPES SAVED");
          return "";
        }
      }
      System.out.println("received get request on server with value " + user);
      System.out.println("response is " + response);
    }
    
    return response;

  }

}
