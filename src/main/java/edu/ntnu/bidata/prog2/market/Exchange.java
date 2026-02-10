package edu.ntnu.bidata.prog2.market;

import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.TransactionArchive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Exchange {
    private final String name;
    private final Map<String, Stock> stocks;
    private int week;

    public Exchange(String name, Map<String, Stock> stocks) {
        this.name = name;
        this.stocks = stocks;
        this.week = 1;
    }

    public String getName() {
        return name;
    }

    public int getWeek() {
        return week;
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public List<Stock> findStocks(String search){
        List<Stock> result = new ArrayList<>();

        for (Stock stock : stocks.values()) {
            if (stock.getSymbol().contains(search) ||
                    stock.getCompany().contains(search)){
                result.add(stock);
            }
        }
        return result;
    }

    public void advance(){
        week++;
    }

    public void buy(Player player, Share share) {
        //later
    }

    public void sell(Player player, Share share) {
        //later
    }
}
