package edu.ntnu.bidata.prog2.transaction;

import edu.ntnu.bidata.prog2.calculator.SaleCalculator;
import edu.ntnu.bidata.prog2.model.Share;

public class Sale extends Transaction {
    public Sale(Share share, int week) {
        super(share, week, new SaleCalculator(share));
    }

    @Override
    public void commit() {
        // to do: need to implement the commit logic
    }
}
