package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * SaleCalculator calculates the financial details of a stock sale transaction.
 * It implements the TransactionCalculator interface to provide methods for
 * calculating gross amount, commission, tax, and total amount for a sale.
 */
public class SaleCalculator implements TransactionCalculator {
    private final Share share;

    /**
     * Constructs a SaleCalculator for the given share.
     *
     * @param share the share being sold
     */
    public SaleCalculator(Share share) {
        this.share = share;
    }

    /**
     * Calculates the gross amount for the sale transaction.
     *
     * @return the gross amount (sales price * quantity)
     */
    @Override
    public BigDecimal calculateGross() { //gross = sales price * quantity
        return share.getStock()
                .getSalesPrice()
                .multiply(share.getQuantity());
    }

    /**
     * Calculates the commission for the sale transaction.
     *
     * @return the commission amount (gross * 1%)
     */
    @Override
    public BigDecimal calculateCommission() { //commission = gross * 1%
        return calculateGross()
                .multiply(new BigDecimal("0.01"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the tax for the sale transaction.
     *
     * @return the tax amount (30% of profit, if profit > 0)
     */
    @Override
    public BigDecimal calculateTax() {

        BigDecimal gross = calculateGross();
        BigDecimal commission = calculateCommission();

        BigDecimal purchaseCost =
                share.getPurchasePrice().multiply(share.getQuantity());

        BigDecimal profit = gross
                .subtract(commission)
                .subtract(purchaseCost);

        if (profit.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return profit.multiply(new BigDecimal("0.30"))
                .setScale(2, RoundingMode.HALF_UP);
    }

     /**
     * Calculates the total amount for the sale transaction.
     *
     * @return the total amount (gross - commission - tax)
     */
    @Override
    public BigDecimal calculateTotal() {

        BigDecimal gross = calculateGross();
        BigDecimal commission = calculateCommission();
        BigDecimal tax = calculateTax();

        return gross
                .subtract(commission)
                .subtract(tax)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
