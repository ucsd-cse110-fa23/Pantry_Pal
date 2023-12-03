package app.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MockRouteHandler implements HttpHandler {
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
   * @return the actual detailed recipe 
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
      value = paramMap.get("q");
      
      // Join username and query params with comma
      response = username + "," + value;

      System.out.println("Username requested: " + username);
      System.out.println("Value requested: " + value);
      System.out.println("response is " + response);
    }

    return response;

  }
}
