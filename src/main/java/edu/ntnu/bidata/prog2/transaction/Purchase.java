package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.PurchaseCalculator;
import edu.ntnu.bidata.prog2.model.Share;

public class Purchase extends Transaction {
    public Purchase(Share share, int week) {
        super(share, week, new PurchaseCalculator(share));
    }

    @Override
    public void commit() {
    // to do: need to implement the commit logic
    }
}
