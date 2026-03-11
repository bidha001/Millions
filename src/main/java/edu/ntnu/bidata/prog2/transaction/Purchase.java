package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.PurchaseCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

/**
 * Represents a purchase transaction where a player buys shares.
 */
public class Purchase extends Transaction {
    public Purchase(Share share, int week) {
        super(share, week, new PurchaseCalculator(share));
    }

    /**
     * Commits the purchase transaction by deducting the total cost from the player's money and adding the share to the player's portfolio.
     * Also adds the transaction to the transaction archive.
     *
     * @param player  The player making the purchase.
     * @param archive The transaction archive to record the transaction.
     * @throws IllegalStateException if the player does not have enough money to complete the purchase.
     */
    @Override
    public void commit(Player player, TransactionArchive archive) {
        if(committed){
            return;
        }

        BigDecimal totalCost = calculator.calculateTotal();

        //Prevent buying shares if player doesn't have enough money.
        if(player.getMoney().compareTo(totalCost) < 0){
            throw new IllegalStateException("You don't have enough money to buy this share!");
        }

        player.setMoney(
                player.getMoney().subtract(calculator.calculateTotal())
        );
        player.getPortfolio().addShare(share);
        committed = true;
        archive.addTransaction(this);
    }
}
