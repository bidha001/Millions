package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.PurchaseCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

import java.math.BigDecimal;

public class Purchase extends Transaction {
    public Purchase(Share share, int week) {
        super(share, week, new PurchaseCalculator(share));
    }

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
