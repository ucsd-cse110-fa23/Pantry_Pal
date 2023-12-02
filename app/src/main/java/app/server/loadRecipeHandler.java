package app.server;

import java.io.*;
import java.net.*;
import java.util.*;
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
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;



public class loadRecipeHandler implements HttpHandler{
    private String jackURI = "mongodb+srv://JBarkes:Nade2021!@cluster0.yu3oax5.mongodb.net/?retryWrites=true&w=majority";
    private String MongoURI = "mongodb+srv://bryancho:73a48JL4@cluster0.jpmyzqg.mongodb.net/?retryWrites=true&w=majority";
    private String peterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
    private String URI = jackURI;

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
   *  return: title_1 + ... + title_n ~ N(0,1)
   */
  private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "No Recipes Created Yet";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();

    if (query != null) {
      // Parse username from URL query with key "q"
      String value = query.substring(query.indexOf("?") + 1);
      value = URLDecoder.decode(value, "UTF-8");
      Map<String, String> paramMap = QueryParser.parseQuery(value);
      String username = (String) paramMap.get("q");
      
      try (MongoClient mongoClient = MongoClients.create(URI)) {
        MongoDatabase database = mongoClient.getDatabase("recipesdbasd");
        MongoCollection<Document> collection = database.getCollection(username);

        // Empty RecipeList
        if (collection.countDocuments() == 1) {
          return "";
        }

        // Else, there is at least one recipe so iterate
        FindIterable<Document> recipe = collection.find(new Document());
        if (recipe != null) {
            response = "";
            for(Document a : recipe) {
                if (a.containsKey("username")) {
                  continue;
                }
                response += "+" + a.getString("title");
            }
            // taking out the first + 
          response = response.substring(1);
          System.out.println(response);
        } else {
          System.out.println("No Recipes Saved.");
        }
      }
      System.out.println("received get request on server with value " + value);
      System.out.println("response is " + response);
    }

    return response;

  }

}
