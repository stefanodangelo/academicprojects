package it.polimi.ingsw.santorini.controller.server;

import it.polimi.ingsw.santorini.communication.*;
import it.polimi.ingsw.santorini.controller.exceptions.NameAlreadyTakenException;
import it.polimi.ingsw.santorini.controller.utils.ConnectionMessage;
import it.polimi.ingsw.santorini.controller.utils.LobbyCommand;
import it.polimi.ingsw.santorini.communication.NetworkMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Brief Server's socket communicating with a single client
 */
public class ClientConnectionSocket implements Runnable {
    /**
     * Brief Inner class responsible for the client notification when something in the server changes
     */
    private class Notifier implements Observer<Object> {
        private Integer playerId;
        /**
         * Brief When called asynchronously sends a message to a specific client
         * @param message is the object to send
         */
        @Override
        public synchronized void update(Object message) {
            Thread t = new Thread(() -> {
                try {
                    outputStream.reset();
                    outputStream.writeObject(message);
                    outputStream.flush();
                } catch (SocketException e) {
                    closeConnection(false);
                } catch (IOException ignored) {
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
            if(message instanceof KickMessage)
                closeConnection(true);
        }

        /**
         * Brief Updates the observer's id assigning it a new value
         * @param newId is the new value for the observer's id
         */
        @Override
        public void updateId(Integer newId) {
            playerId = newId;
            update(new GameMessage(MethodHeading.ID.ordinal(), false, Collections.singletonList(newId)));
        }
    }
    private final ServerNetworkHandler server;
    private final Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Notifier notifier;

    /**
     * Brief Creates the socket for the communication with client, then registers itself in the observers' list
     * @param server is the server's instance
     * @param clientSocket is the socket that will interact with client
     */
    public ClientConnectionSocket(ServerNetworkHandler server, Socket clientSocket){
        this.server = server;
        this.clientSocket = clientSocket;
        notifier = this.new Notifier();

        //observer registration
        server.registerObserver(notifier);
        notifier.playerId = server.getObserversNumber();
    }

    /**
     * Brief Closes all the active communicating channels with the client, then disconnects it
     * @param onKickedPlayer is true if the disconnection takes place during the kick of a player
     */
    private void closeConnection(boolean onKickedPlayer){
        try {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
            server.disconnect(notifier, notifier.playerId, onKickedPlayer);
        } catch (IOException ignored) {
        }
    }

    /**
     * Brief Checks if a player can log in the lobby by analyzing the username he chose
     * The convention followed for the ids is that they are progressive numbers starting from 1
     * @param playerName is the name of the logging-in player
     * @throws NameAlreadyTakenException if there's another player in the lobby with the same name
     */
    private synchronized void logIn(String playerName) throws NameAlreadyTakenException{
        if(server.getActiveNames().contains(playerName))
            throw new NameAlreadyTakenException();
        server.addName(notifier.playerId, playerName);
        server.sendCommandsList(false);
        server.sendRoles();
    }

    /**
     * Brief Handles the registration of a player by trying to log him in the server and notifying him if his username has already been chosen
     * @param playerName is the username of the player who's trying to log in the lobby
     */
    private void handlePlayerUsername(String playerName) {
        try {
            logIn(playerName);
        } catch (NameAlreadyTakenException e){
            notifier.update(new TextMessage(e.getMessage()));
            notifier.update(new GameMessage(MethodHeading.USERNAME.ordinal(), false, Collections.singletonList(notifier.playerId)));
        }
    }

    /**
     * Brief Checks if the client requested to change the lobby's size, that is if the method completes its execution without throwing any exception
     * @param request is the textual format of client's request
     */
    private void checkSizeChangeRequest(String request) {
        Integer size = Integer.parseInt(request);
        if (server.setLobbyDimension(size)) {
            server.sendCommandsList(true);
            notifier.update(new LobbyMessage(NetworkMessage.sizeChanged.ordinal()));
        }
        else
            notifier.update(new LobbyMessage(NetworkMessage.sizeNotChanged.ordinal()));
    }

    /**
     * Brief Checks if the client requested to start the game and in that case asks the server if it's possible to start a new game, consequently forwarding the outcome to the client
     * @param request is the textual format of client's request
     */
    private void checkStartRequest(String request) {
        if (request.equals(TextMessage.getStartRequest())) {
            if (server.allowStart()) {
                server.notifyObservers(new LobbyMessage(NetworkMessage.startConfirmation.ordinal()));
                server.setGameStarted(true);
            } else
                notifier.update(new LobbyMessage(NetworkMessage.startRefusal.ordinal()));
        }
        else
            notifier.update(new LobbyMessage(NetworkMessage.badInput.ordinal()));
    }

    /**
     * Brief Analyzes a message received from the client connected to this socket, sending it back an answer
     * @param message is the message sent by the client
     */
    private void analyzeClientMessage(Object message){
        if(message instanceof TextMessage){
            handleMessage((TextMessage) message);
        }
        else if (message instanceof GameMessage) {
            handleMessage((GameMessage) message);
        }
        else notifier.update(new LobbyMessage(NetworkMessage.waitTurn.ordinal()));
    }

    /**
     * Brief Handles a GameMessage verifying if it contains the player's chosen username. If not, the method forwards the message to the MessageReceiver
     * @param message is the message to analyze
     */
    private void handleMessage(GameMessage message) {
        if(!server.isGameStarted())
            handlePlayerUsername((String) message.getData().get(0));
        else
            server.receive(message);
    }

    /**
     * Brief Handles a TextMessage verifying if it contains a request for the change of the lobby's size
     * @param message is the message to analyze
     */
    private void handleMessage(TextMessage message) {
        if (!server.isGameStarted()) {
            if (notifier.playerId.equals(ServerNetworkHandler.getHostId())) {
                String request = message.getContent();
                try{
                    checkSizeChangeRequest(request);
                } catch (NumberFormatException e){
                    checkStartRequest(request);
                }
            }
            else
                notifier.update(new LobbyMessage(NetworkMessage.waitStart.ordinal()));
        }
        else
            notifier.update(new LobbyMessage(NetworkMessage.waitTurn.ordinal()));
    }

    /**
     * Brief Creates the streams to communicate with the client or closes the connection if something goes wrong
     */
    private void createStreams() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            closeConnection(false);
        }
    }

    /**
     * Brief Reads whatever the client sends, shutting down the connection if the client disconnects both with an explicit request and without sending anything
     */
    private void read() {
        try {
            while (true) {
                Object message = inputStream.readObject();
                if(message instanceof QuitMessage)
                    break;
                else
                    analyzeClientMessage(message);
            }
        } catch(IOException e) {
            System.out.println(ConnectionMessage.socketCloseConnectionMessage);
        } catch (ClassNotFoundException ignored){
        } finally {
            if(!clientSocket.isClosed())
                closeConnection(false);
        }
    }

    /**
     * Brief Establishes a communication with the client, then interacts with it
     */
    @Override
    public void run() {
        createStreams();
        //player log-in
        notifier.update(new GameMessage(MethodHeading.USERNAME.ordinal(), false, Collections.singletonList(notifier.playerId)));

        read();
    }
}