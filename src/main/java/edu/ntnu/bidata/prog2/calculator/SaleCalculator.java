package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

public class SaleCalculator implements TransactionCalculator {
    private final Share share;

    public SaleCalculator(Share share) {
        this.share = share;
    }

    @Override
    public BigDecimal calculateGross() { //gross = sales price * quantity
        return share.getStock()
                .getSalesPrice()
                .multiply(share.getQuantity());
    }

    @Override
    public BigDecimal calculateCommission() { //commission = gross * 1%
        return calculateGross().multiply(new BigDecimal("0.01"));
    }

    @Override
    public BigDecimal calculateTax() { //profit = gross - commission - (purchase price * quantity)
        BigDecimal purchaseCost=
                share .getPurchasePrice().multiply(share.getQuantity());

        BigDecimal profit =
                calculateGross().subtract(calculateCommission()).subtract(purchaseCost);

        return profit.multiply(new BigDecimal("0.30"));
    }

    @Override
    public BigDecimal calculateTotal() { //total = gross - commission - tax
        return calculateGross()
                .subtract(calculateCommission())
                .subtract(calculateTax());
    }
}
