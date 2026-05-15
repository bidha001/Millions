package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.PurchaseCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

/**
 * Represents a purchase transaction where a player buys shares. The purchase is
 * committed by deducting the total cost from the player's money, adding the
 * share to the player's portfolio, and recording the transaction in the
 * player's archive.
 */
public class Purchase extends Transaction {

    /**
     * Constructs a new Purchase for the given share and week.
     *
     * @param share the share being purchased
     * @param week  the week of the purchase
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Purchase(Share share, int week) {
        super(share, week, new PurchaseCalculator(share));
    }

    /**
     * Commits the purchase by deducting the total cost from the player's money,
     * adding the share to the player's portfolio, and recording the transaction
     * in the player's archive.
     *
     * @param player the player committing the purchase
     * @throws IllegalStateException    if this transaction has already been committed,
     *                                  or the player doesn't have enough money
     * @throws IllegalArgumentException if {@code player} is null
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

        player.withdrawMoney(totalCost);
        player.getPortfolio().addShare(share);

        committed = true;
        player.getArchive().add(this);
    }
}