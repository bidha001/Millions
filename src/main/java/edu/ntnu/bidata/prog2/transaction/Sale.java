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
     * Commits the sale transaction for the given player and archives it.
     * If the transaction has already been committed, it does nothing.
     * If the player does not own the share being sold, it throws an IllegalStateException.
     *
     * @param player  The player committing the sale transaction.
     * @param archive The transaction archive to which this transaction will be added after committing.
     */
    @Override
    public void commit(Player player, TransactionArchive archive) {

        if (committed) {
            throw new IllegalStateException("Transaction already committed!");
        }

        if (!player.getPortfolio().containsShare(share)) {
            throw new IllegalStateException("You don't own this share!");
        }

        BigDecimal total = calculator.calculateTotal();

        player.setMoney(player.getMoney().add(total));
        player.getPortfolio().removeShare(share);

        committed = true;
        archive.addTransaction(this);
    }
}
