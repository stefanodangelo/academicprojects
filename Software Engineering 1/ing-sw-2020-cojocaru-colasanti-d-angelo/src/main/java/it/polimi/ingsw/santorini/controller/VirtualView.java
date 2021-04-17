package it.polimi.ingsw.santorini.controller;

import it.polimi.ingsw.santorini.communication.*;
import it.polimi.ingsw.santorini.controller.exceptions.NoMessageWithSuchIdException;
import it.polimi.ingsw.santorini.controller.server.ServerNetworkHandler;
import it.polimi.ingsw.santorini.model.Player;
import it.polimi.ingsw.santorini.model.PlayersHandler;
import it.polimi.ingsw.santorini.model.utils.TurnMessage;

import java.util.*;

/**
 * Brief Class interacting with the server in order to return the expected values to the controller
 */
public class VirtualView {
    private final MessageReceiver receiver = new MessageReceiver();
    private final ServerNetworkHandler server = ServerNetworkHandler.getInstance();

    /**
     * Brief Creates a new VirtualView and sets the receiver reference in the server
     */
    public VirtualView() { server.setReceiver(receiver);}

    /**
     * @return the names of the players in game
     */
    protected List<String> getPlayersNames() {
        return server.getActiveNames();
    }

    /**
     * Brief Sends the active worker's position to each player
     * @param position is the active worker's position
     */
    public void updateActiveWorker(ImmutablePosition position){
        server.send(null, new GameMessage(true, Collections.singletonList(position)));
    }

    /**
     * Brief Sends the serialized board to each client together with a list of positions, then waits for a receiving confirmation from every client
     * @param serializedBoard is a serialized board containing all the relevant information
     * @param allowedPositions are the positions where the current playing player can move or place his worker or build a block
     */
    public void sendBoard(Object[][] serializedBoard, List<ImmutablePosition> allowedPositions) {
        server.send(null, new GameMessage(MethodHeading.BOARD.ordinal(), true, Arrays.asList(serializedBoard, allowedPositions)));
    }

    /**
     * Brief Sends a poll to each player whom id are contained in a map passed as a parameter
     * @param playersNames is a map between the ids of the players who are asked to answer the poll and their names
     * @param question is the poll's question
     * @return the list of players' ids voted by each player
     */
    public List<Integer> getVote(Map<Integer, String> playersNames, String question) {
        List<Integer> votedIds = new ArrayList<>();
        GameMessage answer;
        server.send(null, new GameMessage(MethodHeading.VOTE.ordinal(), true, Arrays.asList(playersNames, question)));
        for(Integer id : playersNames.keySet()) {
            answer = waitAnswer(id);
            votedIds.add((Integer) answer.getData().get(0));
            if(!id.equals(playersNames.size()))   //if the player's id is not the last one of the list
                server.send(id, new LobbyMessage(NetworkMessage.waitVote.ordinal()));
        }
        return votedIds;
    }

    /**
     * Brief Sends to a player his result in the match, then disconnects him from the server or kick him if the game isn't over yet
     * @param playerId is the id of the receiving player
     * @param message is the message containing the result of the match
     * @param gameOver is true if game is over
     */
    public void printVictoryMessage(Integer playerId, String message, Boolean gameOver) {
        if(gameOver != null){
            if (gameOver) {
                server.send(playerId, new WinMessage(new TextMessage(message)));
                for(Player p : PlayersHandler.getPlayers())
                    if(!p.getId().equals(playerId))
                        server.send(p.getId(), new WinMessage());
                for(Player p : PlayersHandler.getPlayers())
                    waitAnswer(p.getId());
                server.reloadLobby();
            }
            else
                server.send(playerId, new KickMessage());
        } else server.send(playerId, new TextMessage(message));
    }

    /**
     * Brief Sends to each client a list conatining the chosen cards during the setup phase
     * @param cards are the chosen cards
     */
    public void updateCards(List<ImmutableCard> cards){
        server.send(null, new GameMessage(true, Collections.singletonList(cards)));
    }

    /**
     * Brief Resets any parameter, for each player, when a turn ends
     */
    public void resetParameters() {
        server.send(null, new GameMessage(true, null));
    }

    /**
     * Brief Waits for an answer from a certain player
     * @param playerId is the id of the player who presumably sent the message
     * @return the message received from the specified player
     */
    private GameMessage waitAnswer(Integer playerId){
        GameMessage message;
        do{
            try {
                message = receiver.getMessage(playerId);
            } catch (NoMessageWithSuchIdException e) {
                message = null;
            }
        } while (message == null);
        return message;
    }

    /**
     * Brief Handles a general choice by sending a message to a certain player, telling the other ones to wait their turn and waiting for the player's answer
     * @param message is the message to send to the player
     * @param playerId is the id of the receiving player
     * @return a message containing the player's answer
     */
    protected GameMessage manageChoice(GameMessage message, Integer playerId){
        server.send(playerId, message);
        server.sendToWaitingPlayers(new LobbyMessage(NetworkMessage.waitChoice.ordinal()), playerId);
        GameMessage answer = waitAnswer(playerId);
        if(answer.getData().get(0).equals(-1))
            answer = manageChoice(message, playerId);
        return answer;
    }
}
