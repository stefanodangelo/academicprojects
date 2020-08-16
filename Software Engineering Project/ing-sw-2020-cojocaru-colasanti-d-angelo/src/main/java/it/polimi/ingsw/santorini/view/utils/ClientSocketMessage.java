package it.polimi.ingsw.santorini.view.utils;

/**
 * Brief Helper class containing all the textual messages needed for the comprehension of the interaction between client and server
 */
public class ClientSocketMessage {
    public static final String serverShutdownMessage = "Server is not responding, closing connection...";
    public static final String serverAnswerMessage = "[SERVER] ";
    public static final String successfulConnectionMessage = "Connected to server!";
    public static final String successfulClosingMessage = "Connection closed";
    public static final String queuedMessage = "Queued to the waiting connections...";
    public static final String fullServerMessage = "Server is full";
}
