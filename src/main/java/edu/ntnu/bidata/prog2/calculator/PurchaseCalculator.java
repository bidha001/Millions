package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

/**
 * Calculator for purchase transactions.
 */
public class PurchaseCalculator implements TransactionCalculator {
    private final Share share;

    /**
     * Constructs a PurchaseCalculator for the given share.
     *
     * @param share the share being purchased
     */
    public PurchaseCalculator(Share share) {
        this.share = share;
    }

    /**
     * Calculates the gross amount for the purchase transaction.
     *
     * @return the gross amount (purchase price * quantity)
     */
    @Override
    public BigDecimal calculateGross() {
        return share.getPurchasePrice().multiply(share.getQuantity());
    }

    /**
     * Calculates the commission for the purchase transaction.
     *
     * @return the commission amount (gross * 0.5%)
     */
    @Override
    public BigDecimal calculateCommission() {
        return calculateGross().multiply(new BigDecimal("0.005"));
    }

    /**
     * Calculates the tax for the purchase transaction.
     *
     * @return the tax amount (0 for purchases)
     */
    @Override
    public BigDecimal calculateTax() {
        return BigDecimal.ZERO;
    }

    /**
     * Calculates the total amount for the purchase transaction.
     *
     * @return the total amount (gross + commission + tax)
     */
    @Override
    public BigDecimal calculateTotal() {
        return calculateGross()
                .add(calculateCommission())
                .add(calculateTax());
    }
}
