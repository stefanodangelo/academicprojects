package it.polimi.ingsw.santorini.communication;

/**
 * Brief Message containing a quit request and the consequent answer
 */
public class QuitMessage extends SerializableMessage  {
    private static final String request = "quit";
    private static final String answer = "A player quit, ending the game...";
    private static final String commandDescription = "close application";

    private boolean gameOver;

    public QuitMessage(){}

    /**
     * Brief Constructs a quit message used in case of victory
     * @param gameOver is true if there's a winner, false otherwise
     */
    public QuitMessage(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public static String getRequest(){ return request; }

    public static String getAnswer() {
        return answer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public static String getCommandDescription() {
        return commandDescription;
    }
}
