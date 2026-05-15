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

    /** Helper: builds a player with given starting money and a stock at the given price. */
    private static Player playerWithCash(String cash) {
        return new Player("Alice", new BigDecimal(cash));
    }

    /** Helper: commits a single Purchase to the player and returns the archived transaction. */
    private static Purchase buyOne(Player player, Stock stock, int week, String quantity, String price) {
        Purchase p = new Purchase(new Share(stock, new BigDecimal(quantity), new BigDecimal(price)), week);
        p.commit(player);
        return p;
    }

    /** Tests that a new archive is empty. */
    @Test
    void newArchiveIsEmpty() {
        TransactionArchive archive = new TransactionArchive();

        assertTrue(archive.isEmpty());
        assertEquals(0, archive.getTransactions().size());
    }

    /** Tests that add stores the transaction and returns true. */
    @Test
    void addStoresTransactionAndReturnsTrue() {
        Player alice = playerWithCash("10000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));

        buyOne(alice, stock, 1, "10", "100");

        assertFalse(alice.getArchive().isEmpty());
        assertEquals(1, alice.getArchive().getTransactions().size());
    }

    /** Tests that add rejects null. */
    @Test
    void addRejectsNull() {
        TransactionArchive archive = new TransactionArchive();

        assertThrows(IllegalArgumentException.class, () -> archive.add(null));
    }

    /** Tests that getTransactions returns a defensive copy. */
    @Test
    void getTransactionsReturnsDefensiveCopy() {
        Player alice = playerWithCash("10000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        buyOne(alice, stock, 1, "10", "100");

        alice.getArchive().getTransactions().clear();

        assertEquals(1, alice.getArchive().getTransactions().size());
    }

    /** Tests that getTransactions(week) returns only transactions from that week. */
    @Test
    void getTransactionsByWeekFiltersCorrectly() {
        Player alice = playerWithCash("100000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        buyOne(alice, stock, 1, "1", "100");
        buyOne(alice, stock, 1, "2", "100");
        buyOne(alice, stock, 2, "3", "100");

        List<Transaction> week1 = alice.getArchive().getTransactions(1);
        List<Transaction> week2 = alice.getArchive().getTransactions(2);
        List<Transaction> week3 = alice.getArchive().getTransactions(3);

        assertEquals(2, week1.size());
        assertEquals(1, week2.size());
        assertEquals(0, week3.size());
    }

    /** Tests that getPurchases(week) returns purchases only. */
    @Test
    void getPurchasesByWeekReturnsPurchasesOnly() {
        Player alice = playerWithCash("100000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        buyOne(alice, stock, 1, "5", "100");
        buyOne(alice, stock, 1, "5", "100");

        List<Purchase> purchases = alice.getArchive().getPurchases(1);

        assertEquals(2, purchases.size());
    }

    /** Tests that getSales(week) returns sales only, separate from purchases in the same week. */
    @Test
    void getSalesByWeekReturnsSalesOnly() {
        Player alice = playerWithCash("100000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        buyOne(alice, stock, 1, "10", "100");

        Sale sale = new Sale(stock, new BigDecimal("4"), 1);
        sale.commit(alice);

        List<Sale> sales = alice.getArchive().getSales(1);
        List<Purchase> purchases = alice.getArchive().getPurchases(1);

        assertEquals(1, sales.size());
        assertEquals(1, purchases.size());
    }

    /** Tests that countDistinctWeeks returns the number of weeks with at least one transaction. */
    @Test
    void countDistinctWeeksCountsUniqueWeeks() {
        Player alice = playerWithCash("100000");
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));
        buyOne(alice, stock, 1, "1", "100");
        buyOne(alice, stock, 1, "1", "100"); // same week — does not double-count
        buyOne(alice, stock, 3, "1", "100");
        buyOne(alice, stock, 5, "1", "100");

        assertEquals(3, alice.getArchive().countDistinctWeeks());
    }

    /** Tests that countDistinctWeeks is zero for an empty archive. */
    @Test
    void countDistinctWeeksIsZeroForEmptyArchive() {
        TransactionArchive archive = new TransactionArchive();

        assertEquals(0, archive.countDistinctWeeks());
    }
}