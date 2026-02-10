package edu.ntnu.bidata.prog2.transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionArchive {
    private final List<Transaction> transactions;


    public TransactionArchive(){
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

     public List<Transaction> getTransactions() {
        return transactions;
    }
}
