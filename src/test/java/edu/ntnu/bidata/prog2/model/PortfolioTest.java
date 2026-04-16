package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PortfolioTest {

    /**
     * Tests that a new portfolio is empty when created.
     */
    @Test
    public void newPortfolioIsEmpty() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0, portfolio.getAllShares().size());
    }

    /**
     * Tests that addShare correctly adds a new share to the portfolio.
     */
    @Test
    public void addShareAddsShareToPortfolio() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);

        portfolio.addShare(share);

        assertEquals(1, portfolio.getAllShares().size());
    }

    /**
     * Tests that adding a share with an existing stock symbol merges the quantities.
     */
    @Test
    public void addShareMergesQuantityWhenSameStockExists() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));

        portfolio.addShare(new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null));
        portfolio.addShare(new Share(stock, new BigDecimal("5"), new BigDecimal("120"), null));

        // Should still be one entry, with merged quantity of 15
        assertEquals(1, portfolio.getAllShares().size());
        assertEquals(new BigDecimal("15"), portfolio.getTotalQuantity("AAPL"));
    }

    /**
     * Tests that adding shares for different stocks keeps them as separate entries.
     */
    @Test
    public void addShareKeepsDifferentStocksSeparate() {
        Portfolio portfolio = new Portfolio();
        Stock apple = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Stock tesla = new Stock("Tesla", "TSLA", new BigDecimal("200"));

        portfolio.addShare(new Share(apple, new BigDecimal("10"), new BigDecimal("100"), null));
        portfolio.addShare(new Share(tesla, new BigDecimal("5"), new BigDecimal("200"), null));

        assertEquals(2, portfolio.getAllShares().size());
    }

    /**
     * Tests that removeShare correctly removes a share from the portfolio.
     */
    @Test
    public void removeShareRemovesShareFromPortfolio() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);

        portfolio.addShare(share);
        portfolio.removeShare(share);

        assertEquals(0, portfolio.getAllShares().size());
    }

    /**
     * Tests that containsShare returns true when the share exists in the portfolio.
     */
    @Test
    public void containsShareReturnsTrueWhenShareExists() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);

        portfolio.addShare(share);

        assertTrue(portfolio.containsShare(share));
    }

    /**
     * Tests that containsShare returns false when the share does not exist in the portfolio.
     */
    @Test
    public void containsShareReturnsFalseWhenShareDoesNotExist() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);

        assertFalse(portfolio.containsShare(share));
    }

    /**
     * Tests that getTotalQuantity returns the correct total for a given stock symbol.
     */
    @Test
    public void getTotalQuantityReturnsCorrectSum() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));

        portfolio.addShare(new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null));
        portfolio.addShare(new Share(stock, new BigDecimal("7"), new BigDecimal("105"), null));

        assertEquals(new BigDecimal("17"), portfolio.getTotalQuantity("AAPL"));
    }

    /**
     * Tests that getTotalQuantity returns zero when the symbol is not in the portfolio.
     */
    @Test
    public void getTotalQuantityReturnsZeroWhenSymbolNotFound() {
        Portfolio portfolio = new Portfolio();

        assertEquals(BigDecimal.ZERO, portfolio.getTotalQuantity("AAPL"));
    }

    /**
     * Tests that getShareByStock returns the correct share when it exists in the portfolio.
     */
    @Test
    public void getShareByStockReturnsCorrectShare() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);

        portfolio.addShare(share);

        Share result = portfolio.getShareByStock(stock);

        assertNotNull(result);
        assertEquals("AAPL", result.getStock().getSymbol());
    }

    /**
     * Tests that getShareByStock returns null when the stock is not in the portfolio.
     */
    @Test
    public void getShareByStockReturnsNullWhenStockNotFound() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));

        assertNull(portfolio.getShareByStock(stock));
    }

    /**
     * Tests that getNetWorth returns zero for an empty portfolio.
     */
    @Test
    public void getNetWorthIsZeroForEmptyPortfolio() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0, portfolio.getNetWorth().compareTo(BigDecimal.ZERO));
    }

    /**
     * Tests that getNetWorth correctly calculates the total value of all shares in the portfolio.
     */
    @Test
    public void getNetWorthCalculatesTotalValueOfShares() {
        Portfolio portfolio = new Portfolio();
        Stock apple = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Stock tesla = new Stock("Tesla", "TSLA", new BigDecimal("200"));

        portfolio.addShare(new Share(apple, new BigDecimal("10"), new BigDecimal("100"), null));
        portfolio.addShare(new Share(tesla, new BigDecimal("5"), new BigDecimal("200"), null));

        // Apple: 100 * 10 = 1000
        // Tesla: 200 * 5 = 1000
        // Total: 2000
        assertEquals(0, portfolio.getNetWorth().compareTo(new BigDecimal("2000")));
    }

    /**
     * Tests that getAllShares returns a defensive copy so the internal list cannot be modified.
     */
    @Test
    public void getAllSharesReturnsDefensiveCopy() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);
        portfolio.addShare(share);

        portfolio.getAllShares().clear();

        // Internal list should still have the share
        assertEquals(1, portfolio.getAllShares().size());
    }
}