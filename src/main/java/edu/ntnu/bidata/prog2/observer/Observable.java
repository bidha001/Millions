package edu.ntnu.bidata.prog2.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable class that maintains a list of observers and notifies them of changes.
 * This is the "subject" in the observer pattern. It allows observers to register
 * and unregister themselves, and it notifies all registered observers when a change occurs.
 */
public class Observable {

    private final List<GameObserver> observers = new ArrayList<>();

    /**
     * Adds an observer to the notification list.
     *
     * @param observer the observer to add
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
     * Notifies all registered observers of a change in the game state.
     *
     * @param event describes what kind of change occurred
     */
    protected void notifyObservers(GameEvent event) {
        // iterate over a copy to avoid ConcurrentModificationException
        // if an observer unregisters itself in response to the event
        for (GameObserver observer : new ArrayList<>(observers)) {
            observer.onGameChanged(event);
        }
    }
}