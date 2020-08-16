package it.polimi.ingsw.santorini.communication;

/**
 * Brief Message containing a textual content and the value of a start request
 */
public class TextMessage extends SerializableMessage  {
    private final String content;
    private static final String startRequest = "start";

    public TextMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static String getStartRequest() {
        return startRequest;
    }
}
