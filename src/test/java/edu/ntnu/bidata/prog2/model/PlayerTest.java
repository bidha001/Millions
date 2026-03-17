package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    public void setMoneyUpdatesPlayerMoney() {

        Player player = new Player("John", new BigDecimal("1000"));

        player.setMoney(new BigDecimal("800"));

        assertEquals(new BigDecimal("800"), player.getMoney());
    }

    @Test
    public void getPortfolioReturnsPlayerPortfolio() {

        Player player = new Player("John", new BigDecimal("1000"));

        Portfolio portfolio = player.getPortfolio();

        assertEquals(portfolio, player.getPortfolio());
    }

    @Test
    public void getNetWorthReturnsCorrectValue() {

        Player player = new Player("John", new BigDecimal("1000"));

        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("150"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("150"));

        player.getPortfolio().addShare(share);

        assertEquals(new BigDecimal("2500"), player.getNetWorth());
    }

    @Test
    public void getStatusReturnsBeginner() {

        Player player = new Player("John", new BigDecimal("1000"));

        assertEquals("Beginner", player.getStatus(5));
    }

    @Test
    public void getStatusReturnsInvestor() {

        Player player = new Player("John", new BigDecimal("1000"));

        assertEquals("Investor", player.getStatus(10));
    }

    @Test
    public void getStatusReturnsSpeculator() {

        Player player = new Player("John", new BigDecimal("1000"));

        assertEquals("Speculator", player.getStatus(20));
    }
}