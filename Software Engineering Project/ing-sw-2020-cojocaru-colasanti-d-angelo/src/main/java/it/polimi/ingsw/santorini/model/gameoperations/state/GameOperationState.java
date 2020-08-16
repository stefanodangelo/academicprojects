package it.polimi.ingsw.santorini.model.gameoperations.state;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.result.GameOperationResult;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableRulesContainer;

import java.util.*;

/**
 * Brief Represents the state of a GameOperation
 * @see it.polimi.ingsw.santorini.model.gameoperations.GameOperation
 */
public abstract class GameOperationState {

    /**
     * Brief The game state
     */
    private GameState gameState;

    /**
     * Brief The operation result
     */
    private final GameOperationResult result = new GameOperationResult();

    /**
     * Brief The initial position of the active worker at the start of the operation
     */
    private Position initialPosition;

    /**
     * Brief The chosen position (useful for position selection phase of th operation)
     */
    private Position chosenPosition;

    /**
     * Brief Getter for the initial position of the active worker
     * @return Position
     */
    public Position initialPosition() {
        return initialPosition;
    }

    /**
     * Brief Setter for the initial position of the active worker
     * @param initialPosition Position
     */
    public void setInitialPosition(Position initialPosition) {
        this.initialPosition = initialPosition;
    }

    /**
     * Brief Getter for the active worker position
     * @return Position
     */
    public Position activeWorkerPosition() {
        return activeWorker().getPosition();
    }

    /**
     * Brief Getter for the chosen position of the active worker
     * @return Position
     */
    public Position chosenPosition() { return chosenPosition; }

    /**
     * Brief Setter for the chosen position of the active worker
     * @param chosenPosition Position
     */
    public void setChosenPosition(Position chosenPosition) {
        this.chosenPosition = chosenPosition;
    }

    /**
     * Brief Setter for the game state
     * @param gameState GameState
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


    /**
     * Brief Getter for the game delegate
     * @return GameDelegate
     */
    public GameDelegate delegate() {
        return gameState.getDelegate();
    }

    /**
     * Brief Getter for the result
     * @return GameOperationResult
     */
    public GameOperationResult result() {
        return result;
    }

    /**
     * Brief Getter for the game state
     * @return GameState
     */
    public GameState gameState() {
        return gameState;
    }

    /**
     * Brief Getter for the current player
     * @return Player
     */
    public Player currentPlayer() {
        return gameState().getCurrentPlayer();
    }

    /**
     * Brief Getter for the players
     * @return List of Player
     */
    public List<Player> players() {
        return gameState().getPlayers();
    }

    /**
     * Brief Getter for the map
     * @return GameMap
     */
    public GameMap map() {
        return gameState().getMap();
    }

    /**
     * Brief Getter for the expansion rules container
     * @return ExpandableRulesContainer
     */
    public ExpandableRulesContainer expansionRules() {
        return result().expansionRules();
    }

    /**
     * Brief Getter for the active worker
     * @return Worker
     */
    public Worker activeWorker() {
        return gameState.getActiveWorker();
    }

    /**
     * Brief Getter for the active position
     * @return Position
     */
    public Position activePosition() {
        return gameState.getActivePosition();
    }

    /**
     * Brief Getter for the inactive worker
     * @return Worker
     */
    public Worker inactiveWorker() {
        Set<Worker> workers = new HashSet<>(currentPlayer().getWorkers());
        workers.remove(gameState.getActiveWorker());
        return new ArrayList<>(workers).get(0);
    }

    /**
     * Brief Getter for the enemy players
     * @return List of Player
     */
    public List<Player> enemies() {
        Set<Player> players = new HashSet<>(players());
        players.remove(currentPlayer());
        return new ArrayList<>(players);
    }

    /**
     * Brief Provides a list of markers that targets all the enemies and has the current player as owner
     * @return List of GameMarker
     */
    public List<GameMarker> enemiesAsTarget() {
        return GameMarker.enemiesAsTarget(currentPlayer(), enemies());
    }

    /**
     * Brief Provides a marker that targets the current player
     * @return List of GameMarker
     */
    public GameMarker currentPlayerAsTarget() {
        return GameMarker.currentPlayerAsTarget(currentPlayer());
    }
}
