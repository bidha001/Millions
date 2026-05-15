package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The SaleCalculator class implements the TransactionCalculator interface to calculate
 * the gross amount, commission, tax, and total proceeds for a sale transaction of shares.
 *
 * @author Binit Dhungana
 * @version 2026-02-11
 */
public class SaleCalculator implements TransactionCalculator {

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.01");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.30");
    private static final int SCALE = 2;

    private final Share share;

    /**
     * Constructs a SaleCalculator for the given share.
     *
     * @param share the share being sold (must not be null)
     * @throws IllegalArgumentException if {@code share} is null
     */
    public SaleCalculator(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        this.share = share;
    }

    /**
     * Calculates the gross amount for the sale transaction, using the
     * sales price of the stock multiplied by the quantity sold.
     *
     * @return the gross amount
     */
    @Override
    public BigDecimal calculateGross() {
        return share.getStock().getSalesPrice()
                .multiply(share.getQuantity());
    }

    /**
     * Calculates the commission for the sale transaction, fixed at 1% of the gross amount.
     *
     * @return the commission amount, rounded to 2 decimal places
     */
    @Override
    public BigDecimal calculateCommission() {
        return calculateGross()
                .multiply(COMMISSION_RATE)
                .setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the tax for the sale transaction, which is 30% of the profit (gross minus commission and purchase cost).
     * If there is no profit, the tax is zero.
     *
     * @return the tax amount, rounded to 2 decimal places
     */
    @Override
    public BigDecimal calculateTax() {
        BigDecimal gross = calculateGross();
        BigDecimal commission = calculateCommission();
        BigDecimal purchaseCost = share.getPurchasePrice().multiply(share.getQuantity());

        BigDecimal profit = gross.subtract(commission).subtract(purchaseCost);

        if (profit.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return profit.multiply(TAX_RATE).setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the total proceeds for the sale transaction, which is the gross amount minus commission and tax.
     *
     * @return the total proceeds, rounded to 2 decimal places
     */
    @Override
    public BigDecimal calculateTotal() {
        return calculateGross()
                .subtract(calculateCommission())
                .subtract(calculateTax())
                .setScale(SCALE, RoundingMode.HALF_UP);
    }
}