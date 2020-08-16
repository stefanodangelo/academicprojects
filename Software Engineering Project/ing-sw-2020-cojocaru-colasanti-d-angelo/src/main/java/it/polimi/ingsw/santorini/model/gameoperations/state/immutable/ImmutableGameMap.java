package it.polimi.ingsw.santorini.model.gameoperations.state.immutable;

import it.polimi.ingsw.santorini.model.Cell;
import it.polimi.ingsw.santorini.model.GameMap;
import it.polimi.ingsw.santorini.model.Position;

/**
 * Brief Immutable implementation for storing purposes of GameMap
 * @see GameMap
 */
public final class ImmutableGameMap {

    private static final Integer defaultWidth = 5;
    private static final Integer defaultHeight = 5;
    private static final ImmutableCell[][] board = new ImmutableCell[defaultHeight][defaultWidth];

    /**
     * Brief Creates a new ImmutableGameMap copying the content of the map passed as a parameter
     * @param gameMap is the map that is wanted to become immutable
     */
    public ImmutableGameMap(GameMap gameMap) {
        for (int x = 0; x < defaultWidth; x++)
            for (int y = 0; y < defaultHeight; y++)
                board[x][y] = new ImmutableCell(gameMap.cellAt(new Position(x, y)));
    }

    public ImmutableCell[][] getBoard() {
        return board;
    }

    /**
     * @return the mutable version of {this} ImmutableGameMap
     */
    public GameMap getMutable() {
        GameMap gameMap = new GameMap();
        for (int x = 0; x < defaultWidth; x++)
            for (int y = 0; y < defaultHeight; y++){
                Cell cell = board[x][y].getMutable();
                gameMap.setCell(cell, new Position(x, y));
            }
        return gameMap;
    }
}
