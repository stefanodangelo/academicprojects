package it.polimi.ingsw.santorini.view.cli;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.communication.LobbyMessage;
import it.polimi.ingsw.santorini.communication.QuitMessage;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.view.modelimport.BlockLevel;
import it.polimi.ingsw.santorini.view.utils.SelectionMessage;
import it.polimi.ingsw.santorini.view.utils.UtilityMessage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Brief Class responsible for the printing of textual tables and the handling of the console's cursor's position
 */
public class CLIPrinter {
    private static final PrintStream outputStream = new PrintStream(System.out);
    /**
     * Brief Parameter indicating the row where the first group of tables will be printed
     */
    private int firstRow = 2;
    /**
     * Brief Parameter indicating the column where the first group of tables will be printed
     */
    private final int firstColumn = 40;
    /**
     * Brief Parameter indicating the row where the second group of tables will be printed
     */
    private int secondRow = 2;
    /**
     * Brief Parameter indicating the column where the second group of tables will be printed
     */
    private int secondColumn = firstColumn;
    /**
     * Brief Number of columns used to get the correct formatting for each table
     */
    private final int GENERAL_OFFSET = 10;
    private static int mostBottomRow;

    /**
     * Brief Moves the console cursor to a specific position
     * @param row is the y coordinate where to move the cursor
     * @param column is the x coordinate where to move the cursor
     */
    public static void moveCursor(int row, int column){
        char escCode = 0x1B;
        print(String.format("%c[%d;%dH",escCode,row,column));
    }

    /**
     * Brief Refreshes the screen
     */
    public static void cleanScreen(){
        println("\033[H\033[2J");
        mostBottomRow = 0;
    }

    /**
     * Brief Resets the printing rows to their initial values
     */
    private void resetRows(){
        BoardPrinter.resetBoardRow();
        firstRow = 2;
        secondRow = 2;
    }

    /**
     * Brief Resets the position of the screen cursor by positioning it at the first free row under all the printed objects
     */
    public void resetCursorPosition() {
        int tempRow = Math.max(BoardPrinter.getBoardRow(), Math.max(firstRow, secondRow)) + 1;
        if(tempRow > mostBottomRow)
            mostBottomRow = tempRow;
        moveCursor(mostBottomRow, 0);
        resetRows();
    }

    /**
     * Brief Re-initializes the value of secondColumn when it's known the size of the first table printed at (firstRow, firstColumn)
     * @param width is the size of the first printed table
     */
    protected void reinitializeSecondColumn(int width) {
        secondColumn = firstColumn + width + GENERAL_OFFSET + 4;
    }

    /**
     * Brief Prints the header
     * @param header is the header
     * @param dimension is the dimension
     * @param row is the row in the map
     * @param column is the column in the map
     * @return the first free row under the printed header
     */
    private int printHeader(String header, int dimension, int row, int column) {
        moveCursor(row, column);
        for(int i = 0; i < dimension + GENERAL_OFFSET; i++) {
            if(i == 0)
                print(Border.UPPER_LEFT_CORNER.symbol());
            else if(i == dimension + GENERAL_OFFSET - 1)
                print(Border.UPPER_RIGHT_CORNER.symbol());
            else
                print(Border.HORIZONTAL_BORDER.symbol());
        }
        moveCursor(++row, column);
        print(Border.VERTICAL_BORDER.symbol());
        moveCursor(row, column + (dimension + 5)/2);
        print(header);
        moveCursor(row, column + dimension + GENERAL_OFFSET - 1);
        print(Border.VERTICAL_BORDER.symbol());
        moveCursor(++row, column);
        for(int i = 0; i < dimension + GENERAL_OFFSET; i++) {
            if(i == 0)
                print(Border.LEFT_CROSSED_BORDER.symbol());
            else if(i == dimension + GENERAL_OFFSET - 1)
                print(Border.RIGHT_CROSSED_BORDER.symbol());
            else
                print(Border.HORIZONTAL_BORDER.symbol());
        }
        return row;
    }

    /**
     * Brief Prints the body
     * @param dimension is the dimension
     * @param row is the row in the map
     * @param column is the column in the map
     * @param tags is the List of Tags
     * @param values is the List of values
     * @return the first free row under the printed body
     */
    private int printBody(int dimension, int row, int column, List<String> tags, List<String> values){
        for(int i = 0; i < values.size(); i++){
            moveCursor(++row, column);
            print(Border.VERTICAL_BORDER.symbol() + " ");
            print(tags.get(i) + ": ");
            print(values.get(i));
            moveCursor(row, column + dimension + 5);
            print("    " + Border.VERTICAL_BORDER.symbol());
        }
        moveCursor(++row, column);
        for(int i = 0; i < dimension + GENERAL_OFFSET; i++) {
            if(i == 0)
                print(Border.LOWER_LEFT_CORNER.symbol());
            else if(i == dimension + GENERAL_OFFSET - 1)
                print(Border.LOWER_RIGHT_CORNER.symbol());
            else
                print(Border.HORIZONTAL_BORDER.symbol());
        }
        return row;
    }

    /**
     * Brief Prints a table in a certain position (row, column) on the screen
     * @param header indicates what the table is about
     * @param dimension is the width of the table
     * @param row is the column where to start printing the table
     * @param column is the column where to start printing the table
     * @param tags are the unique values' ids
     * @param tableValues are the actual values of the table
     * @return the first free row under the printed table
     */
    private int printTable(String header, int dimension, int row, int column, List<String> tags, List<String> tableValues){
        row = printHeader(header, dimension, row, column);
        row = printBody(dimension, row, column, tags, tableValues);
        return ++row;
    }

    /**
     * Brief Prints the list of players in game
     * @param names are the players' names
     */
    public void printPlayersList(String header, List<String> names){
        int longestNameLength = 0;
        List<String> tags = new ArrayList<>();
        //tags assignment
        for(String name : names) {
            if (name.length() > longestNameLength)
                longestNameLength = name.length();
            if(View.getPlayerId() > names.size())
                tags.add(String.valueOf(names.indexOf(name)+2));
            else {
                String tag = String.valueOf(names.indexOf(name)+1);
                if(LobbyMessage.getHostId().equals(names.indexOf(name)+1))
                    tag = Color.formatMessageColor(tag, Color.BLUE);
                tags.add(tag);
            }
        }
        //table printing
        secondRow = printTable(header, longestNameLength, secondRow, secondColumn, tags, names);
    }

    /**
     * Brief Prints the commands available in lobby for a certain player
     * @param header is the header of the table
     * @param tags are the commands that can be used by the player
     * @param values are the values contained in the table
     */
    public void printLobbyCommands(String header, List<String> tags, List<String> values){
        int width = 0;
        //tags assignment
        for(int i = 0; i < tags.size(); i++) {
            if (tags.get(i).length() + values.get(i).length() > width)
                width = tags.get(i).length() + values.get(i).length();
            tags.set(i, Color.formatMessageColor(tags.get(i), CLIView.getInputOptionsColor()));
        }
        //table printing
        firstRow = printTable(header, width, firstRow, firstColumn, tags, values);
        reinitializeSecondColumn(width);
    }

    /**
     * Brief Prints the helping commands
     * @param thereAreCards determines whether to show the cards command or not
     */
    protected void printUtilityCommands(boolean thereAreCards){
        int TABLE_DIMENSION = 17;
        List<String> tags = new ArrayList<>(Arrays.asList(Color.formatMessageColor(QuitMessage.getRequest(), CLIView.getInputOptionsColor()), Color.formatMessageColor(UtilityMessage.helpCommand, CLIView.getInputOptionsColor())));
        List<String> tableValues = new ArrayList<>(Arrays.asList("give up", "show rules"));
        if(thereAreCards) {
            tags.add(Color.formatMessageColor(UtilityMessage.cardsEffectCommand, CLIView.getInputOptionsColor()));
            tableValues.add("show cards");
        }
        resetRows();
        //table printing
        firstRow = printTable("Utility", TABLE_DIMENSION, firstRow, firstColumn, tags, tableValues);
        reinitializeSecondColumn(TABLE_DIMENSION);
    }

    /**
     * Brief Prints all the possible commands for the movement and building steps
     */
    protected void printCommands(){
        int centerPosition = firstColumn + GENERAL_OFFSET;
        //header printing
        moveCursor(++firstRow, firstColumn);
        print(TurnCommand.getHeader());
        //body printing
        printCommandSymbols(++firstRow, centerPosition);
        printCommandsValues(centerPosition);
    }

    /**
     * Brief Prints the turn's command's symbols
     * @param initialPosition is the first row after which the printing process starts
     * @param centerPosition is the positions where the commands will be printed
     */
    private void printCommandSymbols(int initialPosition, int centerPosition) {
        int index = 0;
        moveCursor(firstRow, centerPosition);
        for(TurnCommand command : TurnCommand.values()) {
            if (command.getSymbol() != null)
                print(command.getSymbol() + " ");
            else
                print("  ");
            if(++index == 3) {
                firstRow = firstRow + 2;
                moveCursor(firstRow, centerPosition);
                index = 0;
            }
        }
        firstRow = initialPosition + 1;
    }

    /**
     * Brief Prints the turn's command's values
     * @param centerPosition is the positions where the commands will be printed
     */
    private void printCommandsValues(int centerPosition) {
        int index = 0;
        moveCursor(firstRow, centerPosition + 1);
        for(TurnCommand command : TurnCommand.values()) {
            print(Color.formatMessageColor(command.getCommand(), CLIView.getInputOptionsColor()));
            if(++index == 3) {
                moveCursor(++firstRow, centerPosition + 1);
                index = 0;
            }
        }
    }

    /**
     * Brief Prints the list of blocks available in game
     */
    protected void printBlocksList(){
        int longestLength = 0;
        List<String> blocksNames = new ArrayList<>(), blocksSymbols = new ArrayList<>();
        //tags assignment
        for(BlockLevel blockLevel : BlockLevel.values()) {
            if(blockLevel.equals(BlockLevel.GROUND)) continue;
            if (blockLevel.getName().length() + blockLevel.symbol().length() > longestLength)
                longestLength = blockLevel.getName().length() + blockLevel.symbol().length();
            blocksNames.add(blockLevel.getName());
            blocksSymbols.add(Color.formatMessageColor(blockLevel.symbol(), Color.valueOf(blockLevel.color())));
        }
        //table printing
        secondRow = printTable("Blocks", longestLength, secondRow, secondColumn, blocksNames, blocksSymbols);
    }

    /**
     * Brief Prints a colored list of the cards in game
     * @param cards are the cards in game
     */
    protected void printCards(List<ImmutableCard> cards){
        int longestDimension = 0;
        List<String> cardsNames = new ArrayList<>();
        //tags assignment
        for (int i = 0; i < cards.size(); i++) {
            int newDimension = cards.get(i).getName().length() + View.getPlayersInGame().get(i).length();
            if (newDimension > longestDimension)
                longestDimension = newDimension;
            cardsNames.add(cards.get(i).getName());
        }
        //table printing
        secondRow = printTable("Cards", longestDimension, secondRow, secondColumn, View.getPlayersInGame(), cardsNames);
    }

    /**
     * Brief Prints a message on the screen without the carriage return symbol
     * @param message is the message to print
     */
    protected static void print(String message){
        outputStream.print(message);
    }

    /**
     * Brief Prints a message on the screen with the carriage return symbol and updates the value of the mostBottomRow
     * @param message is the message to print
     */
    protected static void println(String message){
        outputStream.println(message);
        int printedLines = (int) message.chars().filter(ch -> ch == '\n').count();
        if(printedLines == 0)
            mostBottomRow++;
        mostBottomRow += printedLines;
    }

    /**
     * Brief Prints the timer on the screen refreshing only the portion where it will be printed
     * @param time is the time to be printed
     */
    protected void printTime(Integer time) {
        resetCursorPosition();
        CLIPrinter.println(SelectionMessage.undoDirectionsMessage);
        CLIPrinter.println(SelectionMessage.undoRequestMessage + " REMAINING TIME: " + time);
        mostBottomRow -= 2;
    }

    /**
     * Brief Subtracts the given value to mostBottomRow's value
     * @param value is the number of rows to reset
     */
    public static void updateMostBottomRow(int value){
        mostBottomRow -= value;
    }
}
