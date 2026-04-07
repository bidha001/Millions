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
    private final BigDecimal salePrice;

    /**
     * Constructs a new Share object with the specified stock, quantity, and purchase price.
     *
     * @param stock         The stock associated with this share.
     * @param quantity      The quantity of shares owned.
     * @param purchasePrice The price at which the shares were purchased.
     */
    public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice, BigDecimal salePrice){
        this.stock = stock;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
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

    /**
     * Retrieves the price at which the shares were sold.
     *
     * @return The sale price of the shares.
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * Determines whether this Share is equal to another object.
     * Two Share objects are considered equal if they have the same stock symbol,
     * quantity, and purchase price.
     *
     * @param o The object to compare with this Share.
     * @return true if the specified object is equal to this Share; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Share share = (Share) o;

        return stock.getSymbol().equals(share.stock.getSymbol())
                && quantity.compareTo(share.quantity) == 0
                && purchasePrice.compareTo(share.purchasePrice) == 0;
    }

    /**
     * Returns a hash code value for this Share object.
     * The hash code is computed based on the stock symbol, quantity, and purchase price.
     *
     * @return A hash code value for this Share object.
     */
    @Override
    public int hashCode() {
        return stock.getSymbol().hashCode()
                + quantity.hashCode()
                + purchasePrice.hashCode();
    }
}
