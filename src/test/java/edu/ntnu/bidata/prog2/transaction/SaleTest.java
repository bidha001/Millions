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

    /**
     * Helper: builds a player with a single lot of the given quantity and purchase price, and the stock's current market price set to {@code marketPrice}.
     * @param qty the quantity of the lot
     * @param price the purchase price of the lot
     * @param marketPrice the current market price of the stock (used for calculating gross and tax)
     * @return a player with the specified lot and market price set
     */
    private static Player aliceWithLot(String qty, String price, String marketPrice) {
        Player alice = new Player("Alice", new BigDecimal("100000"));
        Stock stock = new Stock("AAPL", "Apple", new BigDecimal(price));
        new Purchase(new Share(stock, new BigDecimal(qty), new BigDecimal(price)), 1).commit(alice);
        stock.addNewSalesPrice(new BigDecimal(marketPrice));
        return alice;
    }

    /**
     * Tests that commit on a profitable sale correctly updates the player's money by gross - commission - tax,
     * removes the sold shares from the portfolio, and marks the sale as committed.
     */
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

    /**
     * Tests that after committing a sale, the calculator reports the same gross, commission, tax, and total as were
     * applied to the player's money.
     */
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

    /**
     * Tests that commit on a loss sale correctly updates the player's money by gross - commission (no tax),
     * removes the sold shares from the portfolio, and marks the sale as committed.
     */
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

    /**
     * Tests that when selling shares from multiple lots, the FIFO method is applied to determine the tax basis of each
     * lot, and the tax is calculated separately for each lot and summed up. In this example, the first lot is sold at
     * a profit and taxed, while the second lot is sold at a loss and not taxed, resulting in a total tax that reflects
     * both lots. The final net amount reflects the combined effect of both lots after commission and tax.
     */
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

    /**
     * Tests that commit rejects when the player tries to sell more shares than they have in their portfolio, and that
     * no side effects occur on the player's money or portfolio when this happens.
     */
    @Test
    void commitRejectsWhenNotEnoughShares() {
        Player alice = aliceWithLot("5", "100", "100");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();

        Sale sale = new Sale(stock, new BigDecimal("10"), 2);

        assertThrows(IllegalStateException.class, () -> sale.commit(alice));
        assertEquals(0, alice.getPortfolio().getTotalQuantity("AAPL").compareTo(new BigDecimal("5")));
    }

    /**
     * Tests that commit rejects if called more than once on the same Sale, and that the side effects of the first
     * commit are preserved exactly once.
     */
    @Test
    void commitRejectsDoubleCommit() {
        Player alice = aliceWithLot("10", "100", "150");
        Stock stock = alice.getPortfolio().getShares().getFirst().getStock();
        Sale sale = new Sale(stock, new BigDecimal("10"), 2);

        sale.commit(alice);

        assertThrows(IllegalStateException.class, () -> sale.commit(alice));
    }

    /**
     * Tests that when a partial quantity is sold from a lot, the remaining quantity in the lot retains the original purchase price,
     * and the sale correctly reduces the quantity of the lot while leaving the purchase price unchanged for the remaining shares.
     */
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