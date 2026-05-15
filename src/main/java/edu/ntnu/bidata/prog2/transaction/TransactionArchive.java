package edu.ntnu.bidata.prog2.transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Archives all transactions a player has made. Provides methods to add transactions
 * and query them by week and type. The archive is ordered by insertion, so queries
 * return transactions in the order they were committed.
 */
public class TransactionArchive {

    private final List<Transaction> transactions;

    /**
     * Constructs an empty TransactionArchive.
     */
    public TransactionArchive() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a transaction to the archive. Transactions are stored in the order they
     * were added.
     *
     * @param transaction the transaction to add
     * @return {@code true} if the transaction was added successfully, {@code false} if it was already present
     * @throws IllegalArgumentException if {@code transaction} is null
     */
    public boolean add(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        return transactions.add(transaction);
    }

    /**
     * Checks if the archive is empty.
     *
     * @return {@code true} if the archive contains no transactions, {@code false} otherwise
     */
    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    /**
     * Returns a list of all transactions in the archive, in the order they were added.
     *
     * @return a list of all transactions in the archive
     */
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Returns a list of all transactions that occurred in the specified week, in the order they were added.
     *
     * @param week the week number to filter transactions by
     * @return a list of transactions that occurred in the specified week
     */
    public List<Transaction> getTransactions(int week) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getWeek() == week) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Returns a list of all purchase transactions that occurred in the specified week, in the order they were added.
     *
     * @param week the week number to filter purchase transactions by
     * @return a list of purchase transactions that occurred in the specified week
     */
    public List<Purchase> getPurchases(int week) {
        List<Purchase> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getWeek() == week && t instanceof Purchase p) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Returns a list of all sale transactions that occurred in the specified week, in the order they were added.
     *
     * @param week the week number to filter sale transactions by
     * @return a list of sale transactions that occurred in the specified week
     */
    public List<Sale> getSales(int week) {
        List<Sale> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getWeek() == week && t instanceof Sale s) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Returns the number of distinct weeks in which transactions occurred.
     *
     * @return the number of distinct weeks with transactions
     */
    public int countDistinctWeeks() {
        Set<Integer> weeks = new HashSet<>();
        for (Transaction t : transactions) {
            weeks.add(t.getWeek());
        }
        return weeks.size();
    }

    /**
     * Checks if the archive contains the specified transaction by identity (i.e., the same object reference).
     *
     * @param transaction the transaction to check for
     * @return {@code true} if the archive contains the specified transaction by identity, {@code false} otherwise
     * @throws IllegalArgumentException if {@code transaction} is null
     */
    boolean containsByIdentity(Transaction transaction) {
        Objects.requireNonNull(transaction);
        for (Transaction t : transactions) {
            if (t == transaction) {
                return true;
            }
        }
        return false;
    }
}