package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.SaleCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

/**
 * Represents a sale transaction where a player sells shares.
 */
public class Sale extends Transaction {

    /**
     * Constructs a new Sale transaction for the given share and week.
     *
     * @param share The share being sold.
     * @param week  The week number when the sale is made.
     */
    public Sale(Share share, int week) {
        super(share, week, new SaleCalculator(share));
    }

    /**
     * Commits the sale transaction for the given player.
     * Validates that the player owns the shares and has enough quantity before performing the sale.
     *
     * @param player The player committing the transaction.
     * @throws IllegalStateException if the transaction is already committed,
     * if the player doesn't own the share, or if the player doesn't have enough shares to sell.
     */
    @Override
    public void commit(Player player) {
        if (committed) {
            throw new IllegalStateException("Transaction already committed!");
        }

        // VALIDATE FIRST
        Share existingShare = player.getPortfolio().getShareByStock(share.getStock());
        if (existingShare == null) {
            throw new IllegalStateException("You don't own this share!");
        }
        BigDecimal remaining = existingShare.getQuantity().subtract(share.getQuantity());
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("You don't own enough shares!");
        }

        // THEN mutate state
        BigDecimal total = calculator.calculateTotal();
        player.setMoney(player.getMoney().add(total));
        player.getPortfolio().removeShare(existingShare);
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            Share updatedShare = new Share(existingShare.getStock(), remaining,
                    existingShare.getPurchasePrice(), null);
            player.getPortfolio().addShare(updatedShare);
        }
        committed = true;
        player.getArchive().addTransaction(this);
    }
}