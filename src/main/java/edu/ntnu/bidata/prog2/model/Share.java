package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The Share class represents a share of stock owned by a player in the stock market game.
 * It contains information about the stock, the quantity of shares owned, and the purchase price.
 *
 * @author Binit Dhungana
 * @version 2026-02-11
 */
public class Share {
    private final Stock stock;
    private final BigDecimal quantity;
    private final BigDecimal purchasePrice;

    /**
     * Constructs a new Share object with the specified stock, quantity, and purchase price.
     *
     * @param stock         The stock associated with this share.
     * @param quantity      The quantity of shares owned.
     * @param purchasePrice The price at which the shares were purchased.
     */
    public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Purchase price must be positive");
        }
        this.stock = stock;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

    /**
     * Retrieves the stock that this share is in.
     *
     * @return the stock that this share is in.
     */
    public Stock getStock() {
        return stock;
    }

        /**
        * Retrieves the quantity of shares owned.
        *
        * @return The quantity of shares owned.
        */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Retrieves the price at which the shares were purchased.
     *
     * @return The purchase price of the shares.
     */
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Checks if this share is equal to another object. Two shares are considered equal if they have the same stock symbol,
     * the same quantity (using {@code compareTo} for comparison), and the same purchase price (also using {@code compareTo}).
     *
     * @param o the object to compare with
     * @return {@code true} if the shares are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Share other)) return false;
        return stock.getSymbol().equals(other.stock.getSymbol())
                && quantity.compareTo(other.quantity) == 0
                && purchasePrice.compareTo(other.purchasePrice) == 0;
    }

    /**
     * Computes the hash code for this share based on the stock symbol, quantity, and purchase price.
     * The quantity and purchase price are normalized by stripping trailing zeros to ensure consistent hash codes for equivalent values.
     *
     * @return the hash code for this share
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                stock.getSymbol(),
                quantity.stripTrailingZeros(),
                purchasePrice.stripTrailingZeros()
        );
    }
}
