package it.polimi.ingsw.santorini.view.gui;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.view.gui.controllers.*;
import it.polimi.ingsw.santorini.view.gui.controllers.delegates.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Brief The Graphical User Interface implementation of the View. It uses JavaFX through a helper GUIApp reference.
 * @see View
 */
public class GUIView extends View implements HomeSceneControllerDelegate, SettingsSceneControllerDelegate,
        LobbySceneControllerDelegate, SetupSceneControllerDelegate, GameSceneControllerDelegate {

    /**
     * Brief the GUIApp reference to use JavaFX
     */
    private final GUIApp gui;

    /**
     * Brief the Home scene controller
     */
    private HomeSceneController homeController;

    /**
     * Brief the Settings scene controller
     */
    private SettingsSceneController settingsController;

    /**
     * Brief the Lobby scene controller
     */
    private LobbySceneController lobbyController;

    /**
     * Brief the Setup scene controller
     */
    private SetupSceneController setupController;

    /**
     * Brief the Setup scene controller
     */
    private GameSceneController gameController;

    /**
     * Brief Object for lock during scenes interleaving
     */
    private final Object sceneInterleavingLock = new Object();

    private Boolean disabledLobbyAndSetup = false;

    /**
     * Brief constructor that loads a GUIApp and stores his reference
     */
    public GUIView() {
        GUIApp.runApp();
        gui = GUIApp.getInstance();
        showHomeScene();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean requestsImmediateRun() {
        return false;
    }

    /**
     * Brief sets to null the scenes' controllers references
     */
    public void resetSceneControllers() {
        homeController = null;
        lobbyController = null;
        setupController = null;
    }

    /**
     * Brief shows the home scene
     */
    public void showHomeScene() {
        resetSceneControllers();
        homeController = new HomeSceneController(this);
        gui.setScene(homeController.scene());
    }

    /**
     * Brief shows the settings scene
     */
    public void showSettingsScene() {
        resetSceneControllers();
        settingsController = new SettingsSceneController(this);
        gui.setScene(settingsController.scene());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showLobbyScene() {
        resetSceneControllers();
        lobbyController = new LobbySceneController(this);
        gui.setScene(lobbyController.scene());
    }

    /**
     * Brief shows the Setup scene
     */
    public void showSetupScene() {
        synchronized (sceneInterleavingLock) {
            resetSceneControllers();
            setupController = new SetupSceneController(this);
            gui.setScene(setupController.scene());
            sceneInterleavingLock.notify();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGameScene() {
        disabledLobbyAndSetup = true;
        resetSceneControllers();
        gameController = new GameSceneController(this, cardsInGame, playersInGame);
        gui.setScene(gameController.scene());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Object message) {
        delegate().sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateScreen(Object[][] board, List<ImmutablePosition> validPositions) {
        if (filterBoard(board) && (gameController == null || !gameController.isLoaded())) return;
        while(gameController == null || !gameController.isLoaded()) {
            if (gameController == null) showGameScene();
        }
        gameController.updateScreen(board, validPositions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseGameMode(List<String> availableModes, List<Integer> correctInputs, Consumer<Integer> onCompletion) {
        if (disabledLobbyAndSetup) return;
        synchronized (sceneInterleavingLock) {
            while(setupController == null || !setupController.isLoaded()) {
                try {
                    sceneInterleavingLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setupController.chooseGameMode(availableModes, correctInputs, onCompletion);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseUsername(Integer playerId, Consumer<String> onCompletion) {
        View.playerId = playerId;
        lobbyController.choosePlayerName(onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectColor(Consumer<String> onCompletion) {
        setupController.selectColor(workersColors, result -> {
            onCompletion.accept(result);
            playerColor = Color.valueOf(result);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getVote(Map<Integer, String> players, String question, Consumer<Integer> onCompletion) {
        setupController.getVote(players, question, onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseFirstPlayer(List<String> names, Consumer<Integer> onCompletion) {
        setupController.chooseFirstPlayer(names, onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseCards(List<ImmutableCard> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion) {
        setupController.chooseCards(cards, numberOfSelections, onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseWorker(List<Integer> correctInputs, List<String> genders, Consumer<Integer> onCompletion) {
        gameController.chooseWorker(correctInputs, genders, playerColor, onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosePosition(List<ImmutablePosition> positions, String selectionTypeMessage, ImmutablePosition currentPosition,
                               Consumer<ImmutablePosition> onCompletion) {
        while(gameController == null || !gameController.isLoaded()) {
            if (gameController == null) showGameScene();
        }
        gameController.choosePosition(positions, selectionTypeMessage, currentPosition, onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseBlockType(List<Integer> blockTypes, List<String> blockNames, Consumer<Integer> onCompletion) {
        gameController.chooseBlockType(blockTypes, blockNames, onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipOperation(String operationType, Consumer<Boolean> onCompletion) {
        gameController.skipOperation(operationType, onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage(String message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void thisIsHost(Boolean host) {
        if (!disabledLobbyAndSetup) lobbyController.thisIsHost(host);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameIsStarting() {
        if (!disabledLobbyAndSetup) lobbyController.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printLobbyList(List<String> names) {
        if (!disabledLobbyAndSetup) lobbyController.updatePlayers(names);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitOtherPlayers() {
        if (disabledLobbyAndSetup) return;
        synchronized (sceneInterleavingLock) {
            while(setupController == null || !setupController.isLoaded()) {
                try {
                    sceneInterleavingLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setupController.waitOtherPlayers();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printLobbyCommands(List<String> commands, List<String> functions) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void printGameTitle() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUndoRequest(Consumer<Boolean> onCompletion) {
        gameController.onUndoRequest(onCompletion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printTime(Integer time) {}

    /**
     * {@inheritDoc}
     */
    public void interruptReadingProcess(boolean b) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        delegate().onRunRequested();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChosenName(String name) {
        playerName = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnectionParametersChanged(String IP, Integer port) {
        delegate().onIPUpdated(IP);
        delegate().onPortUpdated(port);
    }

    /**
     * Brief utility fast deserializer
     * @param board the board to be deserialized
     * @return allZero if it contains only zeros
     */
    private boolean filterBoard(Object[][] board) {
        boolean allZero = true;
        for (Object[] objects : board) {
            for (int y = 0; y < board.length; y++) {
                if (objects[y] instanceof Integer && (Integer) objects[y] != 0) {
                    allZero = false;
                    break;
                }
            }
        }
        return allZero;
    }
}