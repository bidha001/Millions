package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PortfolioTest {

    @Test
    public void addShareAddsShareToPortfolio() {

        Portfolio portfolio = new Portfolio();

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("150"), null);

        portfolio.addShare(share);

        assertEquals(1, portfolio.getAllShares().size());
    }

    @Test
    public void containsShareReturnsTrueIfShareExists() {

        Portfolio portfolio = new Portfolio();

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("150"), null);

        portfolio.addShare(share);

        assertTrue(portfolio.containsShare(share));
    }

    @Test
    public void removeShareRemovesShareFromPortfolio() {

        Portfolio portfolio = new Portfolio();

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("150"), null);

        portfolio.addShare(share);
        portfolio.removeShare(share);

        assertEquals(0, portfolio.getAllShares().size());
    }

    @Test
    public void getTotalQuantityReturnsCorrectSum() {

        Portfolio portfolio = new Portfolio();

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));

        Share share1 = new Share(stock, new BigDecimal("10"), new BigDecimal("150"), null);
        Share share2 = new Share(stock, new BigDecimal("5"), new BigDecimal("150"), null);

        portfolio.addShare(share1);
        portfolio.addShare(share2);

        assertEquals(new BigDecimal("15"), portfolio.getTotalQuantity("AAPL"));
    }

}