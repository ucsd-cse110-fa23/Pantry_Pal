package app.server;

import java.net.*;

public class ServerChecker {

    // CHECKS IF THE SERVER IS STILL RUNNING OR NOT
    public static boolean isServerRunning(String serverAddress, int port) {
        try (Socket socket = new Socket(serverAddress, port)) {
            return true; 
        } catch (Exception e) {
            return false; 
        }
    }

}
