package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockTest {

    /**
     * Tests that the constructor stores the symbol, company name, and initial price correctly.
     */
    @Test
    void constructorStoresValuesCorrectly() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));

        assertEquals("AAPL", stock.getSymbol());
        assertEquals("Apple", stock.getCompany());
        assertEquals(0, stock.getSalesPrice().compareTo(new BigDecimal("150")));
    }

    /**
     * Tests that a null symbol is rejected.
     */
    @Test
    void constructorRejectsNullSymbol() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock(null, "Apple", new BigDecimal("150")));
    }

    /** Tests that a blank symbol is rejected. */
    @Test
    void constructorRejectsBlankSymbol() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("  ", "Apple", new BigDecimal("150")));
    }

    /**
     * Tests that a null company name is rejected.
     */
    @Test
    void constructorRejectsNullCompany() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", null, new BigDecimal("150")));
    }

    /**
     * Tests that a blank company name is rejected.
     */
    @Test
    void constructorRejectsZeroPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("AAPL", "Apple", BigDecimal.ZERO));
    }

    /**
     * Tests that a negative initial price is rejected.
     */
    @Test
    void initialTrendIsZero() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));

        assertEquals(0.0, stock.getTrend());
    }

    /**
     * Tests that addNewSalesPrice updates the latest price correctly.
     */
    @Test
    void addNewSalesPriceUpdatesLatestPrice() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(0, stock.getSalesPrice().compareTo(new BigDecimal("200")));
    }

    /**
     * Tests that getHighestPrice returns the initial price when only one price exists.
     */
    @Test
    void getHighestPriceWithSinglePrice() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));

        assertEquals(0, stock.getHighestPrice().compareTo(new BigDecimal("150")));
    }

    /**
     * Tests that getLowestPrice returns the initial price when only one price exists.
     */
    @Test
    void getHighestPriceReturnsCorrectValue() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));
        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("120"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(0, stock.getHighestPrice().compareTo(new BigDecimal("200")));
    }

    /**
     * Tests that getLowestPrice returns the initial price when only one price exists.
     */
    @Test
    void getLowestPriceReturnsCorrectValue() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));
        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("120"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(0, stock.getLowestPrice().compareTo(new BigDecimal("120")));
    }

    /**
     * Tests that getLatestPriceChange returns the correct difference between the latest two prices.
     */
    @Test
    void getLatestPriceChangeReturnsCorrectDifference() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));
        stock.addNewSalesPrice(new BigDecimal("180"));

        assertEquals(0, stock.getLatestPriceChange().compareTo(new BigDecimal("30")));
    }

    /**
     * Tests that getLatestPriceChange returns a negative value when the price has fallen since the last update.
     */
    @Test
    void getLatestPriceChangeReturnsNegativeWhenPriceFell() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));
        stock.addNewSalesPrice(new BigDecimal("120"));

        assertEquals(0, stock.getLatestPriceChange().compareTo(new BigDecimal("-30")));
    }

    /**
     * Tests that getLatestPriceChange returns zero when only one price exists (no previous price to compare to).
     */
    @Test
    void getLatestPriceChangeWithSinglePriceReturnsZero() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));

        assertEquals(0, stock.getLatestPriceChange().compareTo(BigDecimal.ZERO));
    }

    /**
     * Tests that getHistoricalPrices returns all recorded prices.
     */
    @Test
    void getHistoricalPricesReturnsAllPrices() {
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("150"));
        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(3, stock.getHistoricalPrices().size());
    }
}