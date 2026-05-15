package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.TransactionCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaleTest {

    /** Helper: gives Alice a single lot of {qty} AAPL at {price}, then bumps the market price to {marketPrice}. */
    private static Player aliceWithLot(String qty, String price, String marketPrice) {
        Player alice = new Player("Alice", new BigDecimal("100000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal(price));
        new Purchase(new Share(stock, new BigDecimal(qty), new BigDecimal(price)), 1).commit(alice);
        stock.addNewSalesPrice(new BigDecimal(marketPrice));
        return alice;
    }

    /** Tests that a profitable sale of one lot credits gross - commission - tax. */
    @Test
    void commitProfitableSingleLot() {
        Player alice = aliceWithLot("10", "100", "150");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();
        BigDecimal before = alice.getMoney();

        Sale sale = new Sale(stock, new BigDecimal("10"), 2);
        sale.commit(alice);

        // gross 1500, commission 15, profit 485, tax 145.50, net 1339.50
        assertEquals(0, alice.getMoney().subtract(before).compareTo(new BigDecimal("1339.50")));
        assertEquals(0, alice.getPortfolio().getTotalQuantity("AAPL").compareTo(BigDecimal.ZERO));
        assertTrue(sale.isCommitted());
    }

    /** Tests that the post-commit calculator reports the actual recorded totals (the receipt-bug guard). */
    @Test
    void postCommitCalculatorReportsActualTotals() {
        Player alice = aliceWithLot("10", "100", "150");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();

        Sale sale = new Sale(stock, new BigDecimal("10"), 2);
        sale.commit(alice);

        TransactionCalculator calc = sale.getCalculator();
        assertEquals(0, calc.calculateGross().compareTo(new BigDecimal("1500.00")));
        assertEquals(0, calc.calculateCommission().compareTo(new BigDecimal("15.00")));
        assertEquals(0, calc.calculateTax().compareTo(new BigDecimal("145.50")));
        assertEquals(0, calc.calculateTotal().compareTo(new BigDecimal("1339.50")));
    }

    /** Tests that a sale at a loss has tax = 0 and credits gross - commission. */
    @Test
    void commitLossyTaxIsZero() {
        Player alice = aliceWithLot("10", "100", "80");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();
        BigDecimal before = alice.getMoney();

        Sale sale = new Sale(stock, new BigDecimal("10"), 2);
        sale.commit(alice);

        // gross 800, commission 8, tax 0, net 792
        assertEquals(0, alice.getMoney().subtract(before).compareTo(new BigDecimal("792.00")));
        assertEquals(0, sale.getCalculator().calculateTax().compareTo(BigDecimal.ZERO));
    }

    /** Tests that a FIFO sale across two lots taxes per lot (a loss does NOT offset a profit). */
    @Test
    void commitFifoTaxesPerLotIndependently() {
        Player alice = new Player("Alice", new BigDecimal("100000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal("100"));

        // Lot 1: bought 5 @ 100 → tax basis 100
        new Purchase(new Share(stock, new BigDecimal("5"), new BigDecimal("100")), 1).commit(alice);
        // Lot 2: bought 5 @ 200 → tax basis 200
        new Purchase(new Share(stock, new BigDecimal("5"), new BigDecimal("200")), 2).commit(alice);

        // Market now at 150: lot 1 is profitable (sold at 150 vs bought at 100),
        // lot 2 is at a loss (sold at 150 vs bought at 200).
        stock.addNewSalesPrice(new BigDecimal("150"));

        Sale sale = new Sale(stock, new BigDecimal("10"), 3);
        sale.commit(alice);

        // Per-lot:
        //   Lot 1: gross 750, commission 7.50, profit 750 - 7.50 - 500 = 242.50, tax 72.75, net 669.75
        //   Lot 2: gross 750, commission 7.50, profit 750 - 7.50 - 1000 = -257.50 → tax 0, net 742.50
        // Totals: gross 1500, commission 15, tax 72.75, net 1412.25
        TransactionCalculator c = sale.getCalculator();
        assertEquals(0, c.calculateGross().compareTo(new BigDecimal("1500.00")));
        assertEquals(0, c.calculateCommission().compareTo(new BigDecimal("15.00")));
        assertEquals(0, c.calculateTax().compareTo(new BigDecimal("72.75")));
        assertEquals(0, c.calculateTotal().compareTo(new BigDecimal("1412.25")));
    }

    /** Tests that trying to sell more than owned is rejected. */
    @Test
    void commitRejectsWhenNotEnoughShares() {
        Player alice = aliceWithLot("5", "100", "100");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();

        Sale sale = new Sale(stock, new BigDecimal("10"), 2);

        assertThrows(IllegalStateException.class, () -> sale.commit(alice));
        assertEquals(0, alice.getPortfolio().getTotalQuantity("AAPL").compareTo(new BigDecimal("5")));
    }

    /** Tests that the same Sale cannot be committed twice. */
    @Test
    void commitRejectsDoubleCommit() {
        Player alice = aliceWithLot("10", "100", "150");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();
        Sale sale = new Sale(stock, new BigDecimal("10"), 2);

        sale.commit(alice);

        assertThrows(IllegalStateException.class, () -> sale.commit(alice));
    }

    /** Tests that a partial sale leaves a remainder lot with the original purchase price. */
    @Test
    void partialSaleLeavesRemainderLotWithOriginalPrice() {
        Player alice = aliceWithLot("10", "100", "150");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();

        new Sale(stock, new BigDecimal("4"), 2).commit(alice);

        assertEquals(1, alice.getPortfolio().getShares().size());
        Share remaining = alice.getPortfolio().getShares().getFirst();
        assertEquals(0, remaining.getQuantity().compareTo(new BigDecimal("6")));
        assertEquals(0, remaining.getPurchasePrice().compareTo(new BigDecimal("100")));
    }
}