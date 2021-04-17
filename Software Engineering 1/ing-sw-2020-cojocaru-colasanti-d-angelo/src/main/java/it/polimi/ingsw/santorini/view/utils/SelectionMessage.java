package it.polimi.ingsw.santorini.view.utils;

import it.polimi.ingsw.santorini.view.cli.CLIView;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.View;

/**
 * Brief Helper class containing all the useful messages and constants in the selection phase
 */
public class SelectionMessage {
    //defines
    public static final String CONFIRM = "y";
    public static final String DECLINE = "n";

    //messages
    public static final String gameModeSelectionMessage = "Select the mode you want to play by typing one of the following " + Color.formatMessageColor("integers", CLIView.getInputOptionsColor());
    public static final String usernameSelectionMessage = "Enter a username: ";
    public static final String colorSelectionMessage = "Select the color of your workers by typing one of the following " + Color.formatMessageColor("integers", CLIView.getInputOptionsColor());
    public static final String firstPlayerSelectionMessage = "Select the first player";
    public static final String cardSelectionMessage = "Select one card by typing one of the following " + Color.formatMessageColor("integers", CLIView.getInputOptionsColor());
    public static final String remainingSelections = "Remaining selections: ";
    public static final String workerSelectionMessage = "Choose a worker through its " + Color.formatMessageColor("integer", CLIView.getInputOptionsColor());
    public static final String invalidChoiceMessage = "Not valid choice. Type again.";
    public static final String badInputMessage = "Bad input. Type again.";
    public static final String blockTypeSelectionMessage = "Choose the " + Color.formatMessageColor("type", CLIView.getInputOptionsColor()) + " of block you want to build from the following";
    public static final String skipOperationQuestion = "Do you want to skip the next optional ";
    public static final String skipOperationChoices = "(" + Color.formatMessageColor(SelectionMessage.CONFIRM, CLIView.getInputOptionsColor()) + "\\" + Color.formatMessageColor(SelectionMessage.DECLINE, CLIView.getInputOptionsColor()) + ")";
    public static final String xCoordinateSelectionMessage = "X (" + Color.formatMessageColor("0", CLIView.getInputOptionsColor()) + " - " + Color.formatMessageColor(String.valueOf((View.getDefaultHeight() - 1)), CLIView.getInputOptionsColor()) + "): ";
    public static final String yCoordinateSelectionMessage = "Y (" + Color.formatMessageColor("0", CLIView.getInputOptionsColor()) + " - " + Color.formatMessageColor(String.valueOf((View.getDefaultWidth() - 1)), CLIView.getInputOptionsColor()) + "): ";
    public static final String undoRequestMessage = "Undo turn?";
    public static final String undoDirectionsMessage = "Type " + Color.formatMessageColor(UtilityMessage.undoCommand, CLIView.getInputOptionsColor()) + " to UNDO the whole turn or WAIT until the timer expires to confirm";
}
