package it.polimi.ingsw.santorini.communication;

import it.polimi.ingsw.santorini.controller.server.ServerNetworkHandler;

import java.io.Serializable;
import java.util.List;

/**
 * Brief Message exchanged during the lobby phase or when it's necessary to send just a textual message
 */
public class LobbyMessage extends SerializableMessage  {
    private final Integer id;
    private final Integer textualMessageId;
    private final List<Object> data;
    private static final Integer hostId = ServerNetworkHandler.getHostId();

    /**
     * Brief Constructs a message intended to be used to print a message on the view
     * @param textualMessageId is the id of the textual message to print
     */
    public LobbyMessage(Integer textualMessageId){
        this.textualMessageId = textualMessageId;
        id = null;
        data = null;
    }

    /**
     * Brief Constructs a message that will be used to call a specific method on the view basing on the id passed as a parameter
     * @param id is the id of the method to call
     * @param data is the sensible content of the message
     */
    public LobbyMessage(Integer id, List<Object> data){
        this.id = id;
        textualMessageId = null;
        this.data = data;
    }

    public Integer getTextualMessageId() {
        return textualMessageId;
    }

    public Integer getId() {
        return id;
    }

    public static Integer getHostId() {return hostId;}

    public List<Object> getData() {
        return data;
    }
}
