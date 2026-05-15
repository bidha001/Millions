package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaleCalculatorTest {

    /**
     * Builds a Share where the player bought at {@code purchasePrice} and the
     * current market price is {@code currentPrice}.
     */
    private static Share share(BigDecimal quantity, BigDecimal purchasePrice, BigDecimal currentPrice) {
        Stock stock = new Stock("AAPL", "Apple", purchasePrice);
        stock.addNewSalesPrice(currentPrice);
        return new Share(stock, quantity, purchasePrice);
    }

    /** Tests that calculateGross uses the stock's current price, not the purchase price. */
    @Test
    void calculateGrossUsesCurrentPriceTimesQuantity() {
        Share s = share(new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(s);

        // 10 * 150 (current) = 1500 — would be 1000 if it used purchase price
        assertEquals(0, calc.calculateGross().compareTo(new BigDecimal("1500")));
    }

    /** Tests that calculateCommission returns 1% of the gross amount. */
    @Test
    void calculateCommissionIsOnePercentOfGross() {
        Share s = share(new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(s);

        // 1% of 1500 = 15.00
        assertEquals(0, calc.calculateCommission().compareTo(new BigDecimal("15.00")));
    }

    /** Tests that calculateTax returns 30% of the profit when the sale is profitable. */
    @Test
    void calculateTaxIs30PercentOfProfit() {
        Share s = share(new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(s);

        // gross 1500 - commission 15 - purchaseCost 1000 = profit 485
        // tax = 30% of 485 = 145.50
        assertEquals(0, calc.calculateTax().compareTo(new BigDecimal("145.50")));
    }

    /** Tests that calculateTax is zero when the sale is at a loss. */
    @Test
    void calculateTaxIsZeroWhenSellingAtLoss() {
        Share s = share(new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("80"));
        SaleCalculator calc = new SaleCalculator(s);

        assertEquals(0, calc.calculateTax().compareTo(BigDecimal.ZERO));
    }

    /** Tests that calculateTotal returns gross minus commission minus tax. */
    @Test
    void calculateTotalSubtractsCommissionAndTax() {
        Share s = share(new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("150"));
        SaleCalculator calc = new SaleCalculator(s);

        // 1500 - 15 - 145.50 = 1339.50
        assertEquals(0, calc.calculateTotal().compareTo(new BigDecimal("1339.50")));
    }

    /** Tests that the constructor rejects null. */
    @Test
    void constructorRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SaleCalculator(null));
    }
}