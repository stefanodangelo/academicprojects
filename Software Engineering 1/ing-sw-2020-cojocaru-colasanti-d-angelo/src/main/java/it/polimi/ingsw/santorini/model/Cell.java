package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.exceptions.EmptyCellException;

import java.util.Stack;

/**
 * Brief It's a decorator of Stack and represents the instance of a single Cell on the map.
 *       Contains reference to all the GameObjects present on it, both Workers and Blocks, through a Stack,
 *       that also allows to keep track of the level of the tower.
 */
public class Cell {
    private Stack<GameObject> stack = new Stack<>();
    private Boolean blocked;
    private Boolean occupied;
    private Integer level;

    /**
     * Brief a floating (not pushed) GameObject stored in the Cell
     */
    private GameObject floatingObject;

    /**
     * Brief getter for the floatingObject
     * @return floatingObject
     */
    public GameObject getFloatingObject() {
        return floatingObject;
    }

    /**
     * Brief setter for the floatingObject
     * @param floatingObject the floating object
     */
    public void floatObject(GameObject floatingObject) {
        this.floatingObject = floatingObject;
    }

    /**
     * Brief pushes the current floatingObject in this Cell if it is not null and unfloats it
     */
    public void pushFloatingObject() {
        if (floatingObject != null) {
            pushGameObject(floatingObject);
        }
    }

    /**
     * Brief Getter for the stack
     * @return stack
     */
    public Stack<GameObject> getStack() {
        return stack;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setFloatingObject(GameObject floatingObject) {
        this.floatingObject = floatingObject;
    }

    public void setStack(Stack<GameObject> stack) {
        this.stack = stack;
    }


    public Cell(){
        blocked = false;
        occupied = false;
        level = 0;
    }

    /**
     * @return true if there is a dome
     */
    public Boolean isBlocked(){
        return blocked;
    }

    /**
     * @return true if there is a worker
     */
    public Boolean isOccupied() { return occupied; }

    /**
     * @return true if there isn't a dome nor a worker
     */
    public Boolean isReachable() {
        return !isBlocked() && !isOccupied();
    }
    /**
     * @return the highest floor of the building as an Integer (0, 1, 2, 3, 4)
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Brief Sets the correct level to the Cell
     * @param oldLevel is the level before it's updated
     */
    private void updateLevel(Integer oldLevel) {
        if(stack.isEmpty())
            this.level = 0;
        else if(stack.peek().correspondingLevel() != null)   //if object on top is not a Worker
            this.level = stack.peek().correspondingLevel();
        else
            this.level = oldLevel;
    }

    /**
     * Brief Places a GameObject on the Cell
     * @param gameObject is the gameObject to be pushed
     */
    public void pushGameObject(GameObject gameObject) {
        if (gameObject.occupiesCell())  //GameObject is a Worker
            occupied = true;
        else if(gameObject.blocksCell()) //GameObject is a Dome
            blocked = true;
        stack.push(gameObject);
        updateLevel(level);
    }

    /**
     * Brief pop on the Cell's stack
     * @return object popped
     * @throws EmptyCellException if there aren't GameObjects to pop from the Stack
     */
    public GameObject popGameObject() throws EmptyCellException {
        GameObject poppedObject;
        if(stack.isEmpty()) throw new EmptyCellException();
        poppedObject = stack.pop();
        if(poppedObject.occupiesCell())
            occupied = false;
        else if(poppedObject.blocksCell())
            blocked = false;
        updateLevel(level);
        return poppedObject;
    }

    /**
     * Brief getter for current Worker's playerId
     * @return Worker's playerId if there is one on the stack, otherwise null
     */
    public Integer getOccupantId() {
        if(getOccupant() != null) return getOccupant().occupantId();
        return null;
    }

    /**
     * Brief Getter for the worker that occupies the cell
     * @return the occupant worker, otherwise null
     */
    public Worker getOccupant(){
         if(isOccupied()) return (Worker) stack.peek();
         return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return blocked == cell.blocked &&
                occupied == cell.occupied &&
                stack.equals(cell.stack);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "stack=" + stack +
                ", blocked=" + blocked +
                ", occupied=" + occupied +
                ", level=" + level +
                '}';
    }
}
