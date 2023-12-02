package app.server;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

/**
 *  This is the handler for the CSV/database 
 * 
 *  NEED: to connect to mongodb so that add and get is easy.
 *  FORMAT OF all strings must be title+ingredients+instructions
 */

public class RequestHandler implements HttpHandler {
  private String MongoURI = "mongodb+srv://bryancho:73a48JL4@cluster0.jpmyzqg.mongodb.net/?retryWrites=true&w=majority";
  private String peterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";

  // general method and calls certain methods to handle http request
  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
      if (method.equals("GET")) {
        response = handleGet(httpExchange);
      } else if (method.equals("POST")) {
        response = handlePost(httpExchange);
      } else if (method.equals("PUT")) {
        response = handlePut(httpExchange);
      } else if (method.equals("DELETE")) {
        response = handleDelete(httpExchange);
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
   * Handles get for each recipe by looking into database
   * 
   * @param httpExchange
   * @return
   * @throws IOException
   */
  private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "Invalid GET request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();

    if (query != null) {
      // gets the query from the url 
      String value = query.substring(query.indexOf("=") + 1);
      System.out.println("GET VALUE: " + value);
      value = URLDecoder.decode(value, "UTF-8");
      System.out.println("DECODED VALUE: " + value);
      
      try (MongoClient mongoClient = MongoClients.create(peterURI)) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("recipes");

        Document recipe = collection.find(new Document("title", value)).first();
        if (recipe != null) {
          response = recipe.getString("ingredients");
          response += "+" + recipe.getString("instructions");
          System.out.println(response);
        } else {
          System.out.println("null find");
        }
      }
      System.out.println("received get request on server with value " + value);
      System.out.println("response is " + response);
    }

    return response;

  }
     
  /**
   *  getting the entire string of the recipe and need to parse for title, set title field and then set text
   *  
   * There is an issue with the \n appearing in the text.. ask at office hours
   *  using this as the input
   * \n\nLemon Dill Salmon\n\nIngredients:\n\n- 4 (4–6 ounce) pieces fresh or frozen salmon\n- 1/4 cup olive oil\n- 2 tablespoons freshly squeezed lemon juice\n- 2 cloves garlic, minced\n- 2 teaspoons dried dill\n- Kosher salt and freshly ground black pepper\n\nDirections:\n\n1. Preheat oven to 350°F (175°C).\n\n2. Place the salmon on a foil-lined baking sheet. \n\n3. In a small bowl, whisk together the olive oil, lemon juice, garlic, and dill until ingredients are well combined. \n\n4. Drizzle the olive oil mixture over the salmon and season generously with salt and pepper.\n\n5. Bake for 8-10 minutes, or until the salmon is cooked through and flaky. \n\n6. Serve with your favorite sides. Enjoy!
   * Scrambled Eggs+2 eggs, salt, pepper, oil (olive oil or butter)+Put a small amount of oil or butter in a non-stick pan over medium-high heat. Crack two eggs into the pan, season lightly with salt and pepper, and scramble until the eggs are fully cooked.
   *  
   * @param httpExchange
   * @return
   * @throws IOException
   */
  private String handlePost(HttpExchange httpExchange) throws IOException {
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    StringBuilder reqBody = new StringBuilder();

    while(scanner.hasNext()) {
      String nl = scanner.nextLine();
      reqBody.append(nl);
    }
  
    // get the title, ingredients, instructions
    String body = reqBody.toString();
    System.out.println("REQ BODY: " + body);
    int fDelim = body.indexOf("+");
    String title = body.substring(0,fDelim);
    System.out.println("TITLE: " + title);
    int sDelim = body.indexOf("+",fDelim+1);
    String ingredients = body.substring(fDelim+1, sDelim);
    System.out.println("INGRED: " + ingredients);
    String instructions = body.substring(sDelim+1);
    System.out.println("INSTRUCT: " + instructions);

    String response = "valid post";

    try (MongoClient mongoClient = MongoClients.create(peterURI)) {
      MongoDatabase database = mongoClient.getDatabase("PantryPal");
      MongoCollection<Document> collection = database.getCollection("recipes");
      
      Document recipe = new Document("_id", new ObjectId());
      recipe.append("title", title);
      recipe.append("ingredients", ingredients);
      recipe.append("instructions",instructions);

      collection.insertOne(recipe);
      response = "valid posts";
    }
  
    System.out.println(response);
    scanner.close();
    System.out.println("title: " + title);
    System.out.println("ingredients: " + ingredients);
    System.out.println("instructions: " + instructions);
    return response;
  }
     
  /**
   *  Give title+new_ingredients+new_instructions
   * @param httpExchange
   * @return
   * @throws IOException
   */
  private String handlePut(HttpExchange httpExchange) throws IOException{
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    StringBuilder reqBody = new StringBuilder();

    while(scanner.hasNext()) {
      String nl = scanner.nextLine();
      reqBody.append(nl);
    }
  
    // get the title, ingredients, instructions
    String body = reqBody.toString();
    int fDelim = body.indexOf("+");
    String title = body.substring(0,fDelim);
    int sDelim = body.indexOf("+",fDelim+1);
    String ingredients = body.substring(fDelim+1, sDelim);
    String instructions = body.substring(sDelim+1);

    String response = "Not valid put";
    try (MongoClient mongoClient = MongoClients.create(MongoURI)) {
      MongoDatabase database = mongoClient.getDatabase("PantryPal");
      MongoCollection<Document> collection = database.getCollection("recipes");
      
      Bson filter = eq("title", title);
      Bson updateOperation = set("ingredients", ingredients);
      Bson up1 = set("instructions", instructions);
      Bson combined = combine(updateOperation, up1);
      collection.findOneAndUpdate(filter, combined);

      response = "valid put";
    }
    
    System.out.println(response);
    scanner.close();

    return response;
  }

  private String handleDelete(HttpExchange httpExchange) throws IOException{
    String response = "Invalid delete request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    System.out.println(query);

    if (query != null) {
      String value = query.substring(query.indexOf("=") + 1);
      value = URLDecoder.decode(value, "UTF-8");

      try (MongoClient mongoClient = MongoClients.create(MongoURI)) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("recipes");

        collection.findOneAndDelete(new Document("title", value));
        response = "valid delete";
      }
    } 
    
    System.out.println(response);

    return response;
  }

}