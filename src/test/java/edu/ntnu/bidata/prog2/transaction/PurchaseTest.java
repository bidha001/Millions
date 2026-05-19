package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseTest {

    private static Stock stock() {
        return new Stock("AAPL", "Apple", new BigDecimal("100"));
    }

    /**
     * Tests that commit deducts the correct amount of money, adds the share to the player's portfolio,
     * and archives the transaction. Also checks that isCommitted() returns true after a successful commit.
     */
    @Test
    void commitDeductsMoneyAddsShareAndArchives() {
        Player alice = new Player("Alice", new BigDecimal("10000"));
        Stock s = stock();
        Purchase p = new Purchase(new Share(s, new BigDecimal("10"), new BigDecimal("100")), 1);

        p.commit(alice);

        // Gross 1000 + commission 5 = total 1005 ; balance 10000 - 1005 = 8995
        assertEquals(0, alice.getMoney().compareTo(new BigDecimal("8995")));
        assertEquals(1, alice.getPortfolio().getShares().size());
        assertEquals(0, alice.getPortfolio().getTotalQuantity("AAPL").compareTo(new BigDecimal("10")));
        assertEquals(1, alice.getArchive().getTransactions().size());
        assertTrue(p.isCommitted());
    }

    /**
     * Tests that commit rejects when the player has insufficient funds to cover the total cost
     * of the purchase (gross + commission).
     */
    @Test
    void commitRejectsWhenInsufficientFunds() {
        Player alice = new Player("Alice", new BigDecimal("500"));
        Stock s = stock();
        Purchase p = new Purchase(new Share(s, new BigDecimal("10"), new BigDecimal("100")), 1);

        assertThrows(IllegalStateException.class, () -> p.commit(alice));

        // No side effects when commit fails.
        assertEquals(0, alice.getMoney().compareTo(new BigDecimal("500")));
        assertEquals(0, alice.getPortfolio().getShares().size());
        assertTrue(alice.getArchive().isEmpty());
        assertFalse(p.isCommitted());
    }

    /**
     * Tests that commit rejects if called more than once on the same Purchase, and that the side effects of the first
     * commit are preserved exactly once.
     */
    @Test
    void commitRejectsDoubleCommit() {
        Player alice = new Player("Alice", new BigDecimal("10000"));
        Stock s = stock();
        Purchase p = new Purchase(new Share(s, new BigDecimal("10"), new BigDecimal("100")), 1);

        p.commit(alice);

        assertThrows(IllegalStateException.class, () -> p.commit(alice));

        // Side effects of the first commit are preserved exactly once.
        assertEquals(1, alice.getArchive().getTransactions().size());
        assertEquals(0, alice.getMoney().compareTo(new BigDecimal("8995")));
    }
}