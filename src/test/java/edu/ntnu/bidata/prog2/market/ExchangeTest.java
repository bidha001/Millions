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

    private static Exchange exchangeWith(String... symbols) {
        List<Stock> stocks = new java.util.ArrayList<>();
        for (String s : symbols) {
            stocks.add(new Stock(s, s + " Inc.", new BigDecimal("100")));
        }
        return new Exchange("Test Market", stocks, new Random(42));
    }

    /** Tests that the constructor rejects null name. */
    @Test
    void constructorRejectsNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Exchange(null, List.of()));
    }

    /** Tests that the constructor rejects null stocks list. */
    @Test
    void constructorRejectsNullStocks() {
        assertThrows(IllegalArgumentException.class,
                () -> new Exchange("Market", null));
    }

    /** Tests that the constructor rejects duplicate symbols. */
    @Test
    void constructorRejectsDuplicateSymbols() {
        Stock a = new Stock("AAPL", "Apple", new BigDecimal("100"));
        Stock dup = new Stock("AAPL", "Apple Copy", new BigDecimal("105"));

        assertThrows(IllegalArgumentException.class,
                () -> new Exchange("Market", List.of(a, dup)));
    }

    /** Tests that getWeek starts at 1. */
    @Test
    void weekStartsAtOne() {
        Exchange e = exchangeWith("AAPL");

        assertEquals(1, e.getWeek());
    }

    /** Tests that hasStock returns true for listed symbols and false otherwise. */
    @Test
    void hasStockReflectsListing() {
        Exchange e = exchangeWith("AAPL", "TSLA");

        assertTrue(e.hasStock("AAPL"));
        assertTrue(e.hasStock("TSLA"));
        assertFalse(e.hasStock("MSFT"));
        assertFalse(e.hasStock(null));
    }

    /** Tests that getStock throws for unknown symbols. */
    @Test
    void getStockThrowsForUnknownSymbol() {
        Exchange e = exchangeWith("AAPL");

        assertThrows(IllegalArgumentException.class, () -> e.getStock("MSFT"));
    }

    /** Tests that findStocks matches symbol or company, case-insensitively. */
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

    /** Tests that buy returns a committed Purchase and credits the player's portfolio. */
    @Test
    void buyCommitsAPurchase() {
        Exchange e = exchangeWith("AAPL");
        Player alice = new Player("Alice", new BigDecimal("10000"));

        Transaction t = e.buy("AAPL", new BigDecimal("5"), alice);

        assertInstanceOf(Purchase.class, t);
        assertTrue(t.isCommitted());
        assertEquals(0, alice.getPortfolio().getTotalQuantity("AAPL").compareTo(new BigDecimal("5")));
    }

    /** Tests that buy throws for an unknown symbol. */
    @Test
    void buyThrowsForUnknownSymbol() {
        Exchange e = exchangeWith("AAPL");
        Player alice = new Player("Alice", new BigDecimal("10000"));

        assertThrows(IllegalArgumentException.class,
                () -> e.buy("MSFT", new BigDecimal("5"), alice));
    }

    /** Tests that buy throws for non-positive quantity. */
    @Test
    void buyThrowsForZeroQuantity() {
        Exchange e = exchangeWith("AAPL");
        Player alice = new Player("Alice", new BigDecimal("10000"));

        assertThrows(IllegalArgumentException.class,
                () -> e.buy("AAPL", BigDecimal.ZERO, alice));
    }

    /** Tests that sell returns a committed Sale and credits the player's cash. */
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

    /** Tests that advance increments the week and adds one price entry to each stock. */
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

    /** Tests that advance never lets a stock price drop below 1.00. */
    @Test
    void advanceClampsPriceAtOne() {
        Exchange e = exchangeWith("AAPL");
        Stock aapl = e.getStock("AAPL");

        // Run many weeks with a fixed seed; price must never go below 1.00.
        for (int i = 0; i < 200; i++) {
            e.advance();
            assertTrue(aapl.getSalesPrice().compareTo(BigDecimal.ONE) >= 0,
                    "Price dropped below 1.00 at week " + e.getWeek());
        }
    }

    /** Tests that getGainers returns stocks sorted by price change descending. */
    @Test
    void getGainersSortsDescending() {
        Exchange e = exchangeWith("AAA", "BBB", "CCC");
        // Manually set price changes by appending new prices
        e.getStock("AAA").addNewSalesPrice(new BigDecimal("110")); // +10
        e.getStock("BBB").addNewSalesPrice(new BigDecimal("130")); // +30
        e.getStock("CCC").addNewSalesPrice(new BigDecimal("90"));  // -10

        List<Stock> gainers = e.getGainers(3);

        assertEquals("BBB", gainers.get(0).getSymbol());
        assertEquals("AAA", gainers.get(1).getSymbol());
        assertEquals("CCC", gainers.get(2).getSymbol());
    }

    /** Tests that getLosers returns stocks sorted by price change ascending. */
    @Test
    void getLosersSortsAscending() {
        Exchange e = exchangeWith("AAA", "BBB", "CCC");
        e.getStock("AAA").addNewSalesPrice(new BigDecimal("110"));
        e.getStock("BBB").addNewSalesPrice(new BigDecimal("130"));
        e.getStock("CCC").addNewSalesPrice(new BigDecimal("90"));

        List<Stock> losers = e.getLosers(3);

        assertEquals("CCC", losers.get(0).getSymbol());
        assertEquals("AAA", losers.get(1).getSymbol());
        assertEquals("BBB", losers.get(2).getSymbol());
    }

    /** Tests that getGainers respects the limit. */
    @Test
    void getGainersRespectsLimit() {
        Exchange e = exchangeWith("AAA", "BBB", "CCC");
        e.getStock("AAA").addNewSalesPrice(new BigDecimal("110"));
        e.getStock("BBB").addNewSalesPrice(new BigDecimal("130"));
        e.getStock("CCC").addNewSalesPrice(new BigDecimal("120"));

        assertEquals(2, e.getGainers(2).size());
    }

    /** Tests that getGainers rejects a negative limit. */
    @Test
    void getGainersRejectsNegativeLimit() {
        Exchange e = exchangeWith("AAPL");

        assertThrows(IllegalArgumentException.class, () -> e.getGainers(-1));
    }

    /** Tests that getGainers with limit 0 returns an empty list. */
    @Test
    void getGainersWithZeroLimitReturnsEmpty() {
        Exchange e = exchangeWith("AAPL");

        assertTrue(e.getGainers(0).isEmpty());
    }
}