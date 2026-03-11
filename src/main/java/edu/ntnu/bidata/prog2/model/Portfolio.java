package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a portfolio of shares.
 */
public class Portfolio {
    private final List<Share> shares;

    /**
     * Constructs an empty portfolio.
     */
    public Portfolio() {
        this.shares = new ArrayList<>();
    }

    /**
     * Adds a share to the portfolio.
     *
     * @param share the share to add
     */
    public void addShare(Share share) {
        shares.add(share);
    }

    /**
     * Removes a share from the portfolio.
     *
     * @param share the share to remove
     */
    public void removeShare(Share share) {
        shares.remove(share);
    }

    /**
     * Checks if the portfolio contains a specific share.
     *
     * @param share the share to check for
     * @return true if the share is in the portfolio, false otherwise
     */
    public boolean containsShare(Share share) {
        return shares.contains(share);
    }

    /**
     * Retrieves a list of shares in the portfolio that have the specified stock symbol.
     *
     * @param symbol the stock symbol to search for
     * @return a list of shares with the specified stock symbol
     */
    public List<Share> getShares(String symbol) { //return all the shares in the portfolio that have the same stock symbol.
        List<Share> result = new ArrayList<>();

        for (Share share : shares) {
            if (share.getStock().getSymbol().equals(symbol)) {
                result.add(share);
            }
        }
        return result;
    }

    /**
     * Calculates the total quantity of shares in the portfolio for a specific stock symbol.
     *
     * @param symbol the stock symbol to calculate the total quantity for
     * @return the total quantity of shares with the specified stock symbol
     */
    public BigDecimal getTotalQuantity(String symbol) {
        BigDecimal totalQuantity = BigDecimal.ZERO;

        for (Share share : shares) {
            if (share.getStock().getSymbol().equals(symbol)) {
                totalQuantity = totalQuantity.add(share.getQuantity());
            }
        }
        return totalQuantity;
    }

    /**
     * Retrieves a list of all shares in the portfolio.
     *
     * @return a list of all shares in the portfolio
     */
    public List<Share> getAllShares() {
        return new ArrayList<>(shares);
    }

    /**
     * Calculates the net worth of the portfolio by summing the value of all shares.
     *
     * @return the net worth of the portfolio
     */
    public BigDecimal getNetWorth() {

        BigDecimal total = BigDecimal.ZERO;

        for (Share share : shares) {

            BigDecimal price = share.getStock().getSalesPrice();
            BigDecimal quantity = share.getQuantity();

            BigDecimal value = price.multiply(quantity);

            total = total.add(value);
        }

        return total;
    }
}
