package it.polimi.ingsw.santorini.model.gameoperations.state.immutable;

import it.polimi.ingsw.santorini.model.Position;

/**
 * Brief Immutable implementation for storing purposes of Position
 * @see Position
 */
public final class ImmutablePosition {

    private final int x, y;

    /**
     * Brief Creates a new ImmutablePosition copying the content of the position passed as a parameter
     * @param position is the state that is wanted to become immutable
     */
    public ImmutablePosition(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @return the mutable version of {this} ImmutablePosition
     */
    public Position getMutable() {
        Integer x = this.x;
        Integer y = this.y;
        return new Position(x, y);
    }
}
