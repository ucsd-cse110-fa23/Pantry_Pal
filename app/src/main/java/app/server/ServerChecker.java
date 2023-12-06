package app.server;

import java.net.*;

/*
 * This class is a helper class that checks if
 * the server is still running before any model
 * requests are made.
 * 
 * Returns: True if the server is running
 * Returns: False if the server is not running
 */

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
