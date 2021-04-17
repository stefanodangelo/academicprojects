package it.polimi.ingsw.santorini.view.gui.controllers;

import it.polimi.ingsw.santorini.communication.QuitMessage;
import it.polimi.ingsw.santorini.communication.TextMessage;
import it.polimi.ingsw.santorini.view.gui.controllers.delegates.LobbySceneControllerDelegate;
import it.polimi.ingsw.santorini.view.gui.scenes.LobbyScene;
import it.polimi.ingsw.santorini.view.gui.scenes.delegates.LobbySceneDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Brief Controller for the Lobby scene
 * @see LobbyScene
 * @see LobbySceneControllerDelegate
 * @see GUISceneController
 */
public class LobbySceneController extends GUISceneController<LobbyScene, LobbySceneControllerDelegate> implements LobbySceneDelegate {

    /**
     * Brief the number of current players in the lobby
     */
    private List<String> currentPlayers = new ArrayList<>();

    /**
     * Brief the name request
     */
    private Consumer<String> nameRequest;

    /**
     * Brief Boolean indicating whether this is the host
     */
    private Boolean host = false;

    /**
     * Brief Boolean indicating whether the name of the player has been confirmed
     */
    private Boolean nameInserted = false;

    /**
     * Brief max number of players
     */
    private Integer maxNumberOfPlayers = 2;

    /**
     * Brief lobby's current number of players
     */
    private Integer numberOfPlayers = 0;

    /**
     * Brief Main constructor: sets the delegate and loads the scene
     * @param delegate the delegate
     */
    public LobbySceneController(LobbySceneControllerDelegate delegate) {
        super(new LobbyScene(), delegate);
        scene().setDelegate(this);
        loadScene();
        loadingCompleted();
    }

    /**
     * Brief Lets the player choose his username
     * @param onCompletion accept the name entered
     */
    public void choosePlayerName(Consumer<String> onCompletion) {
        nameRequest = onCompletion;
        runSafely(() -> scene().loadNameInput());
    }

    /**
     * Brief Updates the list of players currently in game
     * @param players are the players connected to the lobby
     */
    public void updatePlayers(List<String> players) {
        if (!currentPlayers.equals(players)) {
            currentPlayers = players;
            runSafely(() -> scene().updatePlayersBox(players, nameInserted));
            numberOfPlayers = players.size();
            handleStartEnable();
        }
    }

    /**
     * Brief Handles visibility of start button
     */
    private void handleStartEnable() {
        if (nameInserted && host && numberOfPlayers.equals(maxNumberOfPlayers))
            runSafely(() -> scene().showStartButton());
        else if (nameInserted && host)
            runSafely(() -> scene().hideStartButton());
    }

    /**
     * Brief Sets the host parameter for {this}
     * @param host is true if {this} is the game host
     */
    public void thisIsHost(Boolean host) {
        if (this.host == host) return;
        this.host = host;
        runSafely(() -> scene().loadControlPane(host));
    }

    /**
     * Brief Starts the scene
     */
    public void start() {
        runSafely(() -> delegate().showSetupScene());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExit() {
        sendMessage(new QuitMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNumberOfPlayerChanged(Integer numberOfPlayers) {
        this.maxNumberOfPlayers = numberOfPlayers;
        handleStartEnable();
        sendMessage(new TextMessage(numberOfPlayers.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        sendMessage(new TextMessage(TextMessage.getStartRequest()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNameConfirmed(String name) {
        scene().hideOverLayer();
        if (!name.isEmpty()) {
            runAsync(() -> {
                nameRequest.accept(name);
                nameInserted = true;
                delegate().onChosenName(name);
            });
        } else choosePlayerName(nameRequest);
    }
}
