package app.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class MyServer {

    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "LOCALHOST";
    public static String MONGO_URI = "mongodb+srv://PeterNguyen4:Pn11222003-@cluster0.webebwr.mongodb.net/?retryWrites=true&w=majority";
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
        server.createContext("/mockgpt", new MockGPT());

        server.setExecutor(threadPoolExecutor);
        server.start();
        
        System.out.println("Server started on port " + SERVER_PORT);
    }

    public static void stop() {
        server.stop(0);
    }

}
