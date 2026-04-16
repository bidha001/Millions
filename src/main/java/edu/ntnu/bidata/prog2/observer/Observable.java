package edu.ntnu.bidata.prog2.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for observable subjects in the game model.
 * Manages a list of observers and notifies them of state changes.
 */
public class Observable {

    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Registers an observer to receive notifications.
     *
     * @param observer the observer to add (must not be null)
     */
    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the notification list.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of a state change.
     *
     * @param event the type of change that occurred
     */
    protected void notifyObservers(GameEvent event) {
        // iterate over a copy to avoid ConcurrentModificationException
        // if an observer unregisters itself in response to the event
        for (GameObserver observer : new ArrayList<>(observers)) {
            observer.onGameChanged(event);
        }
    }
}