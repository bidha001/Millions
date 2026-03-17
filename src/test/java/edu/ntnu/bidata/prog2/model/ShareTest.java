package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShareTest {

    /**
     * Test that the getStock method returns the correct Stock object associated with the Share.
     */
    @Test
    public void getStockReturnsCorrectStock() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        Share share = new Share(stock, new BigDecimal("5"), new BigDecimal("150"));

        assertEquals(stock, share.getStock());
    }

    /**
     * Test that the getQuantity method returns the correct quantity of shares owned.
     */
    @Test
    public void getQuantityReturnsCorrectQuantity() {

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        Share share = new Share(stock, new BigDecimal("20"), new BigDecimal("150"));

        assertEquals(new BigDecimal("20"), share.getQuantity());
    }
}