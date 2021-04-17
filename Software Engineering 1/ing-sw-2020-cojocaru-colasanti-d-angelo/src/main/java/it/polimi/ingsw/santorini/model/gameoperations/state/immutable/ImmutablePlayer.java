package it.polimi.ingsw.santorini.model.gameoperations.state.immutable;

import it.polimi.ingsw.santorini.model.Card;
import it.polimi.ingsw.santorini.model.Player;
import it.polimi.ingsw.santorini.model.Worker;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Brief Immutable implementation for storing purposes of Player
 * @see Player
 */
public final class ImmutablePlayer {

    private final int id;
    private final String name;
    private final Card card;
    private final List<Worker> workers;

    /**
     * Brief Creates a new ImmutablePlayer copying the content of the player passed as a parameter
     * @param player is the player that is wanted to become immutable
     */
    public ImmutablePlayer(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.card = player.getCard();
        this.workers = Collections.unmodifiableList(player.getWorkers());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Card getCard() {
        return card;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * @return the mutable version of {this} ImmutablePlayer
     */
    public Player getMutable() {
        Integer id = this.id;
        String name = this.name;
        Card card = this.card;
        ArrayList<Worker> workers = new ArrayList<>(this.workers);
        Player player = new Player(id, name);
        player.setCard(card);
        player.setWorkers(workers);
        return player;
    }
}
