package it.polimi.ingsw.santorini.view.cli;

import it.polimi.ingsw.santorini.communication.ImmutablePosition;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.view.modelimport.BlockLevel;

import java.util.List;

/**
 * Brief Contains all the needed methods for the printing of the game board
 */
public class BoardPrinter {
    /**
     * Brief Parameter indicating the row where the board will be printed
     */
    private static int boardRow = 2;

    protected static int getBoardRow() {
        return boardRow;
    }

    public static void resetBoardRow() {
        boardRow = 2;
    }

    /**
     * Brief Prints the colored workersBoard together with others info
     * @param workersBoard contains all the workers placed on the board
     * @param buildingLevels are the levels of all the buildings in the map
     * @param validPositions are the optional positions to display
     */
    protected static void printBoard(String[][] workersBoard, Integer[][] buildingLevels, List<ImmutablePosition> validPositions){
        int col = boardRow, initialRow = boardRow, initialColumn = col, cellHeight = 5, cellWidth = 7;
        CLIPrinter.moveCursor(boardRow, col);
        for(int i = 0; i < workersBoard.length; i++){
            for(int j = 0; j < workersBoard[i].length; j++){
                String building;
                if(buildingLevels[i][j].equals(BlockLevel.GROUND.ordinal()))
                    building = null;
                else
                    building = Color.formatMessageColor(BlockLevel.values()[buildingLevels[i][j]].symbol(), Color.valueOf(BlockLevel.values()[buildingLevels[i][j]].color()));
                printCell(workersBoard[i][j], building, validPositions, new ImmutablePosition(i, j), boardRow, col, cellHeight, cellWidth);
                col += cellWidth - 1;
                CLIPrinter.moveCursor(boardRow, col);
            }
            col = initialColumn;
            boardRow += cellHeight - 1;
            CLIPrinter.moveCursor(boardRow, col);
        }
        smoothBorders(initialRow, initialColumn, workersBoard.length, workersBoard[0].length, cellHeight, cellWidth);
        if(!areAllWorkerPlaced(workersBoard))
            printCoordinates(workersBoard, initialRow, initialColumn, cellHeight, cellWidth);
        CLIPrinter.moveCursor(++boardRow, 0);
    }

    /**
     * Brief Prints the coordinates only for the workers' placement phase
     * @param workersBoard contains all the workers placed on the board
     * @param row is the row in the map
     * @param col is the column in the map
     * @param cellHeight is the chosen cellHeight
     * @param cellWidth is the chosen cellWidth
     */
    private static void printCoordinates(String[][] workersBoard, int row, int col, int cellHeight, int cellWidth) {
        int initialColumn = col;
        //print X coordinates
        row += cellHeight/2;
        col = workersBoard[0].length * cellWidth;
        CLIPrinter.moveCursor(row, col);
        for(int i = 0; i < workersBoard[0].length; i++){
            print(String.valueOf(i));
            row += cellHeight - 1;
            CLIPrinter.moveCursor(row, col);
        }
        print("X");
        //print Y coordinates
        row = workersBoard.length * cellHeight;
        col = initialColumn + cellWidth/2;
        CLIPrinter.moveCursor(row, col);
        for(int i = 0; i < workersBoard.length; i++){
            print(String.valueOf(i));
            col += cellWidth - 1;
            CLIPrinter.moveCursor(row, col);
        }
        CLIPrinter.moveCursor(row, col - 1);
        print("Y");
        if(row > boardRow)
            boardRow = row;
    }

    /**
     * @param workersBoard contains all the workers placed on the board
     * @return true if the game is still in placement phase
     */
    private static boolean areAllWorkerPlaced(String[][] workersBoard) {
        int numOfWorkers = 0;
        for (String[] strings : workersBoard) {
            for (String string : strings) {
                if (string != null)
                    numOfWorkers++;
            }
        }
        return numOfWorkers == View.getNumOfWorkers();
    }

    /**
     * Brief Prints a cell at a certain board's position
     * @param worker is the worker's symbol
     * @param building is the building's symbol
     * @param validPositions are the positions the worker can move to
     * @param cellPosition is the element of communication
     * @param row is the row in the map
     * @param col is the column in the map
     * @param height is the height of the map
     * @param width is the width of the map
     */
    private static void printCell(String worker, String building, List<ImmutablePosition> validPositions, ImmutablePosition cellPosition, int row, int col, int height, int width) {
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++) {
                if(isAtBound(i, height))
                    CLIPrinter.print(Border.DOUBLE_HORIZ_BORDER.symbol());
                else
                    printCellContent(worker, building, validPositions, cellPosition, row, col, i, j, height, width);
            }
            CLIPrinter.moveCursor(++row, col);
        }
    }

    /**
     * Brief Prints the right borders for the board just like it's being "smoothed"
     * @param row is the row in the map
     * @param col is the column in the map
     * @param width is the width of the map
     * @param height is the height of the map
     * @param rowInterval is the interval of rows to skip
     * @param columnInterval is the interval of columns to skip
     */
    private static void smoothBorders(int row, int col, int width, int height, int rowInterval, int columnInterval){
        int initialColumn = col;
        CLIPrinter.moveCursor(row, col);
        for(int i = 0; i <= height; i++){
            for(int j = 0; j <= width; j++){
                printBorders(i, j, width, height);
                col += columnInterval - 1;
                CLIPrinter.moveCursor(row, col);
            }
            row += rowInterval - 1;
            col = initialColumn;
            CLIPrinter.moveCursor(row, col);
        }
    }

    /**
     * Brief Prints the correct type of border basing on the original cell's position
     * @param i is the coordinate in the terminal
     * @param j is the coordinate in the terminal
     * @param width is the height of the map
     * @param height is the width of the map
     */
    private static void printBorders(int i, int j, int width, int height) {
        if(i == 0) {
            if (j == 0)
                print(Border.DOUBLE_UPPER_LEFT_CORNER.symbol());
            else if (j == width)
                print(Border.DOUBLE_UPPER_RIGHT_CORNER.symbol());
            else
                print(Border.UP_CROSSED_BORDER.symbol());
        }
        else if (i == height) {
            if (j == 0)
                print(Border.DOUBLE_LOWER_LEFT_CORNER.symbol());
            else if (j == width)
                print(Border.DOUBLE_LOWER_RIGHT_CORNER.symbol());
            else
                print(Border.DOWN_CROSSED_BORDER.symbol());
        }
        else{
            if(j == 0)
                print(Border.DOUBLE_LEFT_CROSSED_BORDER.symbol());
            else if(j == width)
                print(Border.DOUBLE_RIGHT_CROSSED_BORDER.symbol());
            else
                print(Border.CROSSED_BORDERS.symbol());
        }
    }

    /**
     * Prints the content of a single board's cell
     * @param worker is the worker's symbol
     * @param building is the building's symbol
     * @param validPositions are the positions where the worker can move to
     * @param cellPosition is the element of communication
     * @param row is the row in the map
     * @param col is the column in the map
     * @param i is the coordinate in the terminal
     * @param j is the coordinate in the terminal
     * @param height is the height of the map
     * @param width is the width of the map
     */
    private static void printCellContent(String worker, String building, List<ImmutablePosition> validPositions, ImmutablePosition cellPosition, int row, int col, int i, int j, int height, int width) {
        int centerX = height / 2, centerY = width / 2;

        setBackgroundColor(validPositions, cellPosition);
        if(isAtBound(j, width)) {
            print(Color.RESET);
            print(Border.CENTRAL_BORDER.symbol());
        }
        else if (i == centerX && j == centerY) {
            print(Color.RESET);
            if(building == null) {  //if there's no building
                setBackgroundColor(validPositions, cellPosition);
                print(BlockLevel.GROUND.symbol());
                printWorkerAt(worker, row, col + centerX + 1, col + centerX + 2);
            }
            else {
                print(building);
                printWorkerAt(worker, row, col + centerX, col + centerX + 2);
            }
        }
        else
            print(BlockLevel.GROUND.symbol());
    }

    /**
     * Brief Prints, if present, a worker at a certain position, then moves the cursor at the next free position to print
     * @param worker is the worker symbol
     * @param row is the row in the map
     * @param col is the column in the map
     * @param freeColumn is the first free column
     */
    private static void printWorkerAt(String worker, int row, int col, int freeColumn) {
        if(worker != null){
            print(Color.RESET);
            CLIPrinter.moveCursor(row, col);
            print(worker);
            CLIPrinter.moveCursor(row, freeColumn);
        }
    }

    /**
     * @param i is the coordinate
     * @param upperBound is the upperbound
     * @return @return true if a certain parameter's value is at the bounds of the interval [0, upperBound)
     */
    private static boolean isAtBound(int i, int upperBound) {
        return i == 0 || i == upperBound - 1;
    }


    /**
     * Brief Sets the background color of a single cell basing on the containment of its position in the list passed as a parameter to the function
     * @param validPositions are any eventual positions to highlight
     * @param position is the cell's position
     */
    private static void setBackgroundColor(List<ImmutablePosition> validPositions, ImmutablePosition position) {
        if (validPositions != null && validPositions.contains(position)) {
            if(position.equals(View.getActivePosition()))
                print(Color.BACKGROUND_CYAN.color());
            else
                print(Color.BACKGROUND_BLUE.color());
        }
        else {
            if (position.equals(View.getActivePosition()))
                print(Color.BACKGROUND_YELLOW.color());
            else
                print(Color.BACKGROUND_GREEN.color());
        }
    }

    /**
     * Brief Prints a textual message
     * @param message is the message to print
     */
    private static void print(String message){
        CLIPrinter.print(message);
    }
}
