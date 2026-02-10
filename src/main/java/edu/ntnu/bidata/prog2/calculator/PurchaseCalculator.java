package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

public class PurchaseCalculator implements TransactionCalculator {
    private final Share share;

    public PurchaseCalculator(Share share) {
        this.share = share;
    }

    @Override
    public BigDecimal calculateGross() {
        return null;
    }

    @Override
    public BigDecimal calculateCommission() {
        return null;
    }

    @Override
    public BigDecimal calculateTax() {
        return null;
    }

    @Override
    public BigDecimal calculateTotal() {
        return null;
    }
}
