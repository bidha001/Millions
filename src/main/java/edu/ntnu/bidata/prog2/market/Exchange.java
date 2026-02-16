package edu.ntnu.bidata.prog2.market;

import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.Purchase;
import edu.ntnu.bidata.prog2.transaction.Sale;
import edu.ntnu.bidata.prog2.transaction.TransactionArchive;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.math.RoundingMode;

public class Exchange {
    private final String name;
    private final Map<String, Stock> stocks;
    private int week;
    private final TransactionArchive archive;
    private final Random random;

    public Exchange(String name, Map<String, Stock> stocks) {
        this.name = name;
        this.stocks = stocks;
        this.week = 1;
        this.archive = new TransactionArchive();
        this.random = new Random();
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

        for (Stock stock : stocks.values()) {
            //price change between -5% and +5%)
            double changePercent = -0.05 + (0.10) * random.nextDouble();

            BigDecimal currentPrice = stock.getSalesPrice();
            BigDecimal multiplier = BigDecimal.valueOf(1 + changePercent);

            // Round to 2 decimal places
            BigDecimal newPrice = currentPrice.multiply(multiplier)
                    .setScale(2, RoundingMode.HALF_UP);

            // Ensure price doesn't go negative
            if (newPrice.compareTo(BigDecimal.ONE) < 0) {
                newPrice = BigDecimal.ONE; // Ensure price doesn't go negative
            }

            stock.addNewSalesPrice(newPrice);
        }
    }

    public void buy(Player player, Share share) {
        Purchase purchase = new Purchase(share, week);
        purchase.commit(player, archive);
    }

    public void sell(Player player, Share share) {
        Sale sale = new Sale(share, week);
        sale.commit(player, archive);
    }

    public TransactionArchive getArchive() {
        return archive;
    }
}
