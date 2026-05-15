package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Portfolio class represents a collection of shares owned by a player in the stock market game.
 * It provides methods to manage the shares, calculate the net worth of the portfolio, and retrieve
 * information about the shares held.
 *
 * @author Binit Dhungana
 * @version 2026-02-11
 */
public class Portfolio {

    private final List<Share> shares;

    /**
     * Constructs an empty Portfolio.
     */
    public Portfolio() {
        this.shares = new ArrayList<>();
    }

    /**
     * Adds a share to the portfolio.
     *
     * @param share the share to add
     * @return {@code true} if the share was added, {@code false} if it was already present
     * @throws IllegalArgumentException if the share is null
     */
    public boolean addShare(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        return shares.add(share);
    }

    /**
     * Removes a share from the portfolio.
     *
     * @param share the share to remove
     * @return {@code true} if the share was removed, {@code false} if it was not found
     */
    public boolean removeShare(Share share) {
        return shares.remove(share);
    }

    /**
     * Checks if the portfolio contains a specific share.
     *
     * @param share the share to check for
     * @return {@code true} if the share is in the portfolio, {@code false} otherwise
     */
    public boolean contains(Share share) {
        return shares.contains(share);
    }

    /**
     * Returns a defensive copy of all shares in the portfolio.
     *
     * @return a list of all shares
     */
    public List<Share> getShares() {
        return new ArrayList<>(shares);
    }

    /**
     * Returns a list of shares in the portfolio that match the given stock symbol.
     *
     * @param symbol the stock symbol to filter by
     * @return a list of shares with the specified stock symbol, or an empty list if none are found
     * @throws IllegalArgumentException if the symbol is null
     */
    public List<Share> getShares(String symbol) {
        Objects.requireNonNull(symbol, "symbol");
        List<Share> result = new ArrayList<>();
        for (Share s : shares) {
            if (s.getStock().getSymbol().equals(symbol)) {
                result.add(s);
            }
        }
        return result;
    }

    /**
     * Calculates the total quantity of shares in the portfolio for a given stock symbol.
     *
     * @param symbol the stock symbol to calculate the total quantity for
     * @return the total quantity of shares for the specified stock symbol, or zero if none are found
     * @throws IllegalArgumentException if the symbol is null
     */
    public BigDecimal getTotalQuantity(String symbol) {
        Objects.requireNonNull(symbol, "symbol");
        BigDecimal total = BigDecimal.ZERO;
        for (Share s : shares) {
            if (s.getStock().getSymbol().equals(symbol)) {
                total = total.add(s.getQuantity());
            }
        }
        return total;
    }

    /**
     * Calculates the net worth of the portfolio by summing the current value of all shares.
     *
     * @return the total net worth of the portfolio
     */
    public BigDecimal getNetWorth() {
        BigDecimal total = BigDecimal.ZERO;
        for (Share share : shares) {
            total = total.add(
                    share.getStock().getSalesPrice().multiply(share.getQuantity())
            );
        }
        return total;
    }
}