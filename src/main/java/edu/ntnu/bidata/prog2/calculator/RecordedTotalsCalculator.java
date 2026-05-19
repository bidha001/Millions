package edu.ntnu.bidata.prog2.calculator;

import java.math.BigDecimal;

/**
 * A transaction calculator that returns recorded totals for gross, commission, tax, and net total.
 * This implementation assumes that the totals are pre-calculated and provided at construction.
 */
public final class RecordedTotalsCalculator implements TransactionCalculator {

    private final BigDecimal gross;
    private final BigDecimal commission;
    private final BigDecimal tax;
    private final BigDecimal total;

    /**
     * Constructs a recorded-totals calculator.
     *
     * @param gross      the recorded gross amount
     * @param commission the recorded commission
     * @param tax        the recorded tax
     * @param total      the recorded net total
     * @throws IllegalArgumentException if any argument is null
     */
    public RecordedTotalsCalculator(BigDecimal gross, BigDecimal commission, BigDecimal tax, BigDecimal total) {
        if (gross == null || commission == null || tax == null || total == null) {
            throw new IllegalArgumentException("Totals cannot be null");
        }
        this.gross = gross;
        this.commission = commission;
        this.tax = tax;
        this.total = total;
    }

    /**
     * Returns the recorded gross amount.
     *
     * @return the gross amount
     */
    @Override
    public BigDecimal calculateGross() {
        return gross;
    }

    /**
     * Returns the recorded commission.
     *
     * @return the commission amount
     */
    @Override
    public BigDecimal calculateCommission() {
        return commission;
    }

    /**
     * Returns the recorded tax.
     *
     * @return the tax amount
     */
    @Override
    public BigDecimal calculateTax() {
        return tax;
    }

    /**
     * Returns the recorded net total.
     *
     * @return the net total
     */
    @Override
    public BigDecimal calculateTotal() {
        return total;
    }
}