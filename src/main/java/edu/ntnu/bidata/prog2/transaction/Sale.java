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
     * Commits the sale transaction by adding the total value to the player's money,
     * removing the share from the player's portfolio, and recording the transaction
     * in the player's archive.
     *
     * @param player The player executing the sale.
     * @throws IllegalStateException if the transaction has already been committed,
     *                               if the player does not own the share,
     *                               or if the player does not own enough shares.
     */
    @Override
    public void commit(Player player) {

        if (committed) {
            throw new IllegalStateException("Transaction already committed!");
        }

        BigDecimal total = calculator.calculateTotal();

        player.setMoney(player.getMoney().add(total));

        Share existingShare = player.getPortfolio().getShareByStock(share.getStock());

        if (existingShare == null) {
            throw new IllegalStateException("You don't own this share!");
        }

        BigDecimal remaining = existingShare.getQuantity().subtract(share.getQuantity());

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("You don't own enough shares!");
        }

        // remove the original share
        player.getPortfolio().removeShare(existingShare);

        // add back remaining shares if any left
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            Share updatedShare = new Share(
                    existingShare.getStock(),
                    remaining,
                    existingShare.getPurchasePrice(),
                    null
            );
            player.getPortfolio().addShare(updatedShare);
        }

        committed = true;
        player.getArchive().addTransaction(this);
    }
}