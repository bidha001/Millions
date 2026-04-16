package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.PurchaseCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

/**
 * Represents a purchase transaction where a player buys shares.
 */
public class Purchase extends Transaction {

    /**
     * Constructs a new Purchase transaction for the given share and week.
     *
     * @param share The share being purchased.
     * @param week  The week number when the purchase is made.
     */
    public Purchase(Share share, int week) {
        super(share, week, new PurchaseCalculator(share));
    }

    /**
     * Commits the purchase transaction by deducting the total cost from the player's money,
     * adding the share to the player's portfolio, and recording the transaction in the
     * player's archive.
     *
     * @param player The player making the purchase.
     * @throws IllegalStateException if the transaction has already been committed,
     *                               or if the player does not have enough money.
     */
    @Override
    public void commit(Player player) {

        if (committed) {
            throw new IllegalStateException("Transaction already committed!");
        }

        BigDecimal totalCost = calculator.calculateTotal();

        if (player.getMoney().compareTo(totalCost) < 0) {
            throw new IllegalStateException("You don't have enough money!");
        }

        player.setMoney(player.getMoney().subtract(totalCost));
        player.getPortfolio().addShare(share);

        committed = true;
        player.getArchive().addTransaction(this);
    }
}