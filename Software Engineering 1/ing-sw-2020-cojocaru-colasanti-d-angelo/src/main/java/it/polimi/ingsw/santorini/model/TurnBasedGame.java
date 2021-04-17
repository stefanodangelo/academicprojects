package it.polimi.ingsw.santorini.model;

import java.util.function.Consumer;

/**
 * Brief This interface contains four functions corresponding to the four main blocks of a turn based game: -setup
 *                                                                                                          -start
 *                                                                                                          -next turn
 *                                                                                                          -end
 */
public interface TurnBasedGame {
    void setupGame();
    void startGame();
    void handleTurn(Player player, Consumer<Boolean> onCompletion);
    /**
     * Brief Ends the game by removing the players in game
     * @throws RuntimeException to terminate the thread where the game is running
     */
    static void endGame() {
        for(Player p : PlayersHandler.getPlayers())
            PlayersHandler.removePlayer(p);
        throw new RuntimeException();
    }
}
