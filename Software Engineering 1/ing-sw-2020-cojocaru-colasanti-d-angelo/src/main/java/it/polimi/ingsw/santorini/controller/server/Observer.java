package it.polimi.ingsw.santorini.controller.server;

/**
 * Brief Interface for Observer pattern, that is an object that listen for something to happen
 * @param <T> is the generic representing the state to update when calling the homonym method
 */
public interface Observer<T> {
    void update(T message);
    void updateId(Integer newId);
}
