package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockTest {
    @Test
    public void ifConstructorStoresValueCorrectly() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals("Apple", stock.getCompany());
        assertEquals("AAPL", stock.getSymbol());
        assertEquals(new BigDecimal("150"), stock.getSalesPrice());
    }

    @Test
    public void addNewSalesPriceUpdatesLatestPrice() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(new BigDecimal("200"), stock.getSalesPrice());
    }

    @Test
    public void getHighestPriceReturnsCorrectValue() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("120"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(new BigDecimal("200"), stock.getHighestPrice());
    }

    @Test
    public void getLowestPriceReturnsCorrectValue() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));
        stock.addNewSalesPrice(new BigDecimal("120"));
        stock.addNewSalesPrice(new BigDecimal("200"));

        assertEquals(new BigDecimal("120"), stock.getLowestPrice());
    }

    @Test
    public void getLowestPriceWithSinglePrice() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(new BigDecimal("150"), stock.getLowestPrice());
    }

    @Test
    public void getLatestPriceChangeReturnsCorrectDifference() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        stock.addNewSalesPrice(new BigDecimal("180"));

        assertEquals(new BigDecimal("30"), stock.getLatestPriceChange());
    }

    @Test
    public void getLatestPriceChangeWithSinglePriceReturnsZero() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        assertEquals(BigDecimal.ZERO, stock.getLatestPriceChange());
    }
}
