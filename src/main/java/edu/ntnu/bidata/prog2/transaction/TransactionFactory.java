package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.model.Share;

/**
 * Factory class for creating transactions. Provides a static method to create
 * transactions of different types based on the given parameters. Currently only
 * PURCHASE transactions are supported, as SALE transactions require more complex
 * logic that depends on the player's portfolio and matching lots.
 */
public final class TransactionFactory {

    /**
     * Enumeration of transaction types. Currently only PURCHASE is supported, but
     * this can be extended in the future to include SALE and other transaction types.
     *
     */
    public enum Type {
        PURCHASE
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private TransactionFactory() {
        // utility class — no instances
    }

    /**
     * Creates a new transaction of the specified type for the given share and week.
     *
     * @param type  the type of transaction to create
     * @param share the share involved in the transaction
     * @param week  the week number when the transaction is made
     * @return a new Transaction instance of the specified type
     * @throws IllegalArgumentException if any argument is invalid or if the type is unsupported
     */
    public static Transaction create(Type type, Share share, int week) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        if (week < 1) {
            throw new IllegalArgumentException("Week must be at least 1");
        }

        return switch (type) {
            case PURCHASE -> new Purchase(share, week);
        };
    }
}