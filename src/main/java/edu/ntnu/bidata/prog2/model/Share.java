package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;

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
        this.stock = stock;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

    /**
     * Retrieves the stock associated with this share.
     *
     * @return The stock associated with this share.
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
}
