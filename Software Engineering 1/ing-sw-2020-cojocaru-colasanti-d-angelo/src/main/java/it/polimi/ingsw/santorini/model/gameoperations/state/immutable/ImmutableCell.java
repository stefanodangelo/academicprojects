package it.polimi.ingsw.santorini.model.gameoperations.state.immutable;

import it.polimi.ingsw.santorini.model.Cell;
import it.polimi.ingsw.santorini.model.GameObject;

import java.util.*;

/**
 * Brief Immutable implementation for storing purposes of Cell
 * @see Cell
 */
public final class ImmutableCell {

    private final Stack<ImmutableGameObject> stack = new Stack<>();
    private final boolean blocked;
    private final boolean occupied;
    private final int level;
    private final ImmutableGameObject floatingObject;

    /**
     * Brief Creates a new ImmutableCell copying the content of the cell passed as a parameter
     * @param cell is the cell that is wanted to become immutable
     */
    public ImmutableCell(Cell cell) {
        cell.getStack().forEach(gameObject -> this.stack.push(new ImmutableGameObject(gameObject)));
        blocked = cell.isBlocked();
        occupied = cell.isOccupied();
        level = cell.getLevel();
        floatingObject = cell.getFloatingObject() != null ? new ImmutableGameObject(cell.getFloatingObject()) : null;
    }

    public Stack<ImmutableGameObject> getStack() {
        return stack;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getLevel() {
        return level;
    }

    public ImmutableGameObject getFloatingObject() {
        return floatingObject;
    }

    /**
     * @return the mutable version of {this} ImmutableCell
     */
    public Cell getMutable() {
        Stack<GameObject> stack = new Stack<>();
        this.stack.forEach(immGameObject -> stack.push(immGameObject.getMutable()));
        Boolean blocked = this.blocked;
        Boolean occupied = this.occupied;
        Integer level = this.level;
        GameObject floatingGameObject = this.floatingObject != null ? this.floatingObject.getMutable() : null;
        Cell cell = new Cell();
        cell.setStack(stack);
        cell.setBlocked(blocked);
        cell.setOccupied(occupied);
        cell.setLevel(level);
        cell.setFloatingObject(floatingGameObject);
        return cell;
    }
}
