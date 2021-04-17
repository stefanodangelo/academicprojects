package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.exceptions.NoPlayerWithSuchColorException;
import it.polimi.ingsw.santorini.model.exceptions.NoPlayerWithSuchIdException;
import it.polimi.ingsw.santorini.view.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Brief Handles the turns of the game by means of a structure containing references to all the players
 */
public class PlayersHandler {
    private static final ArrayList<Player> players = new ArrayList<>();
    private static Integer currentPlayerIndex = 0;

    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getPlayers(){
        return (ArrayList<Player>) players.clone();
    }

    /**
     * Brief Removes a player and updates the currentPlayerIndex in order to let it be updated by the next call of next() method
     * @param player is the player to be removed
     */
    public static void removePlayer(Player player){
        players.remove(player);
        currentPlayerIndex--;
        if(currentPlayerIndex == -1) currentPlayerIndex = players.size() - 1;
        for(Player p : players)
            p.setId(players.indexOf(p) + 1);
    }

    /**
     * Brief Chooses the next player from players
     *       If current player is at the end of the ArrayList, the next one will be the first element of the ArrayList
     * @return the next player who must play
     */
    public Player next(){
        if (currentPlayerIndex.equals(players.size() - 1))  //if we reached the end of the List
            currentPlayerIndex = 0;
        else
            currentPlayerIndex++;
        return players.get(currentPlayerIndex);
    }

    /**
     * Brief Adds a player in the ArrayList containing all the players taking part in the game
     * @param player is the player who takes part in the game
     */
    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * @param id is the player's id
     * @return the player with the corresponding id or throws NoCardWithSuchIdException
     */
    public static Player getPlayerById(Integer id){
        for (Player p : players){
            if (id.equals(p.getId()))
                return p;
        }
        throw new NoPlayerWithSuchIdException();
    }

    public static Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public static void setCurrentPlayerIndex(Integer currentPlayerIndex) {
        PlayersHandler.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * @param color is the color of the player's workers
     * @return the player with the corresponding workers' colors or throws an exception
     */
    public Player getPlayerByColor(String color) {
        for(Player p : players){
            List<Worker> w = p.getWorkers();
            if(w != null && w.get(0).getColor().equals(color))
                return p;
        }
        throw new NoPlayerWithSuchColorException();
    }

    @Override
    public String toString() {
        StringBuilder playersStringBuilder = new StringBuilder();

        for(int i = 0; i < players.size(); i++){
            playersStringBuilder.append("player at ")
                                .append(i)
                                .append("= ")
                                .append(players.get(i).toString())
                                .append("\n");
        }
        return "PlayersHandler{" +
                "currentPlayerIndex='" + currentPlayerIndex + '\'' +
                "players= " +
                playersStringBuilder +
                '}';
    }
}
