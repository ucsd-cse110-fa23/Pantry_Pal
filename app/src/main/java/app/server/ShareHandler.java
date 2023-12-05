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



public class ShareHandler implements HttpHandler{
    private String MongoURI = "mongodb+srv://bryancho:73a48JL4@cluster0.jpmyzqg.mongodb.net/?retryWrites=true&w=majority";
    private String peterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
    private String URI = MyServer.MONGODBURI;

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
   *  u is the user
   *  q is the title of the recipe
   *  expects: http://localhost:8100/share/?u=BryanTest&q=Egg%20and%20Tomato%20Frittata
   *  return: the html for the recipe
   */
  private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "Invalid GET request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();

    if (query != null) {
      // gets the query from the url 

      String value = query.substring(query.indexOf("?") + 1);
      value = URLDecoder.decode(value, "UTF-8");
      Map<String, String> paramMap = QueryParser.parseQuery(value);
      String user = paramMap.get("u");
      value = (String) paramMap.get("q");

      System.out.println("USER: " + user);
      System.out.println("TITLE: " + value);
      
      try (MongoClient mongoClient = MongoClients.create(URI)) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("recipes");
        Bson filter = Filters.and(Filters.eq("title",value),Filters.eq("user", user));
        Document recipe = collection.find(filter).first();
        if (recipe != null) {
          StringBuilder htmlBuilder = new StringBuilder();
          htmlBuilder
            .append("<html>")
            .append("<body>")
            .append("<h1>")
            .append(recipe.getString("title"))
            .append("</h1>")
            .append("<h2>")
            .append(recipe.getString("mealtype"))
            .append("</h2>")
            .append("<h2>")
            .append(recipe.getString("ingredients"))
            .append("</h2>")
            .append("<p>")
            .append(recipe.getString("instructions"))
            .append("</p>")
            .append("</body>")
            .append("</html>");

            response = htmlBuilder.toString();

          System.out.println(response);
        } else {
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder
              .append("<html>")
              .append("<body>")
              .append("<h1>")
              .append("The recipe you have selected cannont be found by the server.")
              .append(" Please try again!")
              .append("</h1>")
              .append("</body>")
              .append("</html>");
      
            // encode HTML content
            response = htmlBuilder.toString();
        }
      }
      System.out.println("received get request on server with value " + value);
      System.out.println("response is " + response);
    }

    return response;

  }

}
