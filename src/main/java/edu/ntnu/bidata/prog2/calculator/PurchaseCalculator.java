package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The PurchaseCalculator class implements the TransactionCalculator interface to calculate
 * the gross amount, commission, tax, and total cost for a purchase transaction of shares.
 *
 * @author Binit Dhungana
 * @version 2026-02-11
 */
public class PurchaseCalculator implements TransactionCalculator {
    private final Share share;

    /**
     * Constructs a PurchaseCalculator for the given share.
     *
     * @param share the share being purchased (must not be null)
     * @throws IllegalArgumentException if {@code share} is null
     */
    public PurchaseCalculator(Share share) {
        if (share == null) {
            throw new IllegalArgumentException("Share cannot be null");
        }
        this.share = share;
    }

    /**
     * Calculates the gross amount for the purchase transaction, using the
     * purchase price of the stock multiplied by the quantity purchased.
     *
     * @return the gross amount
     */
    @Override
    public BigDecimal calculateGross() {
        return share.getPurchasePrice().multiply(share.getQuantity());
    }

    /**
     * Calculates the commission for the purchase transaction, fixed at 0.5% of the gross amount.
     *
     * @return the commission amount, rounded to 2 decimal places
     */
    @Override
    public BigDecimal calculateCommission() {
        return calculateGross()
                .multiply(new BigDecimal("0.005"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the tax for the purchase transaction, which is zero for purchases.
     *
     * @return the tax amount (always zero)
     */
    @Override
    public BigDecimal calculateTax() {
        return BigDecimal.ZERO;
    }

    /**
     * Calculates the total cost for the purchase transaction, which is the sum of the gross amount and commission.
     *
     * @return the total cost, rounded to 2 decimal places
     */
    @Override
    public BigDecimal calculateTotal() {
        return calculateGross()
                .add(calculateCommission())
                .add(calculateTax())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
