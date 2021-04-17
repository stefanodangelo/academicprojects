package it.polimi.ingsw.santorini.controller.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Brief An Observable is an object which state is of Observer's interest and it has the purpose to keep the up to date
 * @param <T> is the generic representing the state observed by each Observer
 */
public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();
    static final Integer MAX_CAPABILITY = 3;
    static final Integer MIN_NUMOF_PLAYERS = 2;
    static final Integer HOST_ID = 1;
    static Integer CURRENT_CAPABILITY = MIN_NUMOF_PLAYERS;

    /**
     * Brief Adds an observer to the list of registered observers
     * @param observer is the observer to add
     */
    public void registerObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Brief Removes an observer from the list of observers
     * @param observer is the observer to remove
     */
    public void unregisterObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Brief Notifies all the registered observers
     * @param message is the object of the notification
     * @throws ArrayIndexOutOfBoundsException if some error during the notification process went wrong
     */
    protected void notifyObservers(T message) throws ArrayIndexOutOfBoundsException {
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

    /**
     * @return the size of the registered observers
     */
    protected Integer getObserversNumber(){
        synchronized (observers){
            return observers.size();
        }
    }

    /**
     * Brief Notifies a specific observer
     * @param index is used to find the observer in the list
     * @param message is the object of the notification
     * @throws ArrayIndexOutOfBoundsException if some error during the notification process went wrong
     */
    protected void notifyObservers(Integer index, T message) throws ArrayIndexOutOfBoundsException{
        synchronized (observers) {
            observers.get(index).update(message);
        }
    }

    /**
     * Brief Unregisters all the observers listening on this Observable
     */
    protected void unregisterAllObservers(){
        synchronized (observers){
            observers.removeAll(Collections.unmodifiableList(observers));
        }
    }

    /**
     * Brief Updates all the observers' ids
     * It is followed the convention of progressive ids starting from 1
     */
    protected void updateObserversIds(){
        synchronized (observers){
            for(Observer<T> observer : observers)
                observer.updateId(observers.indexOf(observer) + 1);
        }
    }
}
