package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.SaleCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

/***
 * Represents a sale of a share.
 */
public class Sale extends Transaction {
    public Sale(Share share, int week) {
        super(share, week, new SaleCalculator(share));
    }


    /**
     * Commits the sale transaction by updating the player's money and portfolio.
     *
     * @param player  the player executing the transaction
     * @param archive the transaction archive to record the transaction
     * @throws IllegalStateException if the transaction has already been committed, if the player doesn't own the share, or if the player doesn't own enough shares
     */
    @Override
    public void commit(Player player, TransactionArchive archive) {

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
                    existingShare.getPurchasePrice()
            );
            player.getPortfolio().addShare(updatedShare);
        }

        committed = true;
        archive.addTransaction(this);
    }
}
