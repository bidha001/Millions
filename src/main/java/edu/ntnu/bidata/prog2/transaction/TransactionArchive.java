package edu.ntnu.bidata.prog2.transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class representing an archive of transactions.
 */
public class TransactionArchive {
    private final List<Transaction> transactions;

    /**
     * Constructs a new TransactionArchive with an empty list of transactions.
     */
    public TransactionArchive() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a transaction to the archive.
     *
     * @param transaction the transaction to be added
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Retrieves a copy of all transactions in the archive.
     *
     * @return a list containing all transactions in the archive
     */
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Counts the number of distinct weeks in which transactions have occurred.
     *
     * @return the number of distinct weeks
     */
    public int countDistinctWeeks() {
        Set<Integer> weeks = new HashSet<>();

        for (Transaction transaction : transactions) {
            weeks.add(transaction.getWeek());
        }
        return weeks.size();
    }
}