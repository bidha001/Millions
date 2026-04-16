package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StockTest {

    /**
     * Tests that the constructor correctly stores the company name, symbol, and initial price.
     */
    @Test
    public void ifConstructorStoresValueCorrectly() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals("Apple", stock.getCompany());
        assertEquals("AAPL", stock.getSymbol());
        assertEquals(new BigDecimal("150"), stock.getSalesPrice());
    }

    /**
     * Tests that the initial trend is zero when a stock is created.
     */
    @Test
    public void initialTrendIsZero() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(0.0, stock.getTrend());
    }

    /**
     * Tests that addNewSalesPrice correctly updates the latest price of the stock.
     */
    @Test
    public void addNewSalesPriceUpdatesLatestPrice() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(new BigDecimal("200"), stock.getSalesPrice());
    }

    /**
     * Tests that getHighestPrice returns the initial price when no other prices have been added.
     */
    @Test
    public void getHighestPriceWithSinglePrice() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(new BigDecimal("150"), stock.getHighestPrice());
    }

    /**
     * Tests that getHighestPrice returns the correct maximum when multiple prices have been added.
     */
    @Test
    public void getHighestPriceReturnsCorrectValue() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("120"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(new BigDecimal("200"), stock.getHighestPrice());
    }

    /**
     * Tests that getLowestPrice returns the initial price when no other prices have been added.
     */
    @Test
    public void getLowestPriceWithSinglePrice() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(new BigDecimal("150"), stock.getLowestPrice());
    }

    /**
     * Tests that getLowestPrice returns the correct minimum when multiple prices have been added.
     */
    @Test
    public void getLowestPriceReturnsCorrectValue() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("120"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(new BigDecimal("120"), stock.getLowestPrice());
    }

    /**
     * Tests that getLatestPriceChange returns the correct difference between the last two prices.
     */
    @Test
    public void getLatestPriceChangeReturnsCorrectDifference() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));

        assertEquals(new BigDecimal("30"), stock.getLatestPriceChange());
    }

    /**
     * Tests that getLatestPriceChange returns a negative value when the price has fallen.
     */
    @Test
    public void getLatestPriceChangeReturnsNegativeWhenPriceFell() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("120"));

        assertEquals(new BigDecimal("-30"), stock.getLatestPriceChange());
    }

    /**
     * Tests that getLatestPriceChange returns BigDecimal.ZERO when only one price exists.
     */
    @Test
    public void getLatestPriceChangeWithSinglePriceReturnsZero() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(BigDecimal.ZERO, stock.getLatestPriceChange());
    }

    /**
     * Tests that getHistoricalPrices returns all recorded prices in the stock.
     */
    @Test
    public void getHistoricalPricesReturnsAllPrices() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(3, stock.getHistoricalPrices().size());
    }

    /**
     * Tests that getHistoricalPrices returns a defensive copy so the internal list cannot be modified.
     */
    @Test
    public void getHistoricalPricesReturnsDefensiveCopy() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        List<BigDecimal> history = stock.getHistoricalPrices();
        history.add(new BigDecimal("999"));

        assertEquals(1, stock.getHistoricalPrices().size());
    }

    /**
     * Tests that updateTrend always keeps the trend value within the range [-0.05, 0.05].
     */
    @Test
    public void updateTrendKeepsValueWithinBounds() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            stock.updateTrend(random);
            assertTrue(stock.getTrend() >= -0.05);
            assertTrue(stock.getTrend() <= 0.05);
        }
    }
}