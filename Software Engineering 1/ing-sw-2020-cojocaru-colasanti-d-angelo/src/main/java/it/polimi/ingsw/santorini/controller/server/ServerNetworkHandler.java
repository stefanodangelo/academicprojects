package it.polimi.ingsw.santorini.controller.server;

import it.polimi.ingsw.santorini.communication.*;
import it.polimi.ingsw.santorini.controller.CardLoader;
import it.polimi.ingsw.santorini.controller.Controller;
import it.polimi.ingsw.santorini.controller.MessageReceiver;
import it.polimi.ingsw.santorini.controller.utils.ConnectionMessage;
import it.polimi.ingsw.santorini.controller.utils.LobbyCommand;
import it.polimi.ingsw.santorini.communication.NetworkMessage;
import it.polimi.ingsw.santorini.model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Brief Singleton handling the server network by a list of observers
 */
public class ServerNetworkHandler extends Observable<Object> {
    private static ServerNetworkHandler instance;
    private Integer port;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private volatile Boolean gameStarted;
    private final List<String> activeNames;
    private MessageReceiver receiver;
    private final Object gameStatusLock = new Object();

    /**
     * Brief Sets all the server's parameters and creates a new ExecutorService to execute all the interactions with each client
     */
    private ServerNetworkHandler(){
        executor = Executors.newFixedThreadPool(MAX_CAPABILITY);
        gameStarted = false;
        activeNames = new ArrayList<>(MAX_CAPABILITY);
        initializeNames();
    }

    /**
     * @return the instance of the only one ServerNetworkHandler created
     */
    public static ServerNetworkHandler getInstance(){
        if(instance == null)
            instance = new ServerNetworkHandler();
        return instance;
    }

    /**
     * Brief Removes all the names already contained in the list of connected players' names and initializes it with an empty value
     * @implNote the method maintains the names list with a dimension that is equal to the maximum capability of the server to also keep the right order of connections established by the clients
     */
    private void initializeNames(){
        if(!activeNames.isEmpty())
            activeNames.removeAll(Collections.unmodifiableList(activeNames));
        for(int i = 0; i < getMaxCapability(); i++)
            activeNames.add("");
    }

    /**
     * @return true if the number of players is correct and the game can consequently start
     */
    protected boolean allowStart(){
        return getObserversNumber().equals(CURRENT_CAPABILITY) && getActiveNames().size() == CURRENT_CAPABILITY;
    }

    /**
     * Brief Sends a message to a client or to all the connected clients
     * @implNote the id must be subtracted by 1 because of the adopted convention about the players' ids that start from 1 instead of 0
     * @param clientId is the id of the client that will receive the message
     * @param message is the message to send
     */
    public void send(Integer clientId, Object message){
        try {
            if (message instanceof GameMessage && ((GameMessage) message).isBroadcast())
                notifyObservers(message);
            else
                notifyObservers(clientId - 1, message);
        } catch (ArrayIndexOutOfBoundsException ignored){
        }
    }

    /**
     * Brief Sends a message to all the players except the current playing one
     * @param message is the message to send
     * @param id is the id of the player who mustn't receive the message
     */
    public void sendToWaitingPlayers(Object message, Integer id){
        for(Player p : PlayersHandler.getPlayers())
            if(!p.getId().equals(id))
                send(p.getId(), message);
    }

    /**
     * Brief Receives a message and, if the message is a broadcast one, forwards it to the other clients
     * @param message is the received message
     */
    public void receive(GameMessage message){
        new Thread(() -> {
            if(message.isBroadcast())   //forwards the message
                send(null, new GameMessage(message.isBroadcast(), message.getData()));
            receiver.receive(message);
        }).start();
    }

    /**
     * Brief Creates the server and connects it to the clients on a certain specified port
     * @param port is the port through which the communication takes place
     */
    public void startServer(Integer port){
        this.port = port;
        createServer();
        new Thread(() -> {
            while (true){
                if(!gameStarted){
                    if(getObserversNumber().equals(CURRENT_CAPABILITY)) continue;
                    else acceptPlayers();
                }
                loadCardsAndMap();
                startGame();
                waitEndGame();
            }
        }).start();
    }

    /**
     * Brief Accepts whoever tries to connect to the server, but only if there's enough capability
     */
    private void acceptPlayers() {
        //lobby formation
        System.out.println(ConnectionMessage.serverWaitingMessage);
        while (!gameStarted) {
            try {
                listen();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Brief Waits for the end of the game by synchronizing on a lock Object
     */
    private void waitEndGame() {
        synchronized (gameStatusLock) {
            try {
                gameStatusLock.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Brief Creates the server, closing the application if something went wrong with the creation process
     */
    private void createServer(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            System.out.println(ConnectionMessage.serverCreationErrorMessage);
            System.exit(-1);
        }
    }

    /**
     * Brief Makes the server waiting for the receiving of a connection request from a client
     */
    private void listen() throws IOException {
        Socket clientSocket = null;
        int TIMEOUT = 1000; //[ms]

        try {
            serverSocket.setSoTimeout(TIMEOUT);
            //refusing of any other incoming connections
            while (getObserversNumber().equals(CURRENT_CAPABILITY) && getObserversNumber() >= MIN_NUMOF_PLAYERS)
                clientSocket = serverSocket.accept();
            //TCP connection opening
            if (clientSocket == null)
                clientSocket = serverSocket.accept();
            //client connection
            if (getObserversNumber() < CURRENT_CAPABILITY)
                connectToClient(clientSocket);
        } catch (SocketTimeoutException e) {
            serverSocket.setSoTimeout(0);
        }
    }

    /**
     * Brief Starts a new game on a separate thread, sending to each client the parameters needed for the setup
     */
    private void startGame(){
        notifyObservers(new GameMessage(MethodHeading.PARAMS.ordinal(), true, Arrays.asList(GameMap.getDefaultHeight(), GameMap.getDefaultWidth(), getActiveNames())));
        new Thread(() -> {
            TurnBasedGame game = new SantoriniGame(new Controller(), CURRENT_CAPABILITY);
            try {
                game.setupGame();
                game.startGame();
            } catch (IndexOutOfBoundsException ignored){
            }
        }).start();
    }

    /**
     * Brief Loads the map and the game cards in a separated thread
     */
    private void loadCardsAndMap() {
        new Thread(() -> {
            CardLoader.loadCards();
            SantoriniGame.createMap();
        }).start();
    }

    /**
     * Brief Connects the server to the corresponding client's socket, executing the interaction between the two parties
     * @param socket is the socket linked to the client
     */
    private void connectToClient(Socket socket){
        ClientConnectionSocket client = new ClientConnectionSocket(this, socket);
        System.out.println(ConnectionMessage.successfulConnectionMessage);
        //client communication starting
        executor.submit(client);
        if(getObserversNumber() < CURRENT_CAPABILITY)
            System.out.println(ConnectionMessage.serverWaitingMessage);
    }

    /**
     * Brief Removes a specific connection between server and client
     * @implNote the id must be subtracted by 1 because of the adopted convention about the players' ids that start from 1 instead of 0
     * @param observer is the connection to remove from the server
     * @param onKickedPlayer is true if the disconnection takes place during the kick of a player
     */
    protected void disconnect(Observer<Object> observer, Integer id, boolean onKickedPlayer) {
        String disconnectedPlayerName = activeNames.get(id - 1);
        if(getObserversNumber().equals(1)){
            unregisterAllObservers();
            initializeNames();
            executor.shutdownNow();
            executor = Executors.newFixedThreadPool(MAX_CAPABILITY);
        } else {
            unregisterClient(observer, disconnectedPlayerName);
            if (isGameStarted() || onKickedPlayer) {
                boolean isCurrentPlayer = id.equals(PlayersHandler.getCurrentPlayer().getId());
                Player disconnectedPlayer = PlayersHandler.getPlayerById(id);
                new Thread(() -> {
                    SantoriniGame.removeWorkers(disconnectedPlayer);
                    PlayersHandler.removePlayer(disconnectedPlayer);
                    PlayersHandler.setCurrentPlayerIndex(id - 1);
                }).start();
                if (getObserversNumber().equals(1)) {
                    notifyClients(disconnectedPlayerName);
                    notifyObservers(new InterruptionMessage());
                    new Thread(this::restartServer).start();
                } else {
                    notifyObservers(new TextMessage(ConnectionMessage.clientDisconnectionMessage + disconnectedPlayerName));
                    if (isCurrentPlayer)
                        receiver.receive(new GameMessage(id, true, Collections.singletonList(-1)));
                }
            } else {
                sendCommandsList(false);
                sendRoles();
            }
        }
    }

    /**
     * Brief Unregister the disconnected client and updates the list of active names
     * @implNote the method maintains the names list with a dimension that is equal to the maximum capability of the server to also keep the right order of connections established by the clients
     * @param observer is the client who has just disconnected
     * @param disconnectedPlayerName is the name of the client who has just disconnected
     */
    private void unregisterClient(Observer<Object> observer, String disconnectedPlayerName){
        activeNames.remove(disconnectedPlayerName);
        activeNames.add("");
        unregisterObserver(observer);
        updateObserversIds();
    }

    /**
     * Brief Notifies the clients still connected to the server about the just occurred disconnection
     * @param disconnectedPlayerName is the name of the player who has just disconnected
     */
    private void notifyClients(String disconnectedPlayerName){
        send(HOST_ID, new LobbyMessage(MethodHeading.LOBBYCOMMANDS.ordinal(), Arrays.asList(LobbyCommand.getLobbyCommands(), LobbyCommand.getCommandsHelpingText())));
        notifyObservers(new LobbyMessage(MethodHeading.LOBBYLIST.ordinal(), Collections.singletonList(getActiveNames())));
        notifyObservers(new TextMessage(ConnectionMessage.clientDisconnectionMessage + disconnectedPlayerName));
        send(HOST_ID, new LobbyMessage(NetworkMessage.host.ordinal()));
    }

    /**
     * Brief Restarts the server by unregistering all the listening Observers and by reinitializing the needed parameters. It also wakes up the server in order to make it listen for any new incoming connection
     */
    private void restartServer() {
        try {
            TurnBasedGame.endGame();
        } catch (RuntimeException ignored){
        }
        receiver = new MessageReceiver();
        if(!getObserversNumber().equals(CURRENT_CAPABILITY))
            CURRENT_CAPABILITY = MIN_NUMOF_PLAYERS;
        //enabling new lobby formation at the end of the game
        gameStarted = false;
        synchronized (gameStatusLock){
            gameStatusLock.notifyAll();
        }
        System.gc();    //suggesting to make a garbage collection
    }

    /**
     * Brief Brings the server back to the lobby phase at the end of a game
     */
    public void reloadLobby(){
        sendCommandsList(false);
        restartServer();
    }

    /**
     * @return the list of clients' names connected to the server
     */
    public List<String> getActiveNames() {
        List<String> names = new ArrayList<>();
        for(String name : activeNames)
            if(name.length() > 0)
                names.add(name);
        return names;
    }

    /**
     * Brief Adds a name at a specific position, that is id - 1, in the names list
     * @param id is the value of the index, increased by 1, where the name will be put
     * @param name is the name to insert in the list
     */
    public void addName(Integer id, String name){
        activeNames.set(id - 1, name);
    }

    public synchronized boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Brief Resizes lobby's capability if it has a legit value and if the resize doesn't entail to kick a client
     * @param dimension is the new wanted dimension's value
     * @return true if the size has been successfully changed
     */
    public synchronized boolean setLobbyDimension(Integer dimension) {
        if((MIN_NUMOF_PLAYERS <= dimension && dimension <= MAX_CAPABILITY) && (getObserversNumber() <= dimension) && !dimension.equals(CURRENT_CAPABILITY)){
            CURRENT_CAPABILITY = dimension;
            return true;
        }
        return false;
    }

    public static Integer getHostId() {
        return HOST_ID;
    }

    public static Integer getMinCapability(){
        return MIN_NUMOF_PLAYERS;
    }

    public static Integer getActualCapability() { return CURRENT_CAPABILITY;}

    public static Integer getMaxCapability(){
        return MAX_CAPABILITY;
    }

    public void setReceiver(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    /**
     * @return a list containing the ids of all the clients connected to the server
     */
    public List<Integer> getClientsIds(){
        List<Integer> ids = new ArrayList<>();
        for(int i = HOST_ID; i <= getObserversNumber(); i++)
            ids.add(i);
        return ids;
    }

    /**
     * Brief Sends a specific list of commands to each player or to a specific player
     * @implNote the notification follows id+1 convention
     * @param onlyToHost specify whether to send the message to everyone or only to the host
     */
    protected void sendCommandsList(boolean onlyToHost) {
        send(HOST_ID, new LobbyMessage(MethodHeading.LOBBYCOMMANDS.ordinal(), Arrays.asList(LobbyCommand.getLobbyCommands(), LobbyCommand.getCommandsHelpingText())));
        if (!onlyToHost){
            for(int i = ServerNetworkHandler.getHostId(); i < getObserversNumber(); i++)
                send(i+1, new LobbyMessage(MethodHeading.LOBBYCOMMANDS.ordinal(), Arrays.asList(QuitMessage.getRequest(), QuitMessage.getCommandDescription())));
            notifyObservers(new LobbyMessage(MethodHeading.LOBBYLIST.ordinal(), Arrays.asList(getClientsIds(), getActiveNames())));
        } else
            send(HOST_ID, new LobbyMessage(MethodHeading.LOBBYLIST.ordinal(), Arrays.asList(getClientsIds(), getActiveNames())));
    }

    /**
     * Brief Sends to each connected client his role in the lobby, that is if he's the host or not
     */
    protected void sendRoles(){
        send(ServerNetworkHandler.getHostId(), new LobbyMessage(NetworkMessage.host.ordinal()));
        send(ServerNetworkHandler.getHostId(), new LobbyMessage(NetworkMessage.demandInput.ordinal()));
        for(int clientId = HOST_ID + 1; clientId <= getActiveNames().size(); clientId++){
            send(clientId, new LobbyMessage(NetworkMessage.waitStart.ordinal()));
        }
    }
}
