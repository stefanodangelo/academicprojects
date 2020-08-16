package it.polimi.ingsw.santorini.model.gameoperations.state.immutable;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Brief Immutable implementation for storing purposes of GameState
 * @see GameState
 */
public final class ImmutableGameState {
    /**
     * Brief The game map
     */
    private final ImmutableGameMap map;

    /**
     * Brief The players
     */
    private final List<ImmutablePlayer> players;

    /**
     * Brief The current player
     */
    private final ImmutablePlayer currentPlayer;

    /**
     * Brief The active worker (a special worker used by operations)
     */
    private final ImmutableWorker activeWorker;

    /**
     * Brief The active position (a special worker used by operations)
     */
    private final ImmutablePosition activePosition;

    /**
     * Brief The game delegate
     */
    private final GameDelegate delegate;

    /**
     * Brief Creates a new ImmutableGameState copying the content of the state passed as a parameter
     * @param gameState is the state that is wanted to become immutable
     */
    public ImmutableGameState(GameState gameState) {
        this.map = new ImmutableGameMap(gameState.getMap());
        players = gameState.getPlayers().stream().map(ImmutablePlayer::new).collect(Collectors.toList());
        this.currentPlayer = new ImmutablePlayer(gameState.getCurrentPlayer());
        this.activeWorker = gameState.getActiveWorker() != null ? new ImmutableWorker(gameState.getActiveWorker()) : null;
        this.activePosition = gameState.getActivePosition() != null ? new ImmutablePosition(gameState.getActivePosition()): null;
        this.delegate = gameState.getDelegate();
    }

    public ImmutableGameMap getMap() {
        return map;
    }

    public List<ImmutablePlayer> getPlayers() {
        return players;
    }

    public ImmutablePlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public ImmutableWorker getActiveWorker() {
        return activeWorker;
    }

    public ImmutablePosition getActivePosition() {
        return activePosition;
    }

    public GameDelegate getDelegate() {
        return delegate;
    }

    /**
     * @return the mutable version of {this} ImmutableGameState
     */
    public GameState getMutable() {
        GameMap gameMap = map.getMutable();
        List<Player> players = new ArrayList<>(this.players).stream().map(ImmutablePlayer::getMutable).collect(Collectors.toList());
        Player currentPlayer = this.currentPlayer.getMutable();
        Worker activeWorker = this.activeWorker != null ? this.activeWorker.getMutable() : null;
        Position activePosition = this.activePosition != null ? this.activePosition.getMutable() : null;
        GameState gameState = new GameState(gameMap, players, this.delegate);
        gameState.setCurrentPlayer(currentPlayer);
        gameState.setActiveWorker(activeWorker);
        gameState.setActivePosition(activePosition);
        return gameState;
    }
}
