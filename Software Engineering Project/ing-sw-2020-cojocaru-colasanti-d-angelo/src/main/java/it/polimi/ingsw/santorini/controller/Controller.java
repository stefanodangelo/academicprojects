package it.polimi.ingsw.santorini.controller;

import it.polimi.ingsw.santorini.communication.GameMessage;
import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.communication.MethodHeading;
import it.polimi.ingsw.santorini.model.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Brief Controller communicating with the View through the server
 */
public class Controller implements GameDelegate {
    private final VirtualView view;

    public Controller(){
        view = new VirtualView();
    }

    /**
     * Brief Requests the host player to choose the wanted game mode, that is with basic, complete or advanced rules
     * @param playerId is the host player's id
     * @return the chosen mode
     */
    @Override
    public GameMode requestGameMode(Integer playerId) {
        GameMode[] modes = GameMode.values();
        List<String> availableModes = new ArrayList<>();
        List<Integer> correctInputs = new ArrayList<>();

        for (int i = 0; i < modes.length; i++) {
            availableModes.add(modes[i].getMode());
            correctInputs.add(i);
        }
        return GameMode.modeByInt((Integer) view.manageChoice(new GameMessage(MethodHeading.MODE.ordinal(), false, Arrays.asList(availableModes, correctInputs)), playerId).getData().get(0));
    }

    /**
     * Brief Requests the players to insert a username
     * @param onCompletion creates a new Player initialized with the chosen username and with playerId
     */
    @Override
    public void requestPlayersNames(Consumer<List<String>> onCompletion) {
        onCompletion.accept(view.getPlayersNames());
    }

    /**
     * Brief Asks a player to select a workers' color
     * @param playerId is the id of the player who has to select the color
     * @param onCompletion assigns the corresponding workers once selected the color
     */
    @Override
    public void requestPlayerColor(Integer playerId, Consumer<String> onCompletion) {
        onCompletion.accept((String) view.manageChoice(new GameMessage(MethodHeading.COLOR.ordinal(), false, null), playerId).getData().get(0));
    }

    /**
     * Brief For each player's id asks the respective player to vote the most god-like player
     * @param playersIds contains all the players' ids in game
     * @param poll is the poll to send to the players
     * @param onCompletion is the lambda function responsible of the voting system
     */
    @Override
    public void sendPoll(List<Integer> playersIds, Poll poll, Consumer<List<Integer>> onCompletion) {
        Map<Integer, String> players = new HashMap<>();
        for(Integer id : playersIds)
            players.put(id, PlayersHandler.getPlayerById(id).getName());
        onCompletion.accept(view.getVote(players, poll.getQuestion()));
    }

    /**
     * Brief Requests the god-like player to choose which player will start first the game
     * @param playerId is the god-like player's id
     * @param players are the ids of the players to choose from
     * @param onCompletion sets the current player to the chosen one
     */
    @Override
    public void requestFirstPlayerChoice(Integer playerId, ArrayList<Player> players, Consumer<Integer> onCompletion) {
        List<String> names = new ArrayList<>();
        for(Player p : players)
            names.add(p.getName());
        onCompletion.accept((Integer) view.manageChoice(new GameMessage(MethodHeading.FIRST.ordinal(), false, Collections.singletonList(names)), playerId).getData().get(0));
    }

    /**
     * Brief Forwards to the most god-like player the request of cards selection
     * @param playerId is the most god-like player's id
     * @param cards is the list of cards that can be chosen
     * @param numberOfSelections is the number of cards to select
     * @param onCompletion replace the available cards with those selected
     */
    @Override
    @SuppressWarnings("unchecked")
    public void requestCardsSelection(Integer playerId, ArrayList<Card> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion) {
        List<ImmutableCard> clientCards = new ArrayList<>();
        for(Card card : cards)
            clientCards.add(new ImmutableCard(card.getName(), card.getId(), card.getTitle(), card.getDescription()));
        onCompletion.accept((List<Integer>) view.manageChoice(new GameMessage(MethodHeading.CARDS.ordinal(), false, Arrays.asList(clientCards, numberOfSelections)), playerId).getData().get(0));
    }

    /**
     * Brief Serializes the game board and sends it to the view
     * @param board is the game board
     * @param validPositions are the positions where to move, build or place a worker
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBoardChanged(Cell[][] board, List<Position> validPositions) {
        Object[][] serializableBoard = new Object[GameMap.getDefaultHeight()][GameMap.getDefaultWidth()];
        List<ImmutablePosition> positions = null;
        if(validPositions != null) {
            positions = new ArrayList<>();
            for (Position p : validPositions)
                positions.add(new ImmutablePosition(p.getX(), p.getY()));
        }
        for (int i = 0; i < GameMap.getDefaultHeight(); i++) {
            for (int j = 0; j < GameMap.getDefaultWidth(); j++) {
                if(board[i][j].isOccupied())
                    serializableBoard[i][j] = Arrays.asList(board[i][j].getOccupant().getColor(), board[i][j].getOccupant().getGender().symbol(), board[i][j].getLevel());
                else
                    serializableBoard[i][j] = board[i][j].getLevel();
            }
        }
        view.sendBoard(serializableBoard, positions);
    }

    /**
     * Brief Asks the currently playing player to select a worker from the board
     * @param currentPlayerId is the id of the player asked for the selection
     * @param workers are the asked player's workers
     * @param onCompletion accepts the player's choice and sets his active worker
     */
    @Override
    public void workerSelection(Integer currentPlayerId, List<Worker> workers, Consumer<Worker> onCompletion) {
        Integer choice;
        List<Integer> correctInputs = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        for(int i = 0; i < workers.size(); i++) {
            correctInputs.add(i);
            genders.add(workers.get(i).getGender().symbol());
        }
        choice = (Integer) view.manageChoice(new GameMessage(MethodHeading.WORKER.ordinal(), false, Arrays.asList(correctInputs, genders)), currentPlayerId).getData().get(0);
        onCompletion.accept(PlayersHandler.getCurrentPlayer().getWorkers().get(choice));
    }

    /**
     * Brief Asks the currently playing player to choose a position where to place a worker
     * @param currentPlayerId is the id of the player who has to choose
     * @param allowedPositions contains all the available positions on the map
     * @param turnPhase specify the kind of operation the player is performing
     * @param currentPosition is the current position of the selected worker
     * @param onCompletion places the worker on the cell at the chosen position
     */
    @Override
    public void positionSelection(Integer currentPlayerId, List<Position> allowedPositions, String turnPhase, Position currentPosition, Consumer<Position> onCompletion) {
        List<ImmutablePosition> positions = new ArrayList<>();
        ImmutablePosition selectedPosition, currentImmPosition = null;
        for(Position p : allowedPositions)
            positions.add(new ImmutablePosition(p.getX(), p.getY()));
        if(currentPosition != null) 
            currentImmPosition = new ImmutablePosition(currentPosition.getX(), currentPosition.getY());
        selectedPosition = (ImmutablePosition) view.manageChoice(new GameMessage(MethodHeading.POSITION.ordinal(), false, Arrays.asList(positions, turnPhase, currentImmPosition)), currentPlayerId).getData().get(0);
        onCompletion.accept(new Position(selectedPosition.getX(), selectedPosition.getY()));
    }

    /**
     * Brief Asks the currently playing player to select a block type when more than one are available
     * @param currentPlayerId is the id of the player who has to choose
     * @param blockTypes are the types of blocks available for the choice
     * @param onCompletion accepts the selected type of block in order to build it on the board
     */
    @Override
    public void blockTypeSelection(Integer currentPlayerId, List<BlockType> blockTypes, Consumer<BlockType> onCompletion) {
        List<Integer> correctInputs = new ArrayList<>();
        for (BlockType blockType : blockTypes) correctInputs.add(blockType.ordinal());
        correctInputs = correctInputs.stream().sorted(Integer::compareTo).collect(Collectors.toList());
        Integer choice = (Integer) view.manageChoice(new GameMessage(MethodHeading.BLOCK.ordinal(),
                false, Collections.singletonList(correctInputs)), currentPlayerId).getData().get(0);
        onCompletion.accept(BlockType.typeByLevel(correctInputs.get(choice)));
    }

    @Override
    public void executingGameOperation(String gameOperation) {

    }

    /**
     * Brief Asks the currently playing player if he wants to skip the next optional operation(s)
     * @param currentPlayerId is the player who's currently playing
     * @param operationType is the type of operation the player will decide to skip
     * @param onCompletion accepts the player's choice
     */
    @Override
    public void areOptionalOperationsSkipped(Integer currentPlayerId, String operationType, Consumer<Boolean> onCompletion) {
        onCompletion.accept((Boolean) view.manageChoice(new GameMessage(MethodHeading.SKIP.ordinal(), false, Collections.singletonList(operationType)), currentPlayerId).getData().get(0));
    }

    /**
     * Brief Sends a player the victory message, both if he wins and if he loses
     * @param playerId is the receiving player's id
     * @param message is the message to send
     * @param gameOver is true if game is over
     */
    @Override
    public void onVictoryCondition(Integer playerId, String message, Boolean gameOver) {
        view.printVictoryMessage(playerId, message, gameOver);
    }

    /**
     * Brief Communicates to the player which is the worker he selected in order to distinguish it on the board
     * @param worker is the worker chosen for the current turn
     */
    @Override
    public void onActiveWorker(Worker worker) {
        view.updateActiveWorker(new ImmutablePosition(worker.getPosition().getX(), worker.getPosition().getY()));
    }

    /**
     * Brief Sends to the view the chosen cards
     * @param cards is a list of chosen cards
     */
    @Override
    public void onCardsSelected(List<Card> cards) {
        List<ImmutableCard> immutableCards = new ArrayList<>();
        for(Card card : cards)
            immutableCards.add(new ImmutableCard(card.getName(), card.getId(), card.getTitle(), card.getDescription()));
        view.updateCards(immutableCards);
    }

    /**
     * Brief Notifies the players the end of the current turn
     */
    @Override
    public void onTurnOver() {
        view.resetParameters();
    }

    /**
     * Brief Requests the current player to undo his whole turn or to confirm it
     * @param onCompletion accepts the player's choice
     */
    @Override
    public void requestUndoOrConfirm(Consumer<Boolean> onCompletion) {
        GameMessage answer = view.manageChoice(new GameMessage(MethodHeading.UNDO.ordinal(), false), PlayersHandler.getCurrentPlayer().getId());
        onCompletion.accept((Boolean) answer.getData().get(0));
    }
}
