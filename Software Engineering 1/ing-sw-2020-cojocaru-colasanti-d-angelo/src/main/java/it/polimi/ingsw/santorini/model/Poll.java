package it.polimi.ingsw.santorini.model;

import java.util.ArrayList;

/**
 * Brief Class used to choose the first player
 */
public class Poll {
    private final String question;
    private final Integer[] leaderboard;

    @SuppressWarnings("ConstantConditions")
    public Poll(Integer size, String question){
        this.question = question;
        leaderboard = new Integer[size];
        for(int i = 0; i < size; i++)
            leaderboard[i] = 0;
    }

    /**
     * Brief Method responsible of the election of the "challenger", that is the most "god-like" player
     * @param playerId is the voted player's id
     * @param players is the list of all the players in game
     */
    public void vote(Integer playerId, ArrayList<Player> players){
        leaderboard[players.indexOf(PlayersHandler.getPlayerById(playerId))]++;
    }

    /**
     * @param players is the list of the players in game
     * @return the player who got the maximum number of votes
     */
    public Player getLeader(ArrayList<Player> players){
        int max = 0;
        for(int i = 1; i < leaderboard.length; i++)
            if(leaderboard[i] > leaderboard[max])
                max = i;
        return players.get(max);
    }

    public String getQuestion() {
        return question;
    }

    public Integer[] getLeaderboard() {
        return leaderboard;
    }

    @Override
    public String toString() {
        StringBuilder leaderboardStringBuilder = new StringBuilder();

        for(int i = 0; i < leaderboard.length; i++){
            leaderboardStringBuilder.append("player's id at ")
                                    .append(i)
                                    .append("= ")
                                    .append(leaderboard[i])
                                    .append("\n");
        }

        return "Poll{" +
                "question='" + question +
                "leaderboard= " +
                leaderboardStringBuilder +
                '}';
    }
}
