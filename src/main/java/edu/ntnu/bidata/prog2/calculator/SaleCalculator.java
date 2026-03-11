package edu.ntnu.bidata.prog2.calculator;

import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

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
        return calculateGross().multiply(new BigDecimal("0.01"));
    }

    /**
     * Calculates the tax for the sale transaction.
     *
     * @return the tax amount (profit * 30%)
     */
    @Override
    public BigDecimal calculateTax() { //profit = gross - commission - (purchase price * quantity)
        BigDecimal purchaseCost=
                share .getPurchasePrice().multiply(share.getQuantity());

        BigDecimal profit =
                calculateGross().subtract(calculateCommission()).subtract(purchaseCost);

        return profit.multiply(new BigDecimal("0.30"));
    }

    /**
     * Calculates the total amount for the sale transaction.
     *
     * @return the total amount (gross - commission - tax)
     */
    @Override
    public BigDecimal calculateTotal() { //total = gross - commission - tax
        return calculateGross()
                .subtract(calculateCommission())
                .subtract(calculateTax());
    }
}
