package edu.ntnu.bidata.prog2.calculator;


import java.math.BigDecimal;

public interface TransactionCalculator {
    BigDecimal calculateGross();
    BigDecimal calculateCommission();
    BigDecimal calculateTax();
    BigDecimal calculateTotal();
}
