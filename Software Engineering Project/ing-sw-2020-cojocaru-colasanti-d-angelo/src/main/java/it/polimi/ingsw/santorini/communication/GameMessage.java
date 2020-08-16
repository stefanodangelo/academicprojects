package it.polimi.ingsw.santorini.communication;

import java.util.List;

/**
 * Brief Serializable object representing the message exchanged between client and server which contains game data
 */
public class GameMessage extends SerializableMessage {
    /**
     * Brief Attribute containing a unique id for the method to call (client side) or the player's id (server side)
     */
    private final Integer id;
    private final boolean broadcast;
    private final List<Object> data;

    public GameMessage(Integer id, boolean broadcast, List<Object> data){
        this.id = id;
        this.broadcast = broadcast;
        this.data = data;
    }

    /**
     * Brief Creates a GameMessage which doesn't contain any useful data
     * @param id is the id of the message
     * @param broadcast indicates if it is a broadcast
     */
    public GameMessage(Integer id, boolean broadcast){
        this.id = id;
        this.broadcast = broadcast;
        this.data = null;
    }

    /**
     * Brief Creates a GameMessage which is not intended to be used for a method call on the client side
     * @param broadcast indicates if it is a broadcast
     * @param data is the List of Objects contained
     */
    public GameMessage(boolean broadcast, List<Object> data) {
        id = -1;
        this.broadcast = broadcast;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public List<Object> getData() {
        return data;
    }
}
