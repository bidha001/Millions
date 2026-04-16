package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SaleCalculatorTest {

    /**
     * Tests that calculateGross returns sale price multiplied by quantity.
     */
    @Test
    public void calculateGrossReturnsSalePriceTimesQuantity() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"),
                new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(share);

        assertEquals(0, calc.calculateGross().compareTo(new BigDecimal("1500")));
    }

    /**
     * Tests that calculateCommission returns 1% of the gross amount.
     */
    @Test
    public void calculateCommissionIsOnePercentOfGross() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"),
                new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(share);

        // 1% of 1500 = 15.00
        assertEquals(0, calc.calculateCommission().compareTo(new BigDecimal("15.00")));
    }

    /**
     * Tests that calculateTax returns 30% of the profit when sale is profitable.
     */
    @Test
    public void calculateTaxIs30PercentOfProfit() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"),
                new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(share);

        // gross 1500 - commission 15 - cost 1000 = profit 485
        // tax = 30% of 485 = 145.50
        assertEquals(0, calc.calculateTax().compareTo(new BigDecimal("145.50")));
    }

    /**
     * Tests that calculateTax returns zero when the sale is at a loss.
     */
    @Test
    public void calculateTaxIsZeroWhenSellingAtLoss() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"),
                new BigDecimal("100"), new BigDecimal("80"));
        SaleCalculator calc = new SaleCalculator(share);

        assertEquals(0, calc.calculateTax().compareTo(BigDecimal.ZERO));
    }

    /**
     * Tests that calculateTotal returns gross minus commission minus tax.
     */
    @Test
    public void calculateTotalSubtractsCommissionAndTax() {
        Stock stock = new Stock("Apple", "AAPL", new BigDecimal("100"));
        Share share = new Share(stock, new BigDecimal("10"),
                new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(share);

        // 1500 - 15 - 145.50 = 1339.50
        assertEquals(0, calc.calculateTotal().compareTo(new BigDecimal("1339.50")));
    }

    /**
     * Tests that the calculator throws when the share is null.
     */
    @Test
    public void calculatorWithNullShareThrowsOnUse() {
        SaleCalculator calc = new SaleCalculator(null);

        assertThrows(NullPointerException.class, calc::calculateGross);
    }
}