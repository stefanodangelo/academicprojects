package it.polimi.ingsw.santorini.model;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Brief Bridge between SantoriniGame and the Controller
 *       It's responsible for the three main parts of the game: -setup
 *                                                              -move and build
 *                                                              -rendering
 */
public interface GameDelegate {
    GameMode requestGameMode(Integer playerId);
    void requestPlayersNames(Consumer<List<String>> onCompletion);
    void requestPlayerColor(Integer playerId, Consumer<String> onCompletion);
    void sendPoll(List<Integer> playersIds, Poll poll, Consumer<List<Integer>> onCompletion);
    void requestFirstPlayerChoice(Integer playerId, ArrayList<Player> players, Consumer<Integer> onCompletion);
    void requestCardsSelection(Integer playerId, ArrayList<Card> cards, Integer numberOfSelections, Consumer<List<Integer>> onCompletion);
    void onBoardChanged(Cell[][] board, List<Position> validPositions);

    // Game operations:
    void workerSelection(Integer currentPlayerId, List<Worker> workers, Consumer<Worker> onCompletion);
    void positionSelection(Integer currentPlayerId, List<Position> allowedPositions, String turnPhase, Position currentPosition, Consumer<Position> onCompletion);
    void blockTypeSelection(Integer currentPlayerId, List<BlockType> blockTypes, Consumer<BlockType> onCompletion);
    void executingGameOperation(String gameOperation);
    void areOptionalOperationsSkipped(Integer currentPlayerId, String operationType, Consumer<Boolean> onCompletion);
    void onVictoryCondition(Integer playerId, String message, Boolean gameOver);
    void onActiveWorker(Worker worker);
    void onCardsSelected(List<Card> cards);
    void onTurnOver();

    // Undo

    /**
     * Brief called when turn is completed to ask for confirmation or undo
     * @param onCompletion is the Lambda with the confirmation of the move
     */
    void requestUndoOrConfirm(Consumer<Boolean> onCompletion);
}
