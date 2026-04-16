package edu.ntnu.bidata.prog2.observer;

/**
 * Represents the different kinds of changes that can occur in the game model.
 * Observers can use this to decide what to update.
 */
public enum GameEvent {

    /**
     *  A new game has been started.
     */
    GAME_STARTED,

    /**
     * A transaction (buy or sell) has been completed.
     */
    TRANSACTION_COMPLETED,

    /**
     * The game advanced to the next week (prices updated).
     */
    WEEK_ADVANCED
}