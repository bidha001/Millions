package edu.ntnu.bidata.prog2.observer;

/**
 * Observer interface for receiving notifications about changes in the game model.
 * Implementations (typically views) will be notified when game state changes,
 * e.g. after a transaction or when the week advances.
 */
public interface GameObserver {

    /**
     * Called when the game state has changed.
     * The observer should update its display accordingly.
     *
     * @param event describes what kind of change occurred
     */
    public void onGameChanged(GameEvent event);
}