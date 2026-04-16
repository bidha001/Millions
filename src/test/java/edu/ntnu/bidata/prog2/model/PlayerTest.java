package edu.ntnu.bidata.prog2.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerTest {

    /**
     * Tests that the constructor correctly stores the name and starting money.
     */
    @Test
    public void constructorStoresNameAndMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertEquals("Alice", player.getName());
        assertEquals(new BigDecimal("10000"), player.getMoney());
        assertEquals(new BigDecimal("10000"), player.getStartingMoney());
    }

    /**
     * Tests that a new player has an empty portfolio.
     */
    @Test
    public void newPlayerHasEmptyPortfolio() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertNotNull(player.getPortfolio());
        assertEquals(0, player.getPortfolio().getAllShares().size());
    }

    /**
     * Tests that a new player has an empty transaction archive.
     */
    @Test
    public void newPlayerHasEmptyArchive() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertNotNull(player.getArchive());
        assertEquals(0, player.getArchive().getTransactions().size());
    }

    /**
     * Tests that setMoney correctly updates the player's current money.
     */
    @Test
    public void setMoneyUpdatesCurrentMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        player.setMoney(new BigDecimal("7500"));

        assertEquals(new BigDecimal("7500"), player.getMoney());
    }

    /**
     * Tests that setMoney does not change the player's starting money.
     */
    @Test
    public void setMoneyDoesNotChangeStartingMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        player.setMoney(new BigDecimal("5000"));

        assertEquals(new BigDecimal("10000"), player.getStartingMoney());
    }

    /**
     * Tests that getNetWorth returns the current money when the portfolio is empty.
     */
    @Test
    public void getNetWorthEqualsMoneyWhenPortfolioIsEmpty() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertEquals(0, player.getNetWorth().compareTo(new BigDecimal("10000")));
    }

    /**
     * Tests that getNetWorth correctly sums money and portfolio value.
     */
    @Test
    public void getNetWorthSumsMoneyAndPortfolioValue() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));

        player.getPortfolio().addShare(
                new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null));

        // money 10000 + portfolio value (10 * 100 = 1000) = 11000
        assertEquals(0, player.getNetWorth().compareTo(new BigDecimal("11000")));
    }

    /**
     * Tests that getStatus returns "Novice" for a new player at week 1.
     */
    @Test
    public void getStatusReturnsNoviceForNewPlayer() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertEquals("Novice", player.getStatus(1));
    }

    /**
     * Tests that getStatus returns "Novice" when less than 10 weeks have passed, even with gains.
     */
    @Test
    public void getStatusReturnsNoviceWhenFewerThan10Weeks() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        player.setMoney(new BigDecimal("20000")); // doubled, but only at week 5

        assertEquals("Novice", player.getStatus(5));
    }

    /**
     * Tests that getStatus returns "Investor" when player has 10+ weeks and at least 20% gain.
     */
    @Test
    public void getStatusReturnsInvestorAfter10WeeksWith20PercentGain() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        player.setMoney(new BigDecimal("12500")); // 25% increase

        assertEquals("Investor", player.getStatus(10));
    }

    /**
     * Tests that getStatus returns "Novice" after 10 weeks if the gain is less than 20%.
     */
    @Test
    public void getStatusReturnsNoviceWhenGainIsBelow20Percent() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        player.setMoney(new BigDecimal("11000")); // only 10% increase

        assertEquals("Novice", player.getStatus(10));
    }

    /**
     * Tests that getStatus returns "Speculator" when player has 20+ weeks and has at least doubled money.
     */
    @Test
    public void getStatusReturnsSpeculatorAfter20WeeksWithDoubledMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        player.setMoney(new BigDecimal("20000")); // doubled

        assertEquals("Speculator", player.getStatus(20));
    }

    /**
     * Tests that getStatus returns "Investor" at 20+ weeks if money is not doubled but gain is 20%+.
     */
    @Test
    public void getStatusReturnsInvestorWhen20WeeksButNotDoubled() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        player.setMoney(new BigDecimal("15000")); // 50% gain, not doubled

        assertEquals("Investor", player.getStatus(20));
    }

    /**
     * Tests that getStatus returns "Novice" when the player has lost money.
     */
    @Test
    public void getStatusReturnsNoviceWhenPlayerLostMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        player.setMoney(new BigDecimal("8000")); // 20% loss

        assertEquals("Novice", player.getStatus(15));
    }
}