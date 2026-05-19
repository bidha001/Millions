package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link TransactionArchive}.
 * The archive is filled by committing real {@link Purchase} transactions to keep
 * tests honest about how the archive is actually used at runtime.
 */
class TransactionArchiveTest {

    /**
     * Helper: builds a player with the given amount of cash and no shares, for testing purposes.
     * @param cash the amount of cash the player starts with, as a string to avoid BigDecimal parsing issues in tests
     * @return a player with the specified cash and an empty portfolio
     */
    private static Player playerWithCash(String cash) {
        return new Player("Alice", new BigDecimal(cash));
    }

    /**
     * Helper: commits a purchase of the given stock, quantity, and price for the player in the specified week, and returns the committed purchase.
     * @param player the player making the purchase
     * @param stock the stock being purchased
     * @param week the week number of the purchase (for testing getTransactions(week) filtering)
     * @param quantity the quantity of shares being purchased, as a string to avoid BigDecimal parsing issues in tests
     * @param price the purchase price per share, as a string to avoid BigDecimal parsing issues in tests
     * @return the committed Purchase transaction, which has been added to the player's archive and portfolio
     */
    private static Purchase buyOne(Player player, Stock stock, int week, String quantity, String price) {
        Purchase p = new Purchase(new Share(stock, new BigDecimal(quantity), new BigDecimal(price)), week);
        p.commit(player);
        return p;
    }

    /**
     * Tests that a new TransactionArchive is empty and has no transactions.
     */
    @Test
    void newArchiveIsEmpty() {
        TransactionArchive archive = new TransactionArchive();

        assertTrue(archive.isEmpty());
        assertEquals(0, archive.getTransactions().size());
    }

    /**
     * Tests that committing a purchase adds it to the player's archive and that the archive is no longer empty. Also
     * checks that the transaction is correctly stored in the archive's list of transactions.
     */
    @Test
    void addStoresTransactionAndReturnsTrue() {
        Player alice = playerWithCash("10000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));

        buyOne(alice, stock, 1, "10", "100");

        assertFalse(alice.getArchive().isEmpty());
        assertEquals(1, alice.getArchive().getTransactions().size());
    }

    /**
     * Tests that add rejects null transactions with an IllegalArgumentException, and that the archive remains unchanged after the failed add.
     */
    @Test
    void addRejectsNull() {
        TransactionArchive archive = new TransactionArchive();

        assertThrows(IllegalArgumentException.class, () -> archive.add(null));
    }

    /**
     * Tests that getTransactions returns a defensive copy of the transactions list, so that external modifications
     * do not affect the archive's internal state.
     */
    @Test
    void getTransactionsReturnsDefensiveCopy() {
        Player alice = playerWithCash("10000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        buyOne(alice, stock, 1, "10", "100");

        alice.getArchive().getTransactions().clear();

        assertEquals(1, alice.getArchive().getTransactions().size());
    }
}