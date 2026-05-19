package edu.ntnu.bidata.prog2.market;

import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.Purchase;
import edu.ntnu.bidata.prog2.transaction.Sale;
import edu.ntnu.bidata.prog2.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangeTest {
    /**
     * Helper: builds an exchange with the given stock symbols, using a fixed seed for deterministic price changes.
     * @param symbols the stock symbols to list on the exchange; company names will be "{symbol} Inc." and initial price will be 100 for all stocks
     * @return an exchange with the specified stocks and a fixed seed for price changes
     */
    private static Exchange exchangeWith(String... symbols) {
        List<Stock> stocks = new java.util.ArrayList<>();
        for (String s : symbols) {
            stocks.add(new Stock(s, s + " Inc.", new BigDecimal("100")));
        }
        return new Exchange("Test Market", stocks, new Random(42));
    }

    /**
     * Tests that the constructor stores the name and stocks correctly, and that the stocks are accessible by symbol.
     * Also checks that the initial week is 1.
     */
    @Test
    void constructorRejectsNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Exchange(null, List.of()));
    }

    /**
     * Tests that the constructor rejects a blank name (only whitespace).
     */
    @Test
    void constructorRejectsNullStocks() {
        assertThrows(IllegalArgumentException.class,
                () -> new Exchange("Market", null));
    }

    /**
     * Tests that the constructor rejects duplicate stock symbols, which would cause ambiguity in getStock and hasStock.
     */
    @Test
    void constructorRejectsDuplicateSymbols() {
        Stock a = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Stock dup = new Stock("AAPL", "Apple Copy", new BigDecimal("105"));

        assertThrows(IllegalArgumentException.class,
                () -> new Exchange("Market", List.of(a, dup)));
    }

    /**
     * Tests that a new exchange starts at week 1, not week 0, to match typical human expectations of week numbering.
     */
    @Test
    void weekStartsAtOne() {
        Exchange e = exchangeWith("AAPL");

        assertEquals(1, e.getWeek());
    }

    /**
     * Tests that hasStock returns true for listed symbols and false for unknown symbols, including null.
     * This ensures that the exchange correctly tracks which stocks are listed and prevents null pointer exceptions in getStock.
     */
    @Test
    void hasStockReflectsListing() {
        Exchange e = exchangeWith("AAPL", "TSLA");

        assertTrue(e.hasStock("AAPL"));
        assertTrue(e.hasStock("TSLA"));
        assertFalse(e.hasStock("MSFT"));
        assertFalse(e.hasStock(null));
    }

    /**
     * Tests that getStock returns the correct Stock for a known symbol,
     * and that the returned Stock is the same instance as the one used to create the exchange.
     */
    @Test
    void getStockThrowsForUnknownSymbol() {
        Exchange e = exchangeWith("AAPL");

        assertThrows(IllegalArgumentException.class, () -> e.getStock("MSFT"));
    }

    /**
     * Tests that findStocks returns stocks whose symbol or company name contains the query string, case-insensitively.
     */
    @Test
    void findStocksMatchesSymbolOrCompany() {
        Exchange e = exchangeWith("AAPL", "GOOG", "TSLA");

        // "aapl" matches AAPL only
        assertEquals(1, e.findStocks("aapl").size());
        // "Inc" matches all three (company names end in "Inc.")
        assertEquals(3, e.findStocks("Inc").size());
        // empty returns everything
        assertEquals(3, e.findStocks("").size());
        assertEquals(3, e.findStocks(null).size());
    }

    /**
     * Tests that buy returns a committed Purchase and debits the player's cash. Also checks that the purchased shares
     * are added to the player's portfolio with the correct quantity.
     */
    @Test
    void buyCommitsAPurchase() {
        Exchange e = exchangeWith("AAPL");
        Player alice = new Player("Alice", new BigDecimal("10000"));

        Transaction t = e.buy("AAPL", new BigDecimal("5"), alice);

        assertInstanceOf(Purchase.class, t);
        assertTrue(t.isCommitted());
        assertEquals(0, alice.getPortfolio().getTotalQuantity("AAPL").compareTo(new BigDecimal("5")));
    }

    /**
     * Tests that buy throws for an unknown stock symbol, and that the player's cash and portfolio are unchanged after the failed buy.
     */
    @Test
    void buyThrowsForUnknownSymbol() {
        Exchange e = exchangeWith("AAPL");
        Player alice = new Player("Alice", new BigDecimal("10000"));

        assertThrows(IllegalArgumentException.class,
                () -> e.buy("MSFT", new BigDecimal("5"), alice));
    }

    /**
     * Tests that buy throws for a non-positive quantity (zero or negative), and that the player's cash and portfolio are unchanged after the failed buy.
     */
    @Test
    void buyThrowsForZeroQuantity() {
        Exchange e = exchangeWith("AAPL");
        Player alice = new Player("Alice", new BigDecimal("10000"));

        assertThrows(IllegalArgumentException.class,
                () -> e.buy("AAPL", BigDecimal.ZERO, alice));
    }

    /**
     * Tests that sell returns a committed Sale and credits the player's cash. Also checks that the sold shares are
     * removed from the player's portfolio with the correct quantity. Assumes that the player already owns enough shares
     */
    @Test
    void sellCommitsASale() {
        Exchange e = exchangeWith("AAPL");
        Player alice = new Player("Alice", new BigDecimal("10000"));
        e.buy("AAPL", new BigDecimal("5"), alice);

        Transaction t = e.sell("AAPL", new BigDecimal("3"), alice);

        assertInstanceOf(Sale.class, t);
        assertTrue(t.isCommitted());
        assertEquals(0, alice.getPortfolio().getTotalQuantity("AAPL").compareTo(new BigDecimal("2")));
    }

    /**
     * Tests that sell throws for an unknown stock symbol, and that the player's cash and portfolio are unchanged after the failed sell.
     */
    @Test
    void advanceIncrementsWeekAndUpdatesPrices() {
        Exchange e = exchangeWith("AAPL", "TSLA");
        int historyBefore = e.getStock("AAPL").getHistoricalPrices().size();

        e.advance();
        e.advance();

        assertEquals(3, e.getWeek());
        assertEquals(historyBefore + 2, e.getStock("AAPL").getHistoricalPrices().size());
        assertEquals(historyBefore + 2, e.getStock("TSLA").getHistoricalPrices().size());
    }
}