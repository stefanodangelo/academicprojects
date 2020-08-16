package it.polimi.ingsw.santorini.view.cli;

import it.polimi.ingsw.santorini.communication.ImmutableCard;
import it.polimi.ingsw.santorini.communication.QuitMessage;
import it.polimi.ingsw.santorini.communication.TextMessage;
import it.polimi.ingsw.santorini.view.Color;
import it.polimi.ingsw.santorini.view.View;
import it.polimi.ingsw.santorini.view.exceptions.InterruptionException;
import it.polimi.ingsw.santorini.view.network.ClientNetworkHandler;
import it.polimi.ingsw.santorini.view.utils.UtilityMessage;
import it.polimi.ingsw.santorini.view.utils.SelectionMessage;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Brief Reader class for the CLI that saves the input in the memory once read
 */
public class CLIReader {
    private volatile String userInput;
    private final Object lock = new Object();
    private Thread readingThread;

    /**
     * Brief Creates a new reader, then starts a thread that continuously reads the user input
     */
    public CLIReader(){
        startReading();
    }

    /**
     * Brief Starts the reading process by creating a new Thread
     */
    public void startReading(){
        readingThread = new Thread(this::readInput);
        readingThread.start();
    }

    /**
     * Brief Interrupts the reading process checking if the interruption happens when a timer expired or not
     * @param onRestart is true if the interruption is for a restart process
     */
    protected void interruptReading(boolean onRestart){
        userInput = UtilityMessage.interruptionCommand;
        readingThread.interrupt();
        if(!onRestart) startReading();
    }

    /**
     * Brief Reads any input checking if it was requested by the server or not
     */
    private void readInput() {
        InterruptibleInputStream input = new InterruptibleInputStream(System.in);
        while(true){
            if (!isInputDeleted() && !View.ViewStatus.isBusy)
                handleNotRequestedInput();
            try {
                StringBuilder sb = new StringBuilder();
                String c;
                do {
                    c = Character.toString(input.read());
                    sb.append(c);
                } while (!c.equals("\n"));
                sb.deleteCharAt(sb.length()-1);
                userInput = sb.toString();
                synchronized (lock){
                    lock.notifyAll();
                }
            } catch (IOException e) {
                View.ViewStatus.isInterrupted = true;
                synchronized (lock){
                    lock.notifyAll();
                }
                break;
            }
        }
    }

    /**
     * Brief Removes the blank spaces at the end of a certain string
     * @param s is the string to format
     * @return the string without any blank space at the end
     */
    private String removeBlankSpaces(String s) {
        if(s.contains(" ")){
            return s.substring(0, s.indexOf(" "));
        }
        return s;
    }

    /**
     * Brief Handles any eventual input that has been entered while not explicitly requested by the server
     */
    private void handleNotRequestedInput() {
        try {
            //noinspection ConstantConditions
            if (!isUtilityInput()) {
                ClientNetworkHandler.asyncWrite(new TextMessage(userInput));
                deleteInput();
            }
        } catch (NullPointerException | InterruptionException ignored){
        }
    }

    /**
     * Brief Asks the user to type something, then checks if the input is correct (in format and value)
     * @param messageToDisplay is the message to send to the player
     * @param correctInputs are all the possible correct inputs
     * @return the correct input
     */
    @SuppressWarnings("ConstantConditions")
    Integer getCorrectInput(String messageToDisplay, List<Integer> correctInputs) {
        Integer correctInput = 0;
        View.ViewStatus.lockStatus();
        CLIPrinter.print(messageToDisplay);
        waitInput();
        AtomicReference<String> formattedInput = new AtomicReference<>(removeBlankSpaces(userInput));
        userInput = formattedInput.get();
        try {
            if (isUtilityInput().equals(false)) {
                correctInput = parseInput(correctInputs);
                unlockInputStream();
                if (correctInput == null)
                    correctInput = getCorrectInput(messageToDisplay, correctInputs);
            }
            else {
                unlockInputStream();
                correctInput = getCorrectInput(messageToDisplay, correctInputs);
            }
        } catch (NullPointerException | InterruptionException ignored){
        }
        return correctInput;
    }

    /**
     * Brief Parses the input to check if it has a proper value, otherwise discards it
     * @param correctInputs is the list of Inputs to parse
     * @return the input's value or null if it is badly formatted or if its value doesn't belongs to the correct inputs
     */
    private Integer parseInput(List<Integer> correctInputs) {
        Integer input = null;
        try {
            input = Integer.valueOf(userInput);
            if (!correctInputs.contains(input)) {
                CLIPrinter.println(SelectionMessage.invalidChoiceMessage);
                input = null;
            }
        } catch (NumberFormatException e) {
            CLIPrinter.println(SelectionMessage.badInputMessage);
        }
        return input;
    }

    /**
     * Brief Verify if the user typed a string made of one character
     * @return the correct input
     */
    @SuppressWarnings("ConstantConditions")
    String getSingleCharInput(){
        String singleChar = null;
        View.ViewStatus.lockStatus();
        waitInput();
        AtomicReference<String> formattedInput = new AtomicReference<>(removeBlankSpaces(userInput));
        userInput = formattedInput.get();
        try{
            if(isUtilityInput().equals(false)){
                singleChar = new String(userInput.toLowerCase());
                unlockInputStream();
                if (singleChar.length() > 1) {
                    CLIPrinter.println(SelectionMessage.badInputMessage);
                    singleChar = getSingleCharInput();
                }
            } else{
                unlockInputStream();
                singleChar = getSingleCharInput();
            }
        } catch (NullPointerException | InterruptionException ignored){
        }
        return singleChar;
    }

    /**
     * Brief Unlocks the reading stream
     */
    void unlockInputStream(){
        View.ViewStatus.unlockStatus();
        deleteInput();
    }

    /**
     * Brief Waits until the user enters an input by synchronizing on a lock Object
     */
    private void waitInput(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Brief Checks if the input entered by the user concerns secondary "helping" commands like showing cards or rules or if it's a quit request
     * @return true if the input is one of the helping commands
     * @throws InterruptionException if the reading process has been interrupted for some reason
     */
    private Boolean isUtilityInput() throws InterruptionException {
        AtomicReference<String> formattedInput = new AtomicReference<>(removeBlankSpaces(userInput));
        userInput = formattedInput.get();
        if(userInput.equals(UtilityMessage.interruptionCommand))
            throw new InterruptionException();
        else if (userInput.equals(QuitMessage.getRequest())) {
            ClientNetworkHandler.asyncWrite(new QuitMessage());
            return null;
        } else if (View.ViewStatus.isSetupOver){
            switch (userInput) {
                case UtilityMessage.helpCommand:
                    CLIPrinter.println(UtilityMessage.textualRules);
                    CLIPrinter.updateMostBottomRow((int) UtilityMessage.textualRules.chars().filter(ch -> ch == '\n').count());
                    deleteInput();
                    break;
                case UtilityMessage.cardsEffectCommand:
                    for (ImmutableCard card : View.getCardsInGame())
                        CLIPrinter.println("\n" + Color.formatMessageColor(card.getName(), CLIView.getInputOptionsColor()) + " - " + card.getName() + " (" + card.getTitle() + ")" + ": " + "\n" + card.getDescription());
                    CLIPrinter.println("");
                    CLIPrinter.updateMostBottomRow((int) UtilityMessage.textualRules.chars().filter(ch -> ch == '\n').count() + 1);
                    deleteInput();
                    break;
            }
        }
        return isInputDeleted();
    }

    /**
     * Brief Reads a complete line from the input discarding it if it's empty
     * @return the read line or null if the input is empty
     */
    protected String readLine(){
        waitInput();
        try {
            //discards the void input and check if the input is not a command
            //noinspection ConstantConditions
            if (userInput.length() == 0 || isUtilityInput()) {
                deleteInput();
                return null;
            }
        } catch (NullPointerException e){
            return null;
        } catch (InterruptionException ignored){
        }
        String input = new String(userInput);
        deleteInput();
        return input;
    }

    /**
     * Brief Gets the player's choice for the undo functionality
     * @return true if the player decided to undo the turn, false otherwise
     */
    protected Boolean getUndoChoice(){
        while (true) {
            try {
                String input = readLine();
                if(input == null)
                    continue;
                if (input.equals(UtilityMessage.undoCommand))
                    return true;
                else if (input.equals(UtilityMessage.interruptionCommand))
                    return false;
                CLIPrinter.println(SelectionMessage.badInputMessage);
            } catch (NullPointerException ignored){
            }
        }
    }

    private void deleteInput(){
        userInput = null;
    }

    private boolean isInputDeleted(){
        return userInput == null;
    }
}
