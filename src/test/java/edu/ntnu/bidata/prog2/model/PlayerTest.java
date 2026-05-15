package edu.ntnu.bidata.prog2.model;

import edu.ntnu.bidata.prog2.transaction.Purchase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

    /** Tests that the constructor stores the name and starting money. */
    @Test
    void constructorStoresNameAndMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertEquals("Alice", player.getName());
        assertEquals(0, player.getMoney().compareTo(new BigDecimal("10000")));
        assertEquals(0, player.getStartingMoney().compareTo(new BigDecimal("10000")));
    }

    /** Tests that the constructor rejects null name. */
    @Test
    void constructorRejectsNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player(null, new BigDecimal("10000")));
    }

    /** Tests that the constructor rejects blank name. */
    @Test
    void constructorRejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("   ", new BigDecimal("10000")));
    }

    /** Tests that the constructor rejects non-positive starting money. */
    @Test
    void constructorRejectsZeroStartingMoney() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Alice", BigDecimal.ZERO));
    }

    /** Tests that a new player has an empty portfolio. */
    @Test
    void newPlayerHasEmptyPortfolio() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertNotNull(player.getPortfolio());
        assertEquals(0, player.getPortfolio().getShares().size());
    }

    /** Tests that a new player has an empty transaction archive. */
    @Test
    void newPlayerHasEmptyArchive() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertNotNull(player.getArchive());
        assertEquals(0, player.getArchive().getTransactions().size());
    }

    /** Tests that addMoney increases the balance. */
    @Test
    void addMoneyIncreasesBalance() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        player.addMoney(new BigDecimal("2500"));

        assertEquals(0, player.getMoney().compareTo(new BigDecimal("12500")));
    }

    /** Tests that addMoney rejects a non-positive amount. */
    @Test
    void addMoneyRejectsZero() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertThrows(IllegalArgumentException.class,
                () -> player.addMoney(BigDecimal.ZERO));
    }

    /** Tests that withdrawMoney decreases the balance. */
    @Test
    void withdrawMoneyDecreasesBalance() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        player.withdrawMoney(new BigDecimal("2500"));

        assertEquals(0, player.getMoney().compareTo(new BigDecimal("7500")));
    }

    /** Tests that withdrawMoney rejects an amount larger than the balance. */
    @Test
    void withdrawMoneyRejectsOverdraft() {
        Player player = new Player("Alice", new BigDecimal("100"));

        assertThrows(IllegalStateException.class,
                () -> player.withdrawMoney(new BigDecimal("500")));
    }

    /** Tests that withdrawMoney rejects a non-positive amount. */
    @Test
    void withdrawMoneyRejectsZero() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertThrows(IllegalArgumentException.class,
                () -> player.withdrawMoney(BigDecimal.ZERO));
    }

    /** Tests that addMoney and withdrawMoney do not change starting money. */
    @Test
    void moneyOperationsDoNotChangeStartingMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        player.addMoney(new BigDecimal("1000"));
        player.withdrawMoney(new BigDecimal("3000"));

        assertEquals(0, player.getStartingMoney().compareTo(new BigDecimal("10000")));
    }

    /** Tests that getNetWorth equals money when the portfolio is empty. */
    @Test
    void getNetWorthEqualsMoneyWhenPortfolioIsEmpty() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertEquals(0, player.getNetWorth().compareTo(new BigDecimal("10000")));
    }

    /** Tests that getNetWorth sums money and portfolio value. */
    @Test
    void getNetWorthSumsMoneyAndPortfolioValue() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));

        player.getPortfolio().addShare(
                new Share(stock, new BigDecimal("10"), new BigDecimal("100")));

        assertEquals(0, player.getNetWorth().compareTo(new BigDecimal("11000")));
    }

    /** Tests that getStatus returns Novice for a fresh player with no trades. */
    @Test
    void getStatusReturnsNoviceForNewPlayer() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertEquals("Novice", player.getStatus());
    }

    /** Tests that getStatus returns Novice when net worth doubled but no trades happened. */
    @Test
    void getStatusReturnsNoviceWithoutTradingWeeks() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        player.addMoney(new BigDecimal("10000")); // doubled, but no archive entries

        assertEquals("Novice", player.getStatus());
    }

    /** Tests that getStatus returns Investor at 10+ trading weeks with at least 20% gain. */
    @Test
    void getStatusReturnsInvestorAfter10TradingWeeksWith20PercentGain() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("10"));

        // commit small buys in 10 distinct weeks → 10 trading weeks, ~10 * 10.05 ≈ 100.50 spent
        for (int week = 1; week <= 10; week++) {
            new Purchase(new Share(stock, new BigDecimal("1"), new BigDecimal("10")), week).commit(player);
        }
        // engineer a 25% gain via market price rising
        stock.addNewSalesPrice(new BigDecimal("260")); // 10 shares × 260 = 2 600 portfolio value
        // money is ~9 899.50 + portfolio 2 600 = 12 499.50 → 24.99% gain

        assertEquals("Investor", player.getStatus());
    }

    /** Tests that getStatus returns Novice at 10+ trading weeks if gain is below 20%. */
    @Test
    void getStatusReturnsNoviceWhenGainIsBelow20Percent() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("10"));

        for (int week = 1; week <= 10; week++) {
            new Purchase(new Share(stock, new BigDecimal("1"), new BigDecimal("10")), week).commit(player);
        }
        // No price rise — net worth stays roughly equal to starting money (slightly less due to commission)
        assertEquals("Novice", player.getStatus());
    }

    /** Tests that 19.5% gain is NOT enough — rounding must not let it qualify. */
    @Test
    void getStatusReturnsNoviceAt19_5PercentGain() {
        Player player = new Player("Alice", new BigDecimal("10000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("10"));

        for (int week = 1; week <= 10; week++) {
            new Purchase(new Share(stock, new BigDecimal("1"), new BigDecimal("10")), week).commit(player);
        }
        // Engineer ~19.5% gain
        stock.addNewSalesPrice(new BigDecimal("205")); // 10 × 205 = 2 050; money ~9 899.5 → ~11 949.5 → ~19.495% gain

        assertEquals("Novice", player.getStatus());
    }

    /** Tests that getStatus returns Speculator at 20+ trading weeks with doubled money. */
    @Test
    void getStatusReturnsSpeculatorAfter20TradingWeeksWithDoubledMoney() {
        Player player = new Player("Alice", new BigDecimal("100000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("10"));

        for (int week = 1; week <= 20; week++) {
            new Purchase(new Share(stock, new BigDecimal("1"), new BigDecimal("10")), week).commit(player);
        }
        // Big price jump so 20 × X + remaining cash ≥ 200 000
        stock.addNewSalesPrice(new BigDecimal("5000")); // 20 × 5 000 = 100 000 portfolio
        // money ~99 899 + portfolio 100 000 = ~199 899 — just under double. Bump price a bit more.
        stock.addNewSalesPrice(new BigDecimal("5050")); // 20 × 5 050 = 101 000 → total ~200 899 → doubled+

        assertEquals("Speculator", player.getStatus());
    }
}