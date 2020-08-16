package it.polimi.ingsw.santorini.model.gameoperations.state.immutable;

import it.polimi.ingsw.santorini.model.GameObject;

/**
 * Brief Immutable implementation for storing purposes of GameObject
 * @see GameObject
 */
public class ImmutableGameObject {

    private final ImmutablePosition position;

    private final GameObject gameObject;

    /**
     * Brief Creates a new ImmutableGameObject copying the content of the object passed as a parameter
     * @param gameObject is the object that is wanted to become immutable
     */
    public ImmutableGameObject(GameObject gameObject) {
        position = new ImmutablePosition(gameObject.getPosition());
        this.gameObject = gameObject;
    }

    public ImmutablePosition getPosition() {
        return position;
    }

    /**
     * @return the mutable version of {this} ImmutableGameObject
     */
    public GameObject getMutable() {
        gameObject.setPosition(position.getMutable());
        return gameObject;
    }
}
