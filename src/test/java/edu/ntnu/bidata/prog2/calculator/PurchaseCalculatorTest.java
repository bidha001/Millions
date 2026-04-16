package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PurchaseCalculatorTest {

    /**
     * Tests that calculateGross returns purchase price multiplied by quantity.
     */
    @Test
    public void calculateGrossReturnsPriceTimesQuantity() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);
        PurchaseCalculator calc = new PurchaseCalculator(share);

        assertEquals(0, calc.calculateGross().compareTo(new BigDecimal("1000")));
    }

    /**
     * Tests that calculateCommission returns 0.5% of the gross amount.
     */
    @Test
    public void calculateCommissionIsHalfPercentOfGross() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);
        PurchaseCalculator calc = new PurchaseCalculator(share);

        // 0.5% of 1000 = 5.00
        assertEquals(0, calc.calculateCommission().compareTo(new BigDecimal("5.00")));
    }

    /**
     * Tests that calculateTax is always zero for purchase transactions.
     */
    @Test
    public void calculateTaxIsZeroForPurchase() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);
        PurchaseCalculator calc = new PurchaseCalculator(share);

        assertEquals(0, calc.calculateTax().compareTo(BigDecimal.ZERO));
    }

    /**
     * Tests that calculateTotal returns gross plus commission plus tax.
     */
    @Test
    public void calculateTotalSumsGrossCommissionAndTax() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"), new BigDecimal("100"), null);
        PurchaseCalculator calc = new PurchaseCalculator(share);

        // 1000 + 5 + 0 = 1005
        assertEquals(0, calc.calculateTotal().compareTo(new BigDecimal("1005.00")));
    }

    /**
     * Tests that calculations round correctly with decimal prices.
     */
    @Test
    public void calculationsWorkWithDecimalPrices() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("99.99"));
        Share share = new Share(stock, new BigDecimal("3"), new BigDecimal("99.99"), null);
        PurchaseCalculator calc = new PurchaseCalculator(share);

        // gross: 3 * 99.99 = 299.97
        assertEquals(0, calc.calculateGross().compareTo(new BigDecimal("299.97")));
        // commission: 0.5% of 299.97 = 1.49985 → 1.50
        assertEquals(0, calc.calculateCommission().compareTo(new BigDecimal("1.50")));
    }

    /**
     * Tests that the calculator throws when the share is null.
     */
    @Test
    public void calculatorWithNullShareThrowsOnUse() {
        PurchaseCalculator calc = new PurchaseCalculator(null);

        assertThrows(NullPointerException.class, calc::calculateGross);
    }
}