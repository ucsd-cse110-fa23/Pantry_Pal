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
  private String jackURI = "mongodb+srv://JBarkes:Nade2021!@cluster0.yu3oax5.mongodb.net/?retryWrites=true&w=majority";
  private String peterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
  private String anthonyURI = "mongodb://azakaria:ILWaFDvRjUEUjpcJ@ac-ytzddhr-shard-00-00.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-01.rzzq5s2.mongodb.net:27017,ac-ytzddhr-shard-00-02.rzzq5s2.mongodb.net:27017/?ssl=true&replicaSet=atlas-11uj01-shard-0&authSource=admin&retryWrites=true&w=majority";
  private String URI = peterURI;
  private String databaseName = "recipesdbasd";


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
   * 
   * @return the actual detailed recipe 
   * 
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
      String username = paramMap.get("u");
      value = (String) paramMap.get("q");
      
      try (MongoClient mongoClient = MongoClients.create(URI)) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(username);

        Document recipe = collection.find(new Document("title", value)).first();
        if (recipe != null) {
          response = recipe.getString("title");
          response += "+" + recipe.getString("ingredients");
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
   * EXPECT: USER+TITLE+INGREDIENTS+INSTRUCTIONS
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
      String nl = URLDecoder.decode(scanner.nextLine(), "UTF-8");
      reqBody.append(nl);
    }
  
    // Get the title, ingredients, instructions
    String body = reqBody.toString();
    String username = body.split("\\&")[0];
    body = body.split("\\&")[2];

    System.out.println("REQ BODY: " + body);
    int fDelim = body.indexOf("+");
    int sDelim = body.indexOf("+",fDelim+1);
    // int tDelim = body.indexOf("+",sDelim+1);

    String title = body.substring(0,fDelim);
    String ingredients = body.substring(fDelim+1, sDelim);
    String instructions = body.substring(sDelim+1);
    // String user = body.substring(tDelim + 1);
    
    System.out.println("TITLE: " + title);
    System.out.println("INGRED: " + ingredients);
    System.out.println("INSTRUCT: " + instructions);
    // System.out.println("USER:" + user);
    String response = "valid post";

    try (MongoClient mongoClient = MongoClients.create(URI)) {
      MongoDatabase database = mongoClient.getDatabase(databaseName);
      MongoCollection<Document> collection = database.getCollection(username);
      
      Document recipe = new Document("_id", new ObjectId());
      recipe.append("title", title);
      recipe.append("ingredients", ingredients);
      recipe.append("instructions",instructions);
      // recipe.append("user",user);

      collection.insertOne(recipe);
      response = "valid posts";
    }
  
    System.out.println(response);
    scanner.close();
    return response;
  }
     
  /**
   * EXPECT: USER+TITLE+INGREDIENTS+INSTRUCTIONS
   * NOT DONE: needs to let the name of the recipe be changed so need to find another way to locate the recipe, also need to always call api again after the back since names could have changed
   * 
   * @return
   */
  private String handlePut(HttpExchange httpExchange) throws IOException{
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    StringBuilder reqBody = new StringBuilder();

    while(scanner.hasNext()) {
      String nl = URLDecoder.decode(scanner.nextLine(), "UTF-8");
      reqBody.append(nl);
    }
  
    // Get the title, ingredients, instructions
    String body = reqBody.toString();
    String username = body.split("\\&")[0];
    body = body.split("\\&")[2];

    int fDelim = body.indexOf("+");
    int sDelim = body.indexOf("+",fDelim+1);
    // int tDelim = body.indexOf("+",sDelim+1);

    String title = body.substring(0,fDelim);
    String ingredients = body.substring(fDelim+1, sDelim);
    String instructions = body.substring(sDelim+1);
    // String user = body.substring(tDelim + 1);

    String response = "Not valid put";
    try (MongoClient mongoClient = MongoClients.create(URI)) {
      MongoDatabase database = mongoClient.getDatabase(databaseName);
      MongoCollection<Document> collection = database.getCollection(username);

      Bson filter = eq("title", title);
      // Bson filter2 = eq("user",user);
      // filter = combine(filter,filter2);

      Bson nameUpdate = set("title",title);
      Bson updateOperation = set("ingredients", ingredients);
      Bson up1 = set("instructions", instructions);
      Bson combined = combine(nameUpdate,updateOperation, up1);
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
      String value = query.substring(query.indexOf("?") + 1);
      value = URLDecoder.decode(value, "UTF-8");
      Map<String, String> paramMap = QueryParser.parseQuery(value);
      String username = paramMap.get("u");
      value = (String) paramMap.get("q");

      try (MongoClient mongoClient = MongoClients.create(URI)) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(username);

        collection.findOneAndDelete(new Document("title", value));
        response = "valid delete";
      }
    } 
    
    System.out.println(response);

    return response;
  }

}