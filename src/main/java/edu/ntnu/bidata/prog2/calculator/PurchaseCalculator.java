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
        return share.getPurchasePrice().multiply(share.getQuantity());
    }

    @Override
    public BigDecimal calculateCommission() {
        return calculateGross().multiply(new BigDecimal("0.005"));
    }

    @Override
    public BigDecimal calculateTax() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateTotal() {
        return calculateGross()
                .add(calculateCommission())
                .add(calculateTax());
    }
}
