package edu.ntnu.bidata.prog2.calculator;


import java.math.BigDecimal;

/**
 * The TransactionCalculator interface defines methods for calculating the gross amount,
 * commission, tax, and total for a financial transaction involving shares. Implementing
 * classes will provide specific calculations based on the type of transaction (e.g., purchase or sale).
 *
 * @author Binit Dhungana
 * @version 2026-02-11
 */
public interface TransactionCalculator {
    BigDecimal calculateGross();
    BigDecimal calculateCommission();
    BigDecimal calculateTax();
    BigDecimal calculateTotal();
}
