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
import com.mongodb.client.model.Filters;

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
  private String adrianURI = "mongodb+srv://adw004:13531Caravel%26@cluster0.nmzzqtt.mongodb.net/?retryWrites=true&w=majority";
  private String URI = MyServer.MONGO_URI;


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
      // httpExchange.sendResponseHeaders(200, response.length());
      // OutputStream outStream = httpExchange.getResponseBody();
      // outStream.write(response.getBytes());
      
      byte[] bs = response.getBytes("UTF-8");
      httpExchange.sendResponseHeaders(200, bs.length);
      OutputStream os = httpExchange.getResponseBody();
      os.write(bs);
      os.close();
      

    } catch (Exception e) {
      System.out.println("An erroneous request");
      response = e.toString();
      e.printStackTrace();
    }

  }
    
  /**
   * need to change to get for the correct user
   * 
   * @return title+ingredients+instructions+mealtype
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
          response = recipe.getString("title");
          response += "+" + recipe.getString("ingredients");
          response += "+" + recipe.getString("instructions");
          //response += "+" + recipe.getString("mealtype");
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
   * EXPECT: USER+TITLE+INGREDIENTS+INSTRUCTIONS+MEALTYPE
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
  
    // get the title, ingredients, instructions
    String body = reqBody.toString();
    System.out.println("REQ BODY: " + body);
    int fDelim = body.indexOf("+");
    int sDelim = body.indexOf("+",fDelim+1);
    int tDelim = body.indexOf("+",sDelim+1);
    int delim4 = body.indexOf("+", tDelim+1);

    String user = body.substring(0,fDelim);
    String title = body.substring(fDelim+1, sDelim);
    String ingredients = body.substring(sDelim+1,tDelim);
    String instructions = body.substring(tDelim+1,delim4);
    String mealtype = body.substring(delim4+1);
    
    System.out.println("TITLE: " + title);
    System.out.println("INGRED: " + ingredients);
    System.out.println("INSTRUCT: " + instructions);
    System.out.println("USER:" + user);
    System.out.println("MEALTYPE: " + mealtype);
    String response = "invalid post";

    try (MongoClient mongoClient = MongoClients.create(URI)) {
      MongoDatabase database = mongoClient.getDatabase("PantryPal");
      MongoCollection<Document> collection = database.getCollection("recipes");
      
      Document recipe = new Document("_id", new ObjectId());
      recipe.append("title", title);
      recipe.append("ingredients", ingredients);
      recipe.append("instructions", instructions);
      recipe.append("user", user);
      recipe.append("mealtype", mealtype);

      collection.insertOne(recipe);
      response = "valid posts";
    }
  
    System.out.println(response);
    scanner.close();
    return response;
  }
     
  /**
   * EXPECT: USER+TITLE+INGREDIENTS+INSTRUCTIONS
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
  
    // get the title, ingredients, instructions
    String body = reqBody.toString();
    System.out.println("REQ BODY: " + body);
    int fDelim = body.indexOf("+");
    int sDelim = body.indexOf("+",fDelim+1);
    int tDelim = body.indexOf("+",sDelim+1);

    String user = body.substring(0,fDelim);
    String title = body.substring(fDelim+1, sDelim);
    String ingredients = body.substring(sDelim+1,tDelim);
    String instructions = body.substring(tDelim + 1);


    System.out.println("TITLE: " + title);
    System.out.println("INGRED: " + ingredients);
    System.out.println("INSTRUCT: " + instructions);
    System.out.println("USER:" + user);


    String response = "Not valid put";
    try (MongoClient mongoClient = MongoClients.create(URI)) {
      MongoDatabase database = mongoClient.getDatabase("PantryPal");
      MongoCollection<Document> collection = database.getCollection("recipes");

      Bson filter = eq("title", title);
      Bson filter2 = eq("user",user);
      filter = combine(filter,filter2);

      Bson up1 = set("ingredients", ingredients);
      Bson up2 = set("instructions", instructions);
      Bson combined = combine(up1, up2);
      collection.findOneAndUpdate(filter, combined);

      response = "valid put";
    }
    
    System.out.println(response);
    scanner.close();

    return response;
  }

  /**
   * Exepects: query paramater in URL, need title and user delimitted by a +, but mgiht need to change + to a -
   *  need to get the correct tuser
   */

  
  private String handleDelete(HttpExchange httpExchange) throws IOException{
    String response = "Invalid delete request";
    URI uri = httpExchange.getRequestURI();
    String query = uri.getRawQuery();
    
    System.out.println(query);
    System.out.println();

    if (query != null) {
      String value = query;
      value = URLDecoder.decode(value, "UTF-8");

      System.out.println("decoded" + value);
      Map<String,String> map  = QueryParser.parseQuery(value);
      for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " | Value: " + entry.getValue());
      }

      // assuming that query is the title 
      String title = map.get("q");
      String user = map.get("u");

      System.out.println("title: " + title + " User: " + user);

      try (MongoClient mongoClient = MongoClients.create(URI)) {
        MongoDatabase database = mongoClient.getDatabase("PantryPal");
        MongoCollection<Document> collection = database.getCollection("recipes");
        
        Bson filter = Filters.and(Filters.eq("title",title),Filters.eq("user", user));

        collection.findOneAndDelete(filter);
        response = "valid delete";
      }
    } 
    
    System.out.println(response);

    return response;
  }

}