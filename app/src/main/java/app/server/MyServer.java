package app.server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class MyServer {

    // initialize server port and hostname
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

    public static void main(String[] args) throws IOException {

        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        HttpServer server = HttpServer.create(
            new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
            0
        );

        // endpoints
        server.createContext("/",  new RequestHandler());
        server.createContext("/recording", new AudioHandler());
        server.createContext("/whisper", new WhisperHandler());
        server.createContext("/chatgpt", new ChatGPTHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/signup", new SignupHandler());

        server.setExecutor(threadPoolExecutor);
        server.start();
          
        System.out.println("Server started on port " + SERVER_PORT);
    }

}
