package it.polimi.ingsw.santorini.communication;

import java.io.Serializable;
import java.util.Objects;

/**
 * Brief Class that represents an immutable 2D position used for the communication between client and server
 */
public class ImmutablePosition implements Serializable {
    private final Integer x;
    private final Integer y;

    public ImmutablePosition(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutablePosition position = (ImmutablePosition) o;
        return x.equals(position.x) &&
                y.equals(position.y);
    }

    @Override
    public String toString() {
        return "ImmutablePosition{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
