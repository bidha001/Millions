package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortfolioTest {

    /** Tests that a new portfolio is empty. */
    @Test
    void newPortfolioIsEmpty() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0, portfolio.getShares().size());
    }

    /** Tests that addShare adds a share to the portfolio. */
    @Test
    void addShareAddsShareToPortfolio() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"));

        assertTrue(portfolio.addShare(share));
        assertEquals(1, portfolio.getShares().size());
    }

    /** Tests that addShare rejects null. */
    @Test
    void addShareRejectsNull() {
        Portfolio portfolio = new Portfolio();

        assertThrows(IllegalArgumentException.class, () -> portfolio.addShare(null));
    }

    /** Tests that adding two buys of the same stock keeps them as separate lots. */
    @Test
    void addShareKeepsSameStockAsSeparateLots() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));

        portfolio.addShare(new Share(stock, new BigDecimal("10"), new BigDecimal("100")));
        portfolio.addShare(new Share(stock, new BigDecimal("5"), new BigDecimal("120")));

        // Two separate lots, NOT merged — preserves per-lot purchase price for tax basis.
        assertEquals(2, portfolio.getShares().size());
        assertEquals(0, portfolio.getTotalQuantity("AAPL").compareTo(new BigDecimal("15")));
    }

    /** Tests that adding shares for different stocks keeps them as separate entries. */
    @Test
    void addShareKeepsDifferentStocksSeparate() {
        Portfolio portfolio = new Portfolio();
        Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));

        portfolio.addShare(new Share(apple, new BigDecimal("10"), new BigDecimal("100")));
        portfolio.addShare(new Share(tesla, new BigDecimal("5"), new BigDecimal("200")));

        assertEquals(2, portfolio.getShares().size());
    }

    /** Tests that removeShare removes a share and returns true. */
    @Test
    void removeShareRemovesShareFromPortfolio() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"));

        portfolio.addShare(share);
        assertTrue(portfolio.removeShare(share));
        assertEquals(0, portfolio.getShares().size());
    }

    /** Tests that removeShare returns false when the share isn't present. */
    @Test
    void removeShareReturnsFalseWhenNotPresent() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"));

        assertFalse(portfolio.removeShare(share));
    }

    /** Tests that contains returns true when the share exists. */
    @Test
    void containsReturnsTrueWhenShareExists() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"));

        portfolio.addShare(share);

        assertTrue(portfolio.contains(share));
    }

    /** Tests that contains returns false when the share does not exist. */
    @Test
    void containsReturnsFalseWhenShareDoesNotExist() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"));

        assertFalse(portfolio.contains(share));
    }

    /** Tests that getShares(symbol) returns only shares for that symbol. */
    @Test
    void getSharesBySymbolFiltersCorrectly() {
        Portfolio portfolio = new Portfolio();
        Stock apple = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Stock tesla = new Stock("TSLA", "Tesla", new BigDecimal("200"));

        portfolio.addShare(new Share(apple, new BigDecimal("10"), new BigDecimal("100")));
        portfolio.addShare(new Share(apple, new BigDecimal("5"), new BigDecimal("110")));
        portfolio.addShare(new Share(tesla, new BigDecimal("3"), new BigDecimal("200")));

        assertEquals(2, portfolio.getShares("AAPL").size());
        assertEquals(1, portfolio.getShares("TSLA").size());
    }

    /** Tests that getTotalQuantity returns the correct total for a given stock symbol. */
    @Test
    void getTotalQuantityReturnsCorrectSum() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));

        portfolio.addShare(new Share(stock, new BigDecimal("10"), new BigDecimal("100")));
        portfolio.addShare(new Share(stock, new BigDecimal("7"), new BigDecimal("105")));

        assertEquals(0, portfolio.getTotalQuantity("AAPL").compareTo(new BigDecimal("17")));
    }

    /** Tests that getTotalQuantity returns zero when the symbol is not in the portfolio. */
    @Test
    void getTotalQuantityReturnsZeroWhenSymbolNotFound() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0, portfolio.getTotalQuantity("AAPL").compareTo(BigDecimal.ZERO));
    }

    /** Tests that getNetWorth returns zero for an empty portfolio. */
    @Test
    void getNetWorthIsZeroForEmptyPortfolio() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0, portfolio.getNetWorth().compareTo(BigDecimal.ZERO));
    }

    /** Tests that getNetWorth uses the stock's current price, not the purchase price. */
    @Test
    void getNetWorthUsesCurrentPriceNotPurchasePrice() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));

        // Bought at 100, market now at 120
        portfolio.addShare(new Share(stock, new BigDecimal("10"), new BigDecimal("100")));
        stock.addNewSalesPrice(new BigDecimal("120"));

        // 10 * 120 = 1200, not 10 * 100 = 1000
        assertEquals(0, portfolio.getNetWorth().compareTo(new BigDecimal("1200")));
    }

    /** Tests that getShares() returns a defensive copy. */
    @Test
    void getSharesReturnsDefensiveCopy() {
        Portfolio portfolio = new Portfolio();
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"));
        portfolio.addShare(share);

        portfolio.getShares().clear();

        assertEquals(1, portfolio.getShares().size());
    }
}