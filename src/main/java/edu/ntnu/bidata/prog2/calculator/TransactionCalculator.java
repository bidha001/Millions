package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.transaction.Transaction;

import java.math.BigDecimal;

public interface TransactionCalculator {
    BigDecimal calculateGross();
    BigDecimal calculateCommission();
    BigDecimal calculateTax();
    BigDecimal calculateTotal();


}
