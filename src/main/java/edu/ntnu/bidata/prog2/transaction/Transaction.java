package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.TransactionCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

/**
 * Abstract class representing a transaction (buy or sell) in the stock market simulation.
 * It contains common properties and methods for both buy and sell transactions.
 */
public abstract class Transaction {
    protected final Share share;
    protected int week;
    protected final TransactionCalculator calculator;
    protected boolean committed;

    /**
     * Constructs a new Transaction with the specified share, week, and calculator.
     *
     * @param share      The share involved in the transaction.
     * @param week       The week number when the transaction is made.
     * @param calculator The calculator used to compute the financial details of the transaction.
     */
    public Transaction(Share share, int week, TransactionCalculator calculator) {
        this.share = share;
        this.week = week;
        this.calculator = calculator;
        this.committed = false;
    }

    /**
     * Retrieves the share involved in the transaction.
     *
     * @return The share involved in the transaction.
     */
    public Share getShare() {
        return share;
    }

    /**
     * Retrieves the week number when the transaction is made.
     *
     * @return The week number of the transaction.
     */
    public int getWeek() {
        return week;
    }

    /**
     * Checks if the transaction has been committed.
     *
     * @return true if the transaction is committed, false otherwise.
     */
    public boolean isCommitted() {
        return committed;
    }

    /**
     * Retrieves the calculator used for this transaction.
     *
     * @return The TransactionCalculator associated with this transaction.
     */
    public TransactionCalculator getCalculator() {
        return calculator;
    }

    /**
     * Commits the transaction, applying its effects to the player's portfolio and cash balance.
     * The transaction is also recorded in the player's transaction archive.
     *
     * @param player The player for whom the transaction is being committed.
     */
    public abstract void commit(Player player);
}