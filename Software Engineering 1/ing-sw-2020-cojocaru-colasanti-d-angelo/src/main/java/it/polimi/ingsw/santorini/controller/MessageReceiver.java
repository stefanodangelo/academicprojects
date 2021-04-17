package it.polimi.ingsw.santorini.controller;

import it.polimi.ingsw.santorini.communication.GameMessage;
import it.polimi.ingsw.santorini.controller.exceptions.NoMessageWithSuchIdException;

import java.util.ArrayList;
import java.util.List;

/**
 * Brief Class implementing a buffer containing all the message received from the server
 */
public class MessageReceiver{
    private final List<GameMessage> messageBuffer = new ArrayList<>();

    /**
     * Brief Adds the received message to the buffer
     * @param message is the message sent by a certain player
     */
    public void receive(GameMessage message){
        messageBuffer.add(message);
    }

    /**
     * @param id is the sender's id
     * @throws NoMessageWithSuchIdException if no message marked with that id was found
     * @return the message in the buffer sent by the id passed as a parameter to the method, otherwise null
     */
    GameMessage getMessage(Integer id){
        for(int i = 0; i < messageBuffer.size(); i++) {
            GameMessage message = messageBuffer.get(i);
            if (message.getId().equals(id)) {
                messageBuffer.remove(message);
                return message;
            }
        }
        throw new NoMessageWithSuchIdException();
    }
}

