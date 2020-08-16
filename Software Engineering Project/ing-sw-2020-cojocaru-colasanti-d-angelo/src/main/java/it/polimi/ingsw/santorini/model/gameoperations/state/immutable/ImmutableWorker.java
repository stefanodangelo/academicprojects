package it.polimi.ingsw.santorini.model.gameoperations.state.immutable;

import it.polimi.ingsw.santorini.model.Gender;
import it.polimi.ingsw.santorini.model.Worker;

/**
 * Brief Immutable implementation for storing purposes of Worker
 * @see Worker
 */
public final class ImmutableWorker extends ImmutableGameObject {

    private final String color;
    private final Gender gender;
    private final int playerId;

    /**
     * Brief Creates a new ImmutableWorker copying the content of the worker passed as a parameter
     * @param worker is the state that is wanted to become immutable
     */
    public ImmutableWorker(Worker worker) {
        super(worker);
        this.color = worker.getColor();
        this.gender = worker.getGender();
        this.playerId = worker.getPlayerId();
    }

    public String getColor() {
        return color;
    }

    public Gender getGender() {
        return gender;
    }

    public int getPlayerId() {
        return playerId;
    }

    /**
     * @return the mutable version of {this} ImmutableWorker
     */
    public Worker getMutable() {
        String color = this.color;
        Gender gender = this.gender;
        Integer playerId = this.playerId;
        Worker worker = new Worker(color, gender);
        worker.setPlayerId(playerId);
        return worker;
    }
}
