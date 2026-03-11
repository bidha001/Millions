package edu.ntnu.bidata.prog2.calculator;


import java.math.BigDecimal;

/**
 * Interface for calculating various aspects of a financial transaction, such as gross amount, commission, tax, and total.
 */
public interface TransactionCalculator {
    BigDecimal calculateGross();
    BigDecimal calculateCommission();
    BigDecimal calculateTax();
    BigDecimal calculateTotal();
}
