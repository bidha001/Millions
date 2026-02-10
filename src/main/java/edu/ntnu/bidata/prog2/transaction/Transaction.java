package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.TransactionCalculator;
import edu.ntnu.bidata.prog2.model.Share;

public abstract class Transaction {
    public final Share share;
    public int week;
    public final TransactionCalculator calculator;
    public boolean committed;

    public Transaction(Share share, int week, TransactionCalculator calculator) {
        this.share = share;
        this.week = week;
        this.calculator = calculator;
        this.committed = false;
    }

    public abstract void commit();
}
