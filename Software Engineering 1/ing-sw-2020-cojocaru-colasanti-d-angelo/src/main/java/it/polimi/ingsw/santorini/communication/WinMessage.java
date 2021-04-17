package it.polimi.ingsw.santorini.communication;

/**
 * Brief Special message sent in case of win by some player
 */
public class WinMessage extends SerializableMessage{
    private final TextMessage textMessage;

    public WinMessage(){textMessage = null;}

    public WinMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }
}
