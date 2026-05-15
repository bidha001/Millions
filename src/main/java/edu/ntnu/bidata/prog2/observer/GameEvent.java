package edu.ntnu.bidata.prog2.observer;

/**
 * Enum representing different types of events that can occur in the stock trading game.
 * This is used to notify observers about specific changes in the game state.
 */
public enum GameEvent {

    /**
     * The game has started.
     */
    GAME_STARTED,

    /**
     * A transaction (buy/sell) has been completed.
     */
    TRANSACTION_COMPLETED,

    /**
     * The player's portfolio has been updated (e.g., after a transaction or market change).
     */
    WEEK_ADVANCED
}