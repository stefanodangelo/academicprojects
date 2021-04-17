package it.polimi.ingsw.santorini.model;

/**
 * Brief Abstract class representing workers and blocks, the game components that can be moved or built on each tile
 */
public abstract class GameObject implements Cellable {
    private Position position;

    public GameObject() {
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Boolean occupiesCell() {
        return false;
    }

    @Override
    public Integer occupantId() {
        return null;
    }

    @Override
    public Boolean blocksCell() {
        return false;
    }

    @Override
    public Integer correspondingLevel() {return null;}

    @Override
    public String toString() {
        return "GameObject{" +
                "position=" + position +
                '}';
    }
}
