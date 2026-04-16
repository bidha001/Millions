package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.model.Share;

/**
 * Factory for creating transaction objects.
 * Centralizes the creation of different transaction types so that
 * client code (e.g. Exchange) does not need to know about concrete classes.
 */
public class TransactionFactory {

    /**
     * The type of transaction to create.
     */
    public enum Type {
        PURCHASE,
        SALE
    }

    /**
     * Private constructor to prevent instantiation — this is a utility class.
     */
    private TransactionFactory() {
    }

    /**
     * Creates a new transaction of the given type.
     *
     * @param type  the type of transaction to create
     * @param share the share involved in the transaction
     * @param week  the week number when the transaction is made
     * @return a new Transaction instance
     * @throws IllegalArgumentException if the transaction type is not supported
     */
    public static Transaction create(Type type, Share share, int week) {

        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null!");
        }

        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null!");
        }

        if (week < 1) {
            throw new IllegalArgumentException("Week must be at least 1!");
        }

        return switch (type) {
            case PURCHASE -> new Purchase(share, week);
            case SALE -> new Sale(share, week);
        };
    }
}