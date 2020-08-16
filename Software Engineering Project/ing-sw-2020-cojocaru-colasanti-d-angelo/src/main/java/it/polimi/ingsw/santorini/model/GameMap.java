package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.exceptions.*;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Brief Class that represents the map and includes game map logic related to insertion and removals of workers or buildings on particular tiles identified via position's coordinates
 */
public class GameMap {
    private static final Integer defaultWidth = 5;
    private static final Integer defaultHeight = 5;
    private static final Cell[][] board = new Cell[defaultHeight][defaultWidth];

    public GameMap() {
        for(int i = 0; i < defaultHeight; i++)
            for(int j = 0; j < defaultWidth; j++)
                board[i][j] = new Cell();
    }

    public static Cell[][] getBoard() {
        return board.clone();
    }

    public void setCell(Cell cell, Position position) {
        board[position.getX()][position.getY()] = cell;
    }

    /**
     * Brief This functions automatically generates the next Block to be built on the given Cell and pushes it, except if a Dome has already
     *       been built on the Cell
     * @param position is the position of the Cell
     * @return true if the Block has been successfully built
     */
    public Boolean buildNextLevel(Position position) {
        Cell chosenCell = cellAt(position);
        Block blockInConstruction;
        if(chosenCell.isBlocked()) return false;
        if(chosenCell.getLevel().equals(3)){  //a Dome is to be built
            if (chosenCell.isOccupied()) return false; //a Worker cannot build a Dome underneath itself
            blockInConstruction = new Block(BlockType.DOME);
        }
        else blockInConstruction = new Block(chosenCell.getLevel() + 1);
        blockInConstruction.setPosition(position);
        if(chosenCell.isOccupied()){ //the Worker on this cell is building underneath itself
            GameObject buildingWorker = chosenCell.popGameObject();
            chosenCell.pushGameObject(blockInConstruction);
            chosenCell.pushGameObject(buildingWorker);
        }
        else
            chosenCell.pushGameObject(blockInConstruction);
        return true;
    }

    /**
     * Brief adds a GameObject on a Cell at a certain position and deletes from allowedPositions if it is invoked during setup
     * @param gameObject is the object to add
     * @param position is the Cell's position
     * @return true if the object gets placed correctly, else false
     * @throws IncorrectBlockTypeException if the block trying to be pushed doesn't follow the correct building order
     */
    public Boolean addGameObject(GameObject gameObject, Position position) throws IncorrectBlockTypeException{
        Cell chosenCell = cellAt(position);
        if(!chosenCell.isReachable())
            return false;
        if((gameObject.correspondingLevel() != null && !gameObject.blocksCell() && !gameObject.correspondingLevel().equals(chosenCell.getLevel() + 1)))
            throw new IncorrectBlockTypeException();
        cellAt(position).pushGameObject(gameObject);
        gameObject.setPosition(position);
        return true;
    }

    /**
     * Brief moves a GameObject from the Cell 'from' to the Cell 'to'
     * @param from is the position of the initial Cell
     * @param to is the position of the Cell the GameObject is being moved to
     * @return true if the GameObject can perform the movement, otherwise false
     * @throws OutOfBoundsException if 'from' is an incorrect Position
     * @throws SamePositionException if 'to' is equal to 'from'
     */
    public Boolean move(Position from, Position to) throws OutOfBoundsException, SamePositionException {
        GameObject g;
        if(!isWithinGrid(from) || !isWithinGrid(to)) throw new OutOfBoundsException();
        else if(from.equals(to)) throw new SamePositionException();
        if (checkMove(from, to)) {
            try{g = cellAt(from).popGameObject();}
            catch (EmptyCellException e) {return false;}
            addGameObject(g, to);
            return true;
        }
        return false;
    }

    public static Integer getDefaultWidth() {
        return defaultWidth;
    }

    public static Integer getDefaultHeight() {
        return defaultHeight;
    }

    /**
     * Brief Checks if a GameObject can move from a position at a certain level to a destination with a given level
     * @param initial is the position of the Cell the GameObject is moving from
     * @param destination is the position of the destination Cell
     * @return true if destination's level is lower, equal or at max one higher than initial's position and hasn't a worker or a dome on top of it
     */
    public Boolean checkMove(Position initial, Position destination) {
        Cell destinationCell = cellAt(destination);
        return (destinationCell.getLevel() <= (cellAt(initial).getLevel() + 1)) && destinationCell.isReachable();
    }

    /**
     * Brief checks if the coordinates of the Cell are within the board
     * @param position is the position to control
     * @return true if the position is within the board
     */
    private static Boolean isWithinGrid(Position position){
        return !((position.getX() < 0) || (position.getY() < 0) || (position.getX() >= defaultWidth) || (position.getY() >= defaultHeight));
    }

    /**
     * Brief returns an arrayList of the Positions on the board accessible to a Worker on the Cell in position
     * @param position is the initial position from which a Worker is to be moved
     * @return the arrayList of accessible Positions
     */
    public ArrayList<Position> getValidDestinations(Position position){
        ArrayList<Position> reachablePositions = getReachablePositions(position);
        reachablePositions.removeIf(p -> !checkMove(position, p));
        return reachablePositions;
    }

    /**
     * Brief Returns the cell at the wanted position
     * @param position is the chosen position
     * @return the corresponding Cell
     */
    public Cell cellAt(Position position){
        return board[position.getX()][position.getY()];
    }

    /**
     * Brief Checks all the neighboring cells without a Dome on top or not occupied by a Worker
     * @param position is the Worker's position around which the method analyzes the unoccupied positions
     * @return an ArrayList of unoccupied positions
     */
    public ArrayList<Position> getReachablePositions(Position position){
        Integer x = position.getX();
        Integer y = position.getY();
        ArrayList<Position> unoccupiedPositions = new ArrayList<>();
        for(int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++) {
                Position p = new Position(i, j);
                if (!p.equals(position) && isWithinGrid(p) && cellAt(p).isReachable())
                    unoccupiedPositions.add(p);
            }
        return unoccupiedPositions;
    }

    /**
     * Brief Returns all the perimetral positions
     * @return a list of positions
     */
    public List<Position> perimeterSpace() {
        List<Position> positions = new ArrayList<>();
        for(int i = 0; i < defaultHeight; i++)
            for(int j = 0; j < defaultWidth; j++)
                if(i == 0 || i == defaultHeight - 1 || j == 0 || j == defaultWidth - 1)
                    positions.add(new Position(i, j));
        return positions;
    }

    /**
     * Brief setter for the floatingObject
     * @param floatingObject the floating object
     * @param position is the position of the object
     */
    public void floatObject(GameObject floatingObject, Position position) {
        cellAt(position).floatObject(floatingObject);
        floatingObject.setPosition(position);
    }

    /**
     * Brief Checks if there's a position in the map on the same direction of the two positions passed to the function
     * @param origin origin position
     * @param destination destination position
     * @return the position (if exists in the map) straight backwards the destination, otherwise null
     */
    public static Position getBackwardsPosition (Position origin, Position destination) {
        int dx = destination.getX() - origin.getX(), dy = destination.getY() - origin.getY();
        dx += dx != 0 ? (dx > 0 ? 1 : -1) : 0;
        dy += dy != 0 ? (dy > 0 ? 1 : -1) : 0;
        Position backwards = new Position(origin.getX() + dx, origin.getY() + dy);
        return isWithinGrid(backwards) ? backwards : null;
    }

    public static List<Position> getNeighboringPositions(Position position){
        List<Position> neighbouringPositions = new ArrayList<>();
        Integer x = position.getX(), y = position.getY();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                Position p = new Position(i, j);
                if (isWithinGrid(p)) neighbouringPositions.add(p);
            }
        }
        return neighbouringPositions;
    }

    public Integer getLevelDifference(Position pos1, Position pos2) {
        return cellAt(pos1).getLevel() - cellAt(pos2).getLevel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Arrays.equals(board, GameMap.board);
    }

    @Override
    public String toString() {
        return "GameMap{" +
                "matrix=" + Arrays.toString(board) +
                '}';
    }
}
