package it.polimi.ingsw.santorini.view;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.view.cli.CLIView;
import it.polimi.ingsw.santorini.view.delegates.ViewDelegate;
import it.polimi.ingsw.santorini.view.gui.GUIView;

import java.util.*;
import java.util.function.Consumer;

/**
 * Brief Factory for the type of view to show to the client
 */
public abstract class View {
    /**
     * Brief Class representing the status of the view
     */
    public static class ViewStatus {
        /**
         * Brief The view is busy if it is executing methods under server's request
         */
        public static boolean isBusy;
        /**
         * Brief Indicates whether the setup has completed or not
         */
        public static boolean isSetupOver;
        public static boolean isInterrupted;

        public static void lockStatus(){ isBusy = true; }
        public static void unlockStatus(){ isBusy = false; }
        public static void endSetup() {isSetupOver = true;}
        public static void resetStatus(){
            isInterrupted = false;
            isBusy = false;
            isSetupOver = false;
        }
    }

    /**
     * Brief the View's delegate
     */
    private ViewDelegate delegate;

    public ViewDelegate delegate() {
        return delegate;
    }

    public void setDelegate(ViewDelegate delegate) {
        this.delegate = delegate;
    }

    public abstract Boolean requestsImmediateRun();

    protected final List<Color> workersColors = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.RED, Color.CYAN));
    protected static Integer defaultHeight, defaultWidth;
    protected static Integer playerId;
    protected static Color playerColor;
    protected static String playerName;
    protected static ImmutablePosition activePosition;
    protected static List<ImmutableCard> cardsInGame = new ArrayList<>();
    protected static final List<String> playersInGame = new ArrayList<>();

    /**
     * Brief Simplified board containing the levels of the buildings actually existing on the real board
     */
    protected Integer[][] buildingLevels;

    /**
     * Brief Factory method for the View
     * @param type is the wanted type of View
     * @return the correct View's instance
     */
    public static View getView(ViewType type){
        switch(type){
            case GUI: return new GUIView();
            case CLI: default: return new CLIView();
        }
    }

    /**
     * Brief Prints the board on the screen
     * @param board is the game board
     * @param validPositions are the optional positions to display
     */
    public abstract void updateScreen(Object[][] board, List<ImmutablePosition> validPositions);

    /**
     * Brief Asks the player to choose a game modality
     * @param availableModes are the available modes to choose from
     * @param correctInputs are the allowed inputs for the choice,
     * @param onCompletion called when completed
     */
    public abstract void chooseGameMode(List<String> availableModes, List<Integer> correctInputs, Consumer<Integer> onCompletion);

    /**
     * Brief Asks the player to enter a username
     * @param playerId is the id of the player who has to enter the name
     * @param onCompletion called when completed
     */
    public abstract void chooseUsername(Integer playerId, Consumer<String> onCompletion);

    /**
     * Brief Asks the player to select a color
     * @param onCompletion called when completed
     */
    public abstract void selectColor(Consumer<String> onCompletion);

    /**
     * Brief Asks the player to vote for the most god-like one
     * @param players maps each id to the corresponding player's name
     * @param question is the question printed for the poll
     * @param onCompletion called when completed
     */
    public abstract void getVote(Map<Integer, String> players, String question, Consumer<Integer> onCompletion);

    /**
     * Brief Asks the player to choose the first player
     * @param names are the names of the players in game
     * @param onCompletion called when completed
     */
    public abstract void chooseFirstPlayer(List<String> names, Consumer<Integer> onCompletion);

    /**
     * Brief Asks the player to choose one or more card
     * @param cards are the available cards
     * @param numberOfSelections is the number of possible selections to make
     * @param onCompletion called when completed
     */
    public abstract void chooseCards(List<ImmutableCard> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion);

    /**
     * Brief Asks the player to select a worker
     * @param correctInputs are the valid inputs
     * @param genders are the workers' genders
     * @param onCompletion called when completed
     */
    public abstract void chooseWorker(List<Integer> correctInputs, List<String> genders, Consumer<Integer> onCompletion);

    /**
     * Brief Asks the player to select a position either for a Move or a Build
     * @param positions are the available positions
     * @param selectionTypeMessage is the message specifying whether the player is moving or building
     * @param currentPosition is the position of the current worker
     * @param onCompletion called when completed
     */
    public abstract void choosePosition(List<ImmutablePosition> positions,
                                                     String selectionTypeMessage, ImmutablePosition currentPosition,
                                                     Consumer<ImmutablePosition> onCompletion);

    /**
     * Brief Asks the player to select the type of the block to build
     * @param blockTypes are the available block types during the building phase
     * @param blockNames are the names of the types of block available for the choice
     * @param onCompletion called when completed
     */
    public abstract void chooseBlockType(List<Integer> blockTypes, List<String> blockNames, Consumer<Integer> onCompletion);

    /**
     * Brief Asks the player if he wants to skip an optional operation
     * @param operationType is the type of operation that will eventually be skipped
     * @param onCompletion called when completed
     */
    public abstract void skipOperation(String operationType, Consumer<Boolean> onCompletion);

    /**
     * Brief Prints any message
     * @param message is the message to be printed
     */
    public abstract void printMessage(String message);

    /**
     * Brief Prints the list of players in lobby
     * @param names contains all the players' names
     */
    public abstract void printLobbyList(List<String> names);

    /**
     * Brief Prints all the available commands in lobby
     * @param commands is a list of textual commands
     * @param functions contains the functions of each command contained in the commands list
     */
    public abstract void printLobbyCommands(List<String> commands, List<String> functions);

    /**
     * Brief Prints the title screen
     */
    public abstract void printGameTitle();

    /**
     * Brief Asks the player whether he wants to undo the whole turn or to confirm the choices made
     * @param onCompletion accepts true if the player chooses to undo the turn, otherwise false
     */
    public abstract void onUndoRequest(Consumer<Boolean> onCompletion);

    /**
     * Brief tells the view to mark that the player is a host or not
     * @param host Boolean true if player is host
     */
    public abstract void thisIsHost(Boolean host);

    /**
     * Brief tells the view that the game is starting
     */
    public abstract void gameIsStarting();

    /**
     * Brief tells the view to go in waiting mode (during setup phase)
     */
    public abstract void waitOtherPlayers();

    public abstract void printTime(Integer time);

    /**
     * Brief Interrupts the reading process distinguishing between when the client gets restarted and when not
     * @param onRestart is true if the client is being restarted
     */
    public abstract void interruptReadingProcess(boolean onRestart);

    public static Integer getDefaultHeight() {
        return defaultHeight;
    }

    public static Integer getDefaultWidth() {
        return defaultWidth;
    }

    public static void setDefaultHeight(Integer defaultHeight) {
        View.defaultHeight = defaultHeight;
    }

    public static void setDefaultWidth(Integer defaultWidth) {
        View.defaultWidth = defaultWidth;
    }

    public static Integer getPlayerId() {
        return playerId;
    }

    public static String getPlayerName() { return playerName; }

    /**
     * Brief Remove a color from the available colors list and formats the corresponding player's name with the removed color
     * @param color is the chosen color
     * @param name is the name of the player who chose that color
     */
    public void removeColor(String color, String name){
        Color c = Color.valueOf(color);
        workersColors.remove(c);
        playersInGame.set(playersInGame.indexOf(name), Color.formatMessageColor(name, c));
    }

    public static void setPlayerId(Integer playerId) {
        View.playerId = playerId;
    }

    public void addName(String name){
        playersInGame.add(name);
    }

    public static void setCardsInGame(List<ImmutableCard> cardsInGame) {
        View.cardsInGame = cardsInGame;
    }

    public static List<ImmutableCard> getCardsInGame() {
        return cardsInGame;
    }

    public static int getNumOfWorkers(){
        return playersInGame.size() * 2;
    }

    public static void setActivePosition(ImmutablePosition activePosition) {
        View.activePosition = activePosition;
    }

    public static ImmutablePosition getActivePosition() {
        return activePosition;
    }

    /**
     * Brief Deletes the current active position
     */
    public static void resetActivePosition(){
        activePosition = null;
    }

    public static List<String> getPlayersInGame() {
        return playersInGame;
    }

    /**
     * @return the type of {this} view
     */
    public ViewType getType(){
        if(requestsImmediateRun()) return ViewType.CLI;
        return ViewType.GUI;
    }
}
