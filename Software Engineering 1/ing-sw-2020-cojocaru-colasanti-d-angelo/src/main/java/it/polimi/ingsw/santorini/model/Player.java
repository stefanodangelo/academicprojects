package it.polimi.ingsw.santorini.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Brief Class representing a specific player in the game having a list of workers and a god card. The array of workers is needed to distinguish between the two workers
 */
public class Player {
    private Integer id;
    private final String name;
    private Card card;
    private ArrayList<Worker> workers;

    public Player(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id){ this.id = id; }

    public String getName() {
        return name;
    }

    public Card getCard() {
        return card;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Worker> getWorkers() {
        return (ArrayList<Worker>) workers.clone();
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        if(this.workers == null)
            this.workers = workers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id) &&
                name.equals(player.name) &&
                Objects.equals(card, player.card) &&
                Objects.equals(workers, player.workers);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", card=" + card +
                ", workers=" + workers +
                '}';
    }
}
