package edu.ntnu.bidata.prog2.model;

import edu.ntnu.bidata.prog2.transaction.Purchase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

    /**
     * Tests that the constructor stores the name and starting money.
     */
    @Test
    void constructorStoresNameAndMoney() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertEquals("Alice", player.getName());
        assertEquals(0, player.getMoney().compareTo(new BigDecimal("10000")));
        assertEquals(0, player.getStartingMoney().compareTo(new BigDecimal("10000")));
    }

    /**
     * Tests that the constructor rejects null name.
     */
    @Test
    void constructorRejectsNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player(null, new BigDecimal("10000")));
    }

    /**
     * Tests that the constructor rejects blank name (only whitespace).
     */
    @Test
    void constructorRejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("   ", new BigDecimal("10000")));
    }

    /**
     * Tests that the constructor rejects null starting money.
     */
    @Test
    void constructorRejectsZeroStartingMoney() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Alice", BigDecimal.ZERO));
    }

    /**
     * Tests that the constructor rejects negative starting money.
     */
    @Test
    void newPlayerHasEmptyPortfolio() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertNotNull(player.getPortfolio());
        assertEquals(0, player.getPortfolio().getShares().size());
    }

    /**
     * Tests that the constructor initializes an empty transaction archive.
     */
    @Test
    void newPlayerHasEmptyArchive() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertNotNull(player.getArchive());
        assertEquals(0, player.getArchive().getTransactions().size());
    }

    /**
     * Tests that addMoney increases the balance by the specified amount.
     */
    @Test
    void addMoneyIncreasesBalance() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        player.addMoney(new BigDecimal("2500"));

        assertEquals(0, player.getMoney().compareTo(new BigDecimal("12500")));
    }

    /**
     * Tests that addMoney rejects a non-positive amount (zero or negative).
     */
    @Test
    void addMoneyRejectsZero() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        assertThrows(IllegalArgumentException.class,
                () -> player.addMoney(BigDecimal.ZERO));
    }

    /**
     * Tests that withdrawMoney decreases the balance by the specified amount, as long as it does not exceed the current balance.
     */
    @Test
    void withdrawMoneyDecreasesBalance() {
        Player player = new Player("Alice", new BigDecimal("10000"));

        player.withdrawMoney(new BigDecimal("2500"));

        assertEquals(0, player.getMoney().compareTo(new BigDecimal("7500")));
    }
}