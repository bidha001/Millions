package edu.ntnu.bidata.prog2.transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public int countDistinctWeeks(){
        Set<Integer> weeks = new HashSet<>();

        for (Transaction transaction : transactions) {
            weeks.add(transaction.getWeek());
        }
        return weeks.size();
    }
}
