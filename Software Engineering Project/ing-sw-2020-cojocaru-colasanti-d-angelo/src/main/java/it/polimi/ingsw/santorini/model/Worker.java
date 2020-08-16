package it.polimi.ingsw.santorini.model;

/**
 * Brief This class represents a Cellable worker in game
 *       A worker may move in a certain position, so it's popped from the previous Stack and is pushed on the new Stack
 *       The only one variable of this class is the player who owns the worker, so it's easy to trace back the player when we are checking conditions, for example, on a stack
 *       A worker is Cellable on a block of any level but NOT on a DOME nor on another worker
 *       What a worker can do is to move to a free cell around him/her and to build a block
 */
public class Worker extends GameObject {
    private final String color;
    private final Gender gender;
    private Integer playerId;

    public Worker(String color, Gender gender){
        super();
        this.color = color;
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public Gender getGender() {
        return gender;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    /**
     * Brief overriden from Cellable
     * @return true only if the GameObject can occupy the Cell it is in, that is if it is a Worker
     */
    @Override
    public Boolean occupiesCell() {
        return true;
    }

    /**
     * Brief overriden from Cellable
     * @return worker's owner's occupantId if it is a Worker, otherwise null
     */
    @Override
    public Integer occupantId() {
        return playerId;
    }

    /**
     * Brief overriden from Cellable
     * @return true if the GameObject is a Dome type Block, otherwise false
     */
    @Override
    public Boolean blocksCell() {
        return false;
    }

    /**
     * Brief overriden from Cellable
     * @return the height at which the Cell is set by pushed GameObjects
     */
    @Override
    public Integer correspondingLevel(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return color.equals(worker.color);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "color=" + color +
                ", playerId=" + playerId +
                super.toString() +
                '}';
    }
}
