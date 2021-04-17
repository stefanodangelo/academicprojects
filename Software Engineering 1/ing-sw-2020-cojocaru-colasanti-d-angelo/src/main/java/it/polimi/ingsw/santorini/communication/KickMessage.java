package it.polimi.ingsw.santorini.communication;


/**
 * Brief Message exchanged when the game isn't over but there's a loser who must be kicked out from the game
 */
public class KickMessage extends SerializableMessage  {
    private static final String message = "You've been kicked out of the game";

    public static String getMessage() {
        return message;
    }
}
