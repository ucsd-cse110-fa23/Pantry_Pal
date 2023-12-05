package app.server;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;



public class MealTypeFilterHandler implements HttpHandler{
<<<<<<< HEAD
    private String MongoURI = "mongodb+srv://bryancho:73a48JL4@cluster0.jpmyzqg.mongodb.net/?retryWrites=true&w=majority";
    private String peterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
    private String adrianURI = "mongodb+srv://adw004:13531Caravel%26@cluster0.nmzzqtt.mongodb.net/?retryWrites=true&w=majority";
    private String URI = peterURI;
=======
    
    private String URI = MyServer.MONGO_URI;
>>>>>>> origin/main

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
      response = e.toString();
      e.printStackTrace();
    }

  }

    /**
   * 
   *  Finds the recipes with user attached to them
   *  expects: http://localhost:8100/?mealtype=mealtype&user=user
   *  return: title_1 + mealtype_1  + ... + title_n + mealtype_n
   */
  private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "Invalid GET request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();

    if (query != null) {
      // gets the query from the url 
      String value = query.substring(query.indexOf("?") + 1);
      value = URLDecoder.decode(value, "UTF-8");
      Map<String,String> map = QueryParser.parseQuery(value);

      String user = map.get("u");
      String mealtype = map.get("q");
      
      try (MongoClient mongoClient = MongoClients.create(URI)) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("recipes");

        Bson filter = Filters.and(Filters.eq("mealtype", mealtype),Filters.eq("user", user));
        
        FindIterable<Document> recipe = collection.find(filter);
        
        if (collection.countDocuments(filter) == 0) {
          return "";
        }
        if (recipe != null) {
            response = "";
            for(Document a : recipe) {
                response += "_" + a.getString("title") + "+" + a.getString("mealtype");
            }
            // takign out the first + 
          response = response.substring(1);
          System.out.println(response);
        } else {
          response = "";
        }
      }
      
      System.out.println("received get request on server with value " + value);
      System.out.println("response is " + response);
    }

    return response;

  }

}
