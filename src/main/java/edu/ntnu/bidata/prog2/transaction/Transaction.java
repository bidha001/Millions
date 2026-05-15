package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.TransactionCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

/**
 * Abstract base class for transactions. Encapsulates common properties and
 * behaviors of transactions, such as the share involved, the week of the
 * transaction, the calculator for totals, and whether the transaction has been
 * committed. Subclasses must implement the {@link #commit(Player)} method to
 * apply the transaction's effects to a player.
 */
public abstract class Transaction {

    protected final Share share;
    protected int week;
    protected TransactionCalculator calculator;
    protected boolean committed;

    /**
     * Constructs a new Transaction with the given share, week, and calculator.
     *
     * @param share      the share involved in the transaction
     * @param week       the week number when the transaction is made
     * @param calculator the calculator for predicting totals before commit
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected Transaction(Share share, int week, TransactionCalculator calculator) {
        this.share = share;
        this.week = week;
        this.calculator = calculator;
        this.committed = false;
    }

    /**
     * Returns the share involved in this transaction.
     *
     * @return the share involved in this transaction
     */
    public Share getShare() {
        return share;
    }

    /**
     * Returns the week number when this transaction was made.
     *
     * @return the week number of this transaction
     */
    public int getWeek() {
        return week;
    }

    /**
     * Returns whether this transaction has been committed.
     *
     * @return {@code true} if this transaction has been committed, {@code false} otherwise
     */
    public boolean isCommitted() {
        return committed;
    }

    /**
     * Returns the calculator for predicting totals before commit. Note that the
     * predicted totals may differ from the actual totals after commit, especially
     * for sales due to FIFO lot accounting.
     *
     * @return the calculator for this transaction
     */
    public TransactionCalculator getCalculator() {
        return calculator;
    }

    /**
     * Sets the calculator for this transaction. This method is protected to allow
     * subclasses to update the calculator if needed, but prevents external code
     * from changing it arbitrarily.
     *
     * @param calculator the new calculator to set (not null)
     * @throws IllegalArgumentException if {@code calculator} is null
     */
    protected void setCalculator(TransactionCalculator calculator) {
        if (calculator == null) {
            throw new IllegalArgumentException("Calculator cannot be null");
        }
        this.calculator = calculator;
    }

    /**
     * Commits this transaction by applying its effects to the given player. This
     * method must be implemented by subclasses to define the specific behavior of
     * committing a purchase or sale.
     *
     * @param player the player committing the transaction
     * @throws IllegalStateException    if this transaction has already been committed
     *                                  or if the player doesn't have enough resources (money or shares)
     * @throws IllegalArgumentException if {@code player} is null
     */
    public abstract void commit(Player player);
}