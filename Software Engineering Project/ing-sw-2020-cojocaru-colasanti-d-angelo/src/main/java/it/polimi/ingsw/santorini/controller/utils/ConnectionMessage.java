package it.polimi.ingsw.santorini.controller.utils;

/**
 * Brief Utils class containing some textual message for the server-client coordination
 */
public class ConnectionMessage {
    public static final String socketCloseConnectionMessage = "Connection with client closed";
    public static final String successfulConnectionMessage = "Connected to client!";
    public static final String serverWaitingMessage = "Waiting for client connection...";
    public static final String clientDisconnectionMessage = "Disconnected player: ";
    public static final String serverCreationErrorMessage = "Issues with server creation. Closing application...";
}
