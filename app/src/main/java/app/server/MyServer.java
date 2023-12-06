package app.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;


/*
 * This class starts our server on the localhost,
 * must be ran before running our gradle app
 * 
 */

public class MyServer {

    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    // private static final String SERVER_HOSTNAME = "100.81.33.231";
    private static final String SERVER_HOSTNAME = "localhost";

    private static String MongoURI = "mongodb+srv://bryancho:73a48JL4@cluster0.jpmyzqg.mongodb.net/?retryWrites=true&w=majority";
    private static String PeterURI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
    public static String MONGO_URI = MongoURI;
    
    private static HttpServer server;

    public static void main(String[] args) throws IOException {

        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        server = HttpServer.create(
            new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
            0
        );

        // endpoints
        server.createContext("/", new RequestHandler());
        server.createContext("/whisper", new WhisperHandler());
        server.createContext("/chatgpt", new ChatGPTHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/signup", new SignupHandler());
        server.createContext("/load-recipe", new loadRecipeHandler());
        server.createContext("/dalle", new DallEHandler());
        server.createContext("/mockDalle", new MockDallE());
        server.createContext("/mealtype", new MealTypeFilterHandler());
        server.createContext("/share", new ShareHandler());
        server.createContext("/mockGPT", new MockGPT());
        server.createContext("/mockwhisper", new MockWhisper());
        server.createContext("/picture", new pictureHandler());

        server.setExecutor(threadPoolExecutor);
        server.start();
        
        System.out.println("Server started on port " + SERVER_PORT);
    }

    public static void stop() {
        server.stop(0);
    }

}
