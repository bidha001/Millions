package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockTest {
    /**
     * Tests that the constructor of the Stock class correctly stores the company name, stock symbol, and initial price.
     */
    @Test
    public void ifConstructorStoresValueCorrectly() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals("Apple", stock.getCompany());
        assertEquals("AAPL", stock.getSymbol());
        assertEquals(new BigDecimal("150"), stock.getSalesPrice());
    }

    /**
     * Tests that the addNewSalesPrice method correctly updates the latest price of the stock.
     */
    @Test
    public void addNewSalesPriceUpdatesLatestPrice() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(new BigDecimal("200"), stock.getSalesPrice());
    }

    /**
     * Tests that the getHighestPrice method returns the correct highest price from the list of prices.
     */
    @Test
    public void getHighestPriceWithSinglePrice() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(new BigDecimal("150"), stock.getHighestPrice());
    }

    /**
     * Tests that the getHighestPrice method returns the correct highest price when multiple prices have been added.
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
     * Tests that the getLowestPrice method returns the correct lowest price from the list of prices.
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
     * Tests that the getLowestPrice method returns the correct lowest price when only a single price is present.
     */
    @Test
    public void getLowestPriceWithSinglePrice() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(new BigDecimal("150"), stock.getLowestPrice());
    }

    /**
     * Tests that the getLatestPriceChange method returns the correct price difference between the last two prices in the list.
     */
    @Test
    public void getLatestPriceChangeReturnsCorrectDifference() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));

        assertEquals(new BigDecimal("30"), stock.getLatestPriceChange());
    }

    /**
     * Tests that the getLatestPriceChange method returns BigDecimal. ZERO when there are fewer than 2 prices in the list.
     */
    @Test
    public void getLatestPriceChangeWithSinglePriceReturnsZero() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(BigDecimal.ZERO, stock.getLatestPriceChange());
    }

    /**
     * Tests that the getHistoricalPrices method returns a list containing all the prices that have been added to the stock.
     */
    @Test
    public void getHistoricalPricesReturnsAllPrices() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(3, stock.getHistoricalPrices().size());
    }
}
