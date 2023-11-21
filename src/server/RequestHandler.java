package server;


import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 *  This is the handler for the CSV/database 
 * 
 *  NEED: to connect to mongodb so that add and get is easy.
 */

public class RequestHandler implements HttpHandler {
    private final Map<String, String> data;

    public RequestHandler(Map<String, String> data) {
        this.data = data;
    }

    // general method and calls certain methods to handle http request
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
              response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
              response = handlePost(httpExchange);
            } else if (method.equals("PUT")){
              response = handlePut(httpExchange);
            }
            else if (method.equals("DELETE")){
                response = handleDelete(httpExchange);
            }
             else {
              throw new Exception("Not Valid Request Method");
            }
          } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
          }

        //Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }
    
    /**
     * Handles get for each recipe by looking into csv
     * 
     * @param httpExchange
     * @return
     * @throws IOException
     */
    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        query = uri.toASCIIString();
        System.out.println(query);
        if (query != null) {
          // gets the query from the url 
          String value = query.substring(query.indexOf("=") + 1);
          if(value.equals("all")){
            // return the list of all recipe title headers -> load button
          }
          else{
            // get all the recipes into one string and seperate through delim of $
            BufferedReader in = new BufferedReader(new FileReader("recipes.csv"));
            String line = in.readLine();
            String combine = "";
            while (line != null) {
                if (combine.equals("")) {
                    combine = combine + line;
                } else {
                    combine = combine + "\n" + line;
                }
                line = in.readLine();
            }
            String[] recipes = combine.split("\\$");
            
            // find detailed recipe from the title of the query
            for(int i =0; i < recipes.length;i++){
              if(recipes[i].contains(value)){
                response = recipes[i];
                break;
              }
            }
            in.close();
          }
          System.out.println("recieved get request on server with value " + value);
          System.out.println("response is " + response);
        }
        return response;
    }
     
    /**
     * recieve audio file and transcribe it to send back to client/controller.
     * 
     * @param httpExchange
     * @return
     * @throws IOException
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String language = postData.substring(
            0,
            postData.indexOf(",")
        ), year = postData.substring(postData.indexOf(",") + 1);
        
        // Store data in hashmap
        data.put(language, year);
        
        String response = "Posted entry {" + language + ", " + year + "}";
        System.out.println(response);
        scanner.close();
        System.out.println("hit post request from request handler");
        return response;
    }
     
    private String handlePut(HttpExchange httpExchange) throws IOException{
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        System.out.println(postData);
        String language = postData.substring(
            0,
            postData.indexOf(",")
        ), year = postData.substring(postData.indexOf(",") + 1);

        String response = "";

        if(data.containsKey(language)){
            String previousYear = data.get(language);
            response = "Updated entry" + "{" + language + "," + year + "} (previous year:" + previousYear +")";
        }
        else{
            response = "Added entry" + "{" + language + "," + year + "}";
        } 

        data.put(language, year);
        
        System.out.println(response);
        scanner.close();
    
        return response;
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException{
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        System.out.println(query);
        if (query != null) {
          String language = query.substring(query.indexOf("=") + 1);
            if(data.get(language) == null){
                response = "No data for " + language;
            }
            else{
                String year = data.get(language);
                response = "deleted entry" + "{" + language + "," + year + "}";
                data.remove(language);
            }
        }
        
        System.out.println(response);
    
        return response;
    }

}