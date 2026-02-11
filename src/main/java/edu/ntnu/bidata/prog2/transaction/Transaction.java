package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.TransactionCalculator;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;

public abstract class Transaction {
    protected final Share share;
    protected int week;
    protected final TransactionCalculator calculator;
    protected boolean committed;

    public Transaction(Share share, int week, TransactionCalculator calculator) {
        this.share = share;
        this.week = week;
        this.calculator = calculator;
        this.committed = false;
    }

    //get share():share
    // more, check mappe dokumentation
    //side 7

    public abstract void commit(Player player, TransactionArchive archive);
}
