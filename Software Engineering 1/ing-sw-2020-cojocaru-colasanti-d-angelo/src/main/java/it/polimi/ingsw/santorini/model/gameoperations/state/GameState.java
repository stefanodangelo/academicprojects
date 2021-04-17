package it.polimi.ingsw.santorini.model.gameoperations.state;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.state.immutable.ImmutableGameState;

import java.util.List;

/**
 * Brief Represents the current game state
 * @see SantoriniGame
 */
public final class GameState {

    /**
     * Brief The game map
     */
    private final GameMap map;

    /**
     * Brief The players
     */
    private final List<Player> players;

    /**
     * Brief The current player
     */
    private Player currentPlayer;

    /**
     * Brief The active worker (a special worker used by operations)
     */
    private Worker activeWorker;

    /**
     * Brief The active position (a special worker used by operations)
     */
    private Position activePosition;

    /**
     * Brief The game delegate
     */
    private final GameDelegate delegate;

    /**
     * Brief Constructor that sets the map, the players and the delegate
     * @param map The game map
     * @param players The players
     * @param delegate The game delegate
     */
    public GameState(GameMap map, List<Player> players, GameDelegate delegate) {
        this.map = map;
        this.players = players;
        this.delegate = delegate;
    }

    /**
     * Brief Setter for activeWorker
     * @param activeWorker Worker
     */
    public void setActiveWorker(Worker activeWorker) {
        this.activeWorker = activeWorker;
    }

    /**
     * Brief Setter for activePosition
     * @param activePosition Position
     */
    public void setActivePosition(Position activePosition) {
        this.activePosition = activePosition;
    }

    /**
     * Brief Setter for currentPlayer
     * @param currentPlayer Player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Brief Getter for game map
     * @return GameMap the map
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Brief Getter for game players
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Brief Getter for the current player
     * @return Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Brief Getter for the active worker
     * @return Worker
     */
    public Worker getActiveWorker() {
        return activeWorker;
    }

    /**
     * Brief Getter for the game delegate
     * @return GameDelegate
     */
    public GameDelegate getDelegate() {
        return delegate;
    }

    /**
     * Brief Getter for the active position
     * @return Position
     */
    public Position getActivePosition() {
        return activePosition;
    }
}
