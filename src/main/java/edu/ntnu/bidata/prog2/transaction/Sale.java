package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.RecordedTotalsCalculator;
import edu.ntnu.bidata.prog2.calculator.SaleCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Portfolio;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Represents a sale transaction where a player sells shares.
 * The sale is committed by walking the player's matching lots in FIFO order,
 * calculating the gross, commission, tax, and net total for each lot, and
 * updating the player's money and portfolio accordingly.
 */
public class Sale extends Transaction {

    private final Stock stock;
    private final BigDecimal quantity;

    /**
     * Constructs a new Sale for the given stock, quantity, and week. The
     * predicted calculator is built using the total quantity and the stock's
     * current sales price, but the actual totals may differ after commit due
     * to FIFO lot accounting.
     *
     * @param stock    the stock being sold
     * @param quantity the total quantity being sold across all touched lots
     * @param week     the week of the sale
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Sale(Stock stock, BigDecimal quantity, int week) {
        super(buildAggregate(stock, quantity), week,
                new SaleCalculator(buildAggregate(stock, quantity)));
        this.stock = stock;
        this.quantity = quantity;
    }

    /** Builds an aggregate Share for the given stock and quantity, using the stock's current sales price.
     *
     * @param stock    the stock being sold
     * @param quantity the total quantity being sold across all touched lots
     * @return a Share representing the aggregate sale
     * @throws IllegalArgumentException if any argument is invalid
     */
    private static Share buildAggregate(Stock stock, BigDecimal quantity) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        return new Share(stock, quantity, stock.getSalesPrice());
    }

    /**
     * Commits the sale by walking the player's matching lots in FIFO order,
     * calculating the gross, commission, tax, and net total for each lot, and
     * updating the player's money and portfolio accordingly.
     *
     * @param player the player committing the sale
     * @throws IllegalStateException    if this transaction has already been committed,
     *                                  or the player doesn't have enough shares
     * @throws IllegalArgumentException if {@code player} is null
     */
    @Override
    public void commit(Player player) {
        if (committed) {
            throw new IllegalStateException("Transaction already committed");
        }
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        Portfolio portfolio = player.getPortfolio();
        List<Share> lots = portfolio.getShares(stock.getSymbol());

        BigDecimal owned = BigDecimal.ZERO;
        for (Share s : lots) {
            owned = owned.add(s.getQuantity());
        }
        if (owned.compareTo(quantity) < 0) {
            throw new IllegalStateException(
                    "Not enough shares: requested " + quantity + ", owned " + owned);
        }

        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal totalCommission = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;

        BigDecimal remaining = quantity;
        for (Share lot : lots) {
            if (remaining.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

            BigDecimal take = lot.getQuantity().min(remaining);

            Share soldChunk = new Share(stock, take, lot.getPurchasePrice());
            SaleCalculator lotCalc = new SaleCalculator(soldChunk);

            totalGross = totalGross.add(lotCalc.calculateGross());
            totalCommission = totalCommission.add(lotCalc.calculateCommission());
            totalTax = totalTax.add(lotCalc.calculateTax());
            totalNet = totalNet.add(lotCalc.calculateTotal());

            portfolio.removeShare(lot);
            BigDecimal lotRemainder = lot.getQuantity().subtract(take);
            if (lotRemainder.compareTo(BigDecimal.ZERO) > 0) {
                portfolio.addShare(new Share(stock, lotRemainder, lot.getPurchasePrice()));
            }
            remaining = remaining.subtract(take);
        }

        // Round each total once at the end for display consistency.
        BigDecimal gross = totalGross.setScale(2, RoundingMode.HALF_UP);
        BigDecimal commission = totalCommission.setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = totalTax.setScale(2, RoundingMode.HALF_UP);
        BigDecimal net = totalNet.setScale(2, RoundingMode.HALF_UP);

        player.addMoney(net);
        setCalculator(new RecordedTotalsCalculator(gross, commission, tax, net));

        committed = true;
        player.getArchive().add(this);
    }

    /**
     * The stock being sold.
     *
     * @return the stock being sold
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * The total quantity being sold across all touched lots.
     *
     * @return the total quantity being sold
     */
    public BigDecimal getQuantity() {
        return quantity;
    }
}