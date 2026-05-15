package edu.ntnu.bidata.prog2.observer;

/**
 * Observer interface for the stock trading game.
 * Classes that implement this interface can register themselves as observers
 * to receive updates when the game state changes.
 */
public interface GameObserver {

    /**
     * Called when the game state changes. The event parameter describes what kind of change occurred.
     *
     * @param event the type of game event that occurred
     */
    public void onGameChanged(GameEvent event);
}