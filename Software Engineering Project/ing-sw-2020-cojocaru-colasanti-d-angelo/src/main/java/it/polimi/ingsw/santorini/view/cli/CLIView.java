package it.polimi.ingsw.santorini.view.cli;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.view.utils.SelectionMessage;
import it.polimi.ingsw.santorini.view.utils.UtilityMessage;

import java.util.*;
import java.util.function.Consumer;

/**
 * Brief Creates a textual representation of the gaming scenario
 */
public class CLIView extends View {
    private final CLIPrinter printer;
    private final CLIReader reader;
    private int TIME = 5;   //[s]
    private TimerTask task = createTimerTask();

    private static final Color INPUT_OPTIONS_COLOR = Color.REVERSED;

    /**
     * Brief Creates a new CLI initializing its reader and printer
     */
    public CLIView(){
        printer = new CLIPrinter();
        reader = new CLIReader();
    }

    /**
     * Brief Creates a new task for the timer execution
     * @return the created task
     */
    private TimerTask createTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if(TIME >= 0){
                    printTime(TIME);
                    TIME--;
                } else
                    interruptReadingProcess(false);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean requestsImmediateRun() {
        return true;
    }

    @Override
    public void updateScreen(Object[][] board, List<ImmutablePosition> validPositions) {
        String[][] workersBoard = new String[defaultHeight][defaultHeight];
        if(buildingLevels == null)
            buildingLevels = new Integer[defaultHeight][defaultWidth];
        for(int i = 0; i < defaultHeight; i++)
            for (int j = 0; j < defaultWidth; j++)
                deserializeCell(board[i][j], workersBoard, i, j);
        CLIPrinter.cleanScreen();
        printer.printUtilityCommands(cardsInGame.size() != 0);
        BoardPrinter.printBoard(workersBoard, buildingLevels, validPositions);
    }

    /**
     * Brief Analyzes the content of a board's cell, updating two other boards: one containing all the buildings' levels and the other one ready to be printed
     * @param cell is the cell that has to be analyzed
     * @param workersBoard is a board containing the workers' genders correctly formatted with their respective color
     * @param row is the cell's row
     * @param col is the cell's column
     */
    @SuppressWarnings({"unchecked"})
    private void deserializeCell(Object cell, String[][] workersBoard, int row, int col) {
        if (cell instanceof List)  //position is occupied
            extractCellContent((List<Object>) cell, workersBoard, row, col);
        else
            buildingLevels[row][col] = (Integer) cell;   //level in position (i, j)
    }

    /**
     * Brief Extracts the content of an occupied board's cell checking if it's a worker or a building
     * @param cell is the list of objects on the Cell
     * @param board is the board
     * @param row is the cell's row
     * @param col is the cell's column
     */
    private void extractCellContent(List<Object> cell, String[][] board, int row, int col) {
        String color = "", gender = "";
        for (Object content : cell) {
            if (content instanceof String) {    //the content is a worker
                try {
                    Color.valueOf((String) content);
                    color = (String) content;
                } catch (IllegalArgumentException e) {
                    gender = (String) content;
                }
            } else  //the content is a building
                buildingLevels[row][col] = (Integer) content;
        }
        board[row][col] = Color.formatMessageColor(gender, Color.valueOf(color));
    }

    @Override
    public void chooseGameMode(List<String> availableModes, List<Integer> correctInputs, Consumer<Integer> onCompletion) {
        Integer choice;
        CLIPrinter.cleanScreen();
        printMessage(SelectionMessage.gameModeSelectionMessage);
        for(String mode : availableModes)
            printMessage(Color.formatMessageColor(String.valueOf(availableModes.indexOf(mode)), INPUT_OPTIONS_COLOR) + ": " + mode);
        choice = reader.getCorrectInput("Chosen mode: ", correctInputs);
        onCompletion.accept(choice);
    }

    @Override
    public void chooseUsername(Integer playerId, Consumer<String> onCompletion) {
        View.playerId = playerId;
        do {
            CLIPrinter.print(SelectionMessage.usernameSelectionMessage);
            playerName = reader.readLine();
        }
        while (playerName == null);
        onCompletion.accept(playerName);
    }

    @Override
    public void selectColor(Consumer<String> onCompletion) {
        String choice;
        List<Integer> correctInputs = new ArrayList<>();
        Color chosenColor;
        if(workersColors.size() > 1){   //let the user choose a color if there are at least two available choices
            CLIPrinter.cleanScreen();
            printMessage(SelectionMessage.colorSelectionMessage);
            for (Color color : workersColors) {
                correctInputs.add(workersColors.indexOf(color));
                printMessage(Color.formatMessageColor(String.valueOf(workersColors.indexOf(color)), INPUT_OPTIONS_COLOR) + ": " + Color.formatMessageColor(color.getName(), color));
            }
            chosenColor = workersColors.get(reader.getCorrectInput("Chosen color: ", correctInputs));
        }
        else
            chosenColor = workersColors.get(0);
        playerColor = chosenColor;
        choice = chosenColor.getName();
        onCompletion.accept(choice);
    }

    @Override
    public void getVote(Map<Integer, String> players, String question, Consumer<Integer> onCompletion){
        Integer choice;
        List<Integer> ids = new ArrayList<>(players.keySet());
        CLIPrinter.cleanScreen();
        printMessage(question);
        for(Integer id : ids)
            printMessage(Color.formatMessageColor(String.valueOf(id), INPUT_OPTIONS_COLOR) + " - " + players.get(id));
        choice = reader.getCorrectInput("Chosen player: ", ids);
        onCompletion.accept(choice);
    }

    @Override
    public void chooseFirstPlayer(List<String> names, Consumer<Integer> onCompletion) {
        Integer choice;
        List<Integer> correctInputs = new ArrayList<>();
        CLIPrinter.cleanScreen();
        printMessage(SelectionMessage.firstPlayerSelectionMessage);
        for(int i = 1; i <= names.size(); i++) {
            correctInputs.add(i);
            printMessage(Color.formatMessageColor(String.valueOf(i), INPUT_OPTIONS_COLOR) + ": " + names.get(i-1));
        }
        choice = reader.getCorrectInput("Chosen player: ", correctInputs);
        onCompletion.accept(choice);
    }

    @Override
    public void chooseCards(List<ImmutableCard> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion) {
        Integer choice;
        List<Integer> chosenIds = new ArrayList<>();

        CLIPrinter.cleanScreen();
        printMessage(SelectionMessage.cardSelectionMessage);
        for(; numberOfSelections > 0; numberOfSelections--){
            printMessage(SelectionMessage.remainingSelections + numberOfSelections);
            choice = getChosenCard(cards);
            chosenIds.add(choice);
            removeSelectedCard(choice, cards);
            CLIPrinter.cleanScreen();
        }
        onCompletion.accept(chosenIds);
    }

    /**
     * Brief Removes the card with the chosen id from the deck of cards passed as a parameter to the method
     * @param chosenCardId is the Id of the card to be removed
     * @param cards is the List of cards
     */
    private void removeSelectedCard(Integer chosenCardId, List<ImmutableCard> cards) {
        cards.removeIf(card -> card.getId().equals(chosenCardId));
    }

    /**
     * @param cards are the cards to choose from
     * @return the chosen card's id
     */
    private Integer getChosenCard(List<ImmutableCard> cards){
        List<Integer> correctInputs = new ArrayList<>();
        for (ImmutableCard card : cards) {
            correctInputs.add(card.getId());
            printMessage(Color.formatMessageColor(String.valueOf(card.getId()), INPUT_OPTIONS_COLOR) + " - " + card.getName() + " (" + card.getTitle() + ")" + ": " + "\n" + card.getDescription() + "\n");
        }
        return reader.getCorrectInput("Chosen card: ", correctInputs);
    }

    @Override
    public void chooseWorker(List<Integer> correctInputs, List<String> genders, Consumer<Integer> onCompletion) {
        Integer choice;
        printTables();
        printMessage(SelectionMessage.workerSelectionMessage);
        for(Integer i : correctInputs)
            printMessage(Color.formatMessageColor(String.valueOf(i), INPUT_OPTIONS_COLOR) + ": " + genders.get(i));
        choice = reader.getCorrectInput("Chosen worker: ", correctInputs);
        onCompletion.accept(choice);
    }

    @Override
    public void choosePosition(List<ImmutablePosition> positions, String selectionTypeMessage,
                               ImmutablePosition currentPosition, Consumer<ImmutablePosition> onCompletion){
        printTables();
        printMessage(selectionTypeMessage);
        if(currentPosition == null)
            onCompletion.accept(handlePlacementPhase(positions));
        else
            onCompletion.accept(handleTurn(positions, currentPosition));
    }

    /**
     * Brief Handles the turn phase (Move+Build) asking the player to choose a direction where to move or to build
     * @param positions is the Listof valid positions that can be reached
     * @param currentPosition is the position of the current cell
     * @return the position in the chosen direction
     */
    private ImmutablePosition handleTurn(List<ImmutablePosition> positions, ImmutablePosition currentPosition) {
        CLIPrinter.print("Chosen position: ");
        ImmutablePosition position = TurnCommand.getPositionByCommand(reader.getSingleCharInput(), currentPosition);
        while (position == null || !positions.contains(position)) {
            reader.unlockInputStream();
            printMessage(SelectionMessage.invalidChoiceMessage);
            position = TurnCommand.getPositionByCommand(reader.getSingleCharInput(), currentPosition);
        }
        return position;
    }

    /**
     * Brief Handles the workers placement phase asking the player to select the position where to place each worker
     * @return the chosen position
     */
    private ImmutablePosition handlePlacementPhase(List<ImmutablePosition> positions) {
        ImmutablePosition position = placeWorker();
        while (!positions.contains(position)) {
            printMessage(SelectionMessage.invalidChoiceMessage);
            position = placeWorker();
        }
        return position;
    }

    /**
     * Brief Asks the player to select a position by typing its coordinates' values
     * @return the chosen position
     */
    private ImmutablePosition placeWorker(){
        int x, y;
        List<Integer> correctXInputs = new ArrayList<>(), correctYInputs = new ArrayList<>();

        for(int i = 0; i < defaultHeight; i++)
            correctXInputs.add(i);
        for (int j = 0; j < defaultWidth; j++)
            correctYInputs.add(j);

        x = reader.getCorrectInput(SelectionMessage.xCoordinateSelectionMessage, correctXInputs);
        y = reader.getCorrectInput(SelectionMessage.yCoordinateSelectionMessage, correctYInputs);
        return new ImmutablePosition(x, y);
    }

    @Override
    public void chooseBlockType(List<Integer> blockTypes, List<String> blockNames, Consumer<Integer> onCompletion) {
        Integer choice;
        printTables();
        printMessage(SelectionMessage.blockTypeSelectionMessage);
        for (int i = 0; i < blockNames.size(); i++)
            printMessage(Color.formatMessageColor(blockTypes.get(i).toString(),
                    INPUT_OPTIONS_COLOR) + ": " + blockNames.get(i));
        choice = reader.getCorrectInput("Chosen block type: ", blockTypes);
        onCompletion.accept(choice);
    }

    @Override
    public void skipOperation(String operationType, Consumer<Boolean> onCompletion) {
        Boolean choice;
        do {
            printTables();
            printMessage(SelectionMessage.skipOperationQuestion + operationType + "? " + SelectionMessage.skipOperationChoices);
            String input = reader.getSingleCharInput();
            switch(input) {
                case SelectionMessage.CONFIRM: choice = true; break;
                case SelectionMessage.DECLINE: choice = false; break;
                default: printMessage(SelectionMessage.badInputMessage); choice = null; break;
            }
        }
        while (choice == null);
        onCompletion.accept(choice);
    }

    @Override
    public void printMessage(String message) {
        CLIPrinter.println(message);
    }

    @Override
    public void printLobbyList(List<String> names) {
        printer.printPlayersList("Lobby", names);
        printer.resetCursorPosition();
    }

    @Override
    public void printLobbyCommands(List<String> commands, List<String> functions) {
        CLIPrinter.cleanScreen();
        printer.printLobbyCommands("Commands", commands, functions);
    }

    @Override
    public void printGameTitle() {
        CLIPrinter.cleanScreen();
        CLIPrinter.println(Color.formatMessageColor(UtilityMessage.textualTitle, Color.BLUE) + "\n");
        CLIPrinter.println(UtilityMessage.credits + "\n");
        CLIPrinter.println("\n");
    }

    @Override
    public void onUndoRequest(Consumer<Boolean> onCompletion) {
        boolean choice;
        Thread timer = new Thread(() -> {
            Timer t = new Timer();
            int period = 1000;  //[ms]
            t.schedule(task, 0, period);
        });
        timer.start();
        choice = reader.getUndoChoice();
        interruptTimer(timer);
        onCompletion.accept(choice);
     }

    @Override
    public void thisIsHost(Boolean host) {

    }

    @Override
    public void gameIsStarting() {

    }

    @Override
    public void waitOtherPlayers() {

    }

    @Override
    public void printTime(Integer time) {
        printer.printTime(time);
    }

    @Override
    public void interruptReadingProcess(boolean onRestart) {
        reader.interruptReading(onRestart);
    }

    /**
     * Brief Prints the tables containing relevant information such as the list of players or the commands each player can use
     */
    private void printTables(){
        printer.printPlayersList("Players", playersInGame);
        printer.printBlocksList();
        printer.printCommands();
        if(cardsInGame.size() != 0)
            printer.printCards(cardsInGame);
        printer.resetCursorPosition();
    }

    public static Color getInputOptionsColor() {
        return INPUT_OPTIONS_COLOR;
    }

    /**
     * Brief Interrupts the timer for the undo functionality
     * @param timer is a thread containing the real timer
     */
    private void interruptTimer(Thread timer){
        task.cancel();
        timer.interrupt();
        TIME = 5;
        ViewStatus.isInterrupted = false;
        task = createTimerTask();
    }
}
