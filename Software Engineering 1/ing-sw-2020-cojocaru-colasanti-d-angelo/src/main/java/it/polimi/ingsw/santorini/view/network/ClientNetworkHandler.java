package it.polimi.ingsw.santorini.view.network;

import it.polimi.ingsw.santorini.communication.*;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.view.ViewType;
import it.polimi.ingsw.santorini.view.cli.BoardPrinter;
import it.polimi.ingsw.santorini.view.cli.CLIPrinter;
import it.polimi.ingsw.santorini.view.delegates.ViewDelegate;
import it.polimi.ingsw.santorini.view.exceptions.EmptyBufferException;
import it.polimi.ingsw.santorini.view.utils.ClientSocketMessage;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Brief Handler of the asynchronous interaction between client and server
 */
public class ClientNetworkHandler implements Runnable, ViewDelegate {
    /**
     * Brief Inner class implementing a buffer containing all the message received from the server
     */
    public static class MessageReceiver{
        protected static final List<Object> messageBuffer = new ArrayList<>();

        /**
         * Brief Adds the message currently received to the buffer
         * @param message is the message sent by the server
         */
        public static void receive(Object message){
            synchronized (messageBuffer){
                ClientNetworkHandler.MessageReceiver.messageBuffer.add(message);
                messageBuffer.notifyAll();
            }
        }

        /**
         * @return the first available message in the buffer, otherwise null
         */
        private static Object getMessage(){
            synchronized (messageBuffer){
                if(!messageBuffer.isEmpty()){
                    Object message = messageBuffer.get(0);
                    messageBuffer.remove(message);
                    return message;
                }
            }
            throw new EmptyBufferException();
        }
    }
    private String ip;
    private Integer port;
    private Socket socket;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    private MessageAnalyzer analyzer;

    /**
     * Brief Initializes the server's ip and port in order to communicate with it and creates a new Analyzer
     * @param ip is the server's ip
     * @param port is the port through which client and server will communicate
     * @param view is the view reference to pass to the analyzer in order to call the methods on it
     */
    public ClientNetworkHandler(String ip, Integer port, View view){
        this.ip = ip;
        this.port = port;
        analyzer = new MessageAnalyzer(view);
        view.setDelegate(this);
        if (view.requestsImmediateRun()) run();
    }

    /**
     * Brief calls run() on View request
     */
    @Override
    public void onRunRequested() {
        run();
    }

    /**
     * Brief updates the IP
     */
    @Override
    public void onIPUpdated(String ip) {
        this.ip = ip;
    }

    /**
     * Brief updates the Port
     */
    @Override
    public void onPortUpdated(Integer port) {
        this.port = port;
    }

    /**
     * Brief sends a message
     */
    @Override
    public void sendMessage(Object message) {
        asyncWrite(message);
    }

    /**
     * Brief Resets the socket's timeout
     */
    private void resetTimeout(){
        try {
            socket.setSoTimeout(0);
        } catch (SocketException socketException) {
            socketException.printStackTrace();
        }
    }

    /**
     * Brief Forces the closing of the client application
     */
    private void shutdownSocket(){
        System.out.println(ClientSocketMessage.serverShutdownMessage);
        System.out.println(ClientSocketMessage.successfulClosingMessage);
        System.exit(0);
    }

    /**
     * Brief Analyzes the message coming from the server
     */
    private void analyzeIncomingMessages(){
        while(true) {
            try {
                Object serverResponse = ClientNetworkHandler.MessageReceiver.getMessage();
                if (serverResponse instanceof LobbyMessage)
                    handleMessage((LobbyMessage) serverResponse);
                else if (serverResponse instanceof GameMessage)
                    handleMessage((GameMessage) serverResponse);
                else if (serverResponse instanceof TextMessage)
                    analyzer.getView().printMessage(((TextMessage) serverResponse).getContent());
                else if (serverResponse instanceof WinMessage) {
                    if(((WinMessage) serverResponse).getTextMessage() != null)
                        analyzer.getView().printMessage(((WinMessage) serverResponse).getTextMessage().getContent());
                    restartClient();
                }
            } catch (EmptyBufferException ignored){
                waitMessage();
            }
        }
    }

    /**
     * Brief Restarts the client assigning new values to its most relevant parameters
     */
    private void restartClient() {
        sendMessage(new GameMessage(View.getPlayerId(), false, null));  //receiving confirmation
        analyzer.getView().interruptReadingProcess(true);
        analyzer = new MessageAnalyzer(View.getView(analyzer.getView().getType()));
        analyzer.getView().setDelegate(this);
        View.ViewStatus.resetStatus();
        if(analyzer.getView().getType().equals(ViewType.CLI))
            BoardPrinter.resetBoardRow();
        System.gc();
    }

    /**
     * Brief Waits until a new message arrives
     */
    private void waitMessage() {
        synchronized (MessageReceiver.messageBuffer){
            try {
                MessageReceiver.messageBuffer.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Brief Handles a message received during the lobby phase or just containing textual information to print
     * @param message is the message to handleMessage
     */
    private void handleMessage(LobbyMessage message) {
        if (message.getId() != null)
            analyzer.analyzeMessage(message);
        if (message.getTextualMessageId() != null)
            analyzer.parseMessageId(message.getTextualMessageId());
    }

    /**
     * Brief Analyzes a message received during the game
     * @param message is the message to handleMessage
     */
    private void handleMessage(GameMessage message) {
        View.ViewStatus.lockStatus();
        analyzer.analyzeMessage(message, (analyzedMessage) -> {
            if(!View.ViewStatus.isInterrupted)
                if (analyzedMessage != null)
                    sendMessage(analyzedMessage);
            else View.ViewStatus.isInterrupted = false;
            View.ViewStatus.unlockStatus();
        });
    }

    /**
     * Brief Asynchronously reads a message received from the server
     */
    public void asyncRead() {
        new Thread(() -> {
            while(true){
                Object serverResponse;
                try {
                    serverResponse = inputStream.readObject();
                    if(serverResponse instanceof QuitMessage){
                        if(!((QuitMessage) serverResponse).isGameOver())
                            analyzer.getView().printMessage(QuitMessage.getAnswer());
                        break;
                    }
                    else if(serverResponse instanceof KickMessage){
                        analyzer.getView().printMessage(KickMessage.getMessage());
                        break;
                    }
                    else if(serverResponse instanceof InterruptionMessage) {
                        analyzer.getView().interruptReadingProcess(false);
                        View.ViewStatus.isInterrupted = true;
                    }
                    else
                        ClientNetworkHandler.MessageReceiver.receive(serverResponse);
                } catch (SocketTimeoutException e) {
                    resetTimeout();
                } catch (EOFException e) {
                    //this client is disconnecting
                    break;
                } catch (SocketException e) {
                    shutdownSocket();
                } catch (ClassNotFoundException | IOException ignored) {
                }
            }
            closeConnection();
        }).start();
    }

    /**
     * Brief Sends a message to the server
     * @param message is the message to be written
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public static void asyncWrite(Object message){
        new Thread(() -> {
            try {
                synchronized (outputStream) {
                    try {
                        outputStream.reset();
                        outputStream.writeObject(message);
                        outputStream.flush();
                    } catch (IOException ignored) {
                    }
                }
            } catch (NullPointerException ignored){
            }
        }).start();
    }

    /**
     * Brief Closes all the active connections with the server, then closes the application
     */
    private void closeConnection(){
        try {
            if(outputStream != null)
                outputStream.close();
            if(inputStream != null)
                inputStream.close();
            socket.close();
        } catch (NullPointerException e) {
            System.out.println(ClientSocketMessage.serverShutdownMessage);
        } catch (IOException ignored) {
        } finally {
            System.out.println(ClientSocketMessage.successfulClosingMessage);
        }
        System.exit(0);
    }

    /**
     * Brief Tries to create a new connection with the server, closing every connection if some troubles have been encountered
     */
    private void connectToServer() {
        try {
            socket = new Socket(ip, port);
            System.out.println(ClientSocketMessage.queuedMessage);
        } catch(ConnectException e){
            closeConnection();
        } catch (IOException ignored) {
        }
    }

    /**
     * Brief Checks whether the server is full or not by waiting if within a certain amount of time it succeeds in connecting to the server
     */
    private void checkServerCapability(){
        int TIME_TO_WAIT = 1000; //[ms]
        new Thread(() -> {
            while(!socket.isClosed()){
                try {
                    socket.setSoTimeout(TIME_TO_WAIT);
                    while(socket.getSoTimeout() > 0) Thread.onSpinWait();
                } catch (SocketException ignored) {
                }
            }
        }).start();
    }

    /**
     * Brief Connects the I/O streams with the server's ones, closing every connection if the server is full
     */
    private void createStreamingConnections(){
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(ClientSocketMessage.successfulConnectionMessage);
        } catch(SocketTimeoutException e){
            System.out.println(ClientSocketMessage.fullServerMessage);
            closeConnection();
        } catch (IOException ignored) {
        }
    }

    /**
     * Brief Connects the client with the server, then lets them communicate each other
     */
    @Override
    public void run() {
        connectToServer();
        checkServerCapability();
        createStreamingConnections();
        analyzer.getView().printGameTitle();
        asyncRead();
        analyzeIncomingMessages();
    }
}

