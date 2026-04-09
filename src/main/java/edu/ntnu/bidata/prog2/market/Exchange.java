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

/**
 * Represents a stock exchange where players can buy and sell shares.
 */
public class Exchange {
    private final String name;
    private final Map<String, Stock> stocks;
    private int week;
    private final Random random;

    /**
     * Constructs a new Exchange with the specified name and stocks.
     *
     * @param name   The name of the exchange.
     * @param stocks A map of stock symbols to Stock objects available on the exchange.
     */
    public Exchange(String name, Map<String, Stock> stocks) {
        this.name = name;
        this.stocks = stocks;
        this.week = 1;
        this.random = new Random();
    }

    /**
     * Retrieves the name of the exchange.
     *
     * @return The name of the exchange.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the current week number in the stock market game.
     *
     * @return The current week number.
     */
    public int getWeek() {
        return week;
    }

    /**
     * Retrieves a Stock object based on its symbol.
     *
     * @param symbol The stock symbol to look up.
     * @return The Stock object associated with the given symbol, or null if not found.
     */
    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

   /**
     * Finds stocks whose symbol or company name contains the given search string (case-insensitive).
     *
     * @param search The search string to look for in stock symbols and company names.
     * @return A list of Stock objects that match the search criteria.
     */
    public List<Stock> findStocks(String search) {

        List<Stock> result = new ArrayList<>();

        String lowerSearch = search.toLowerCase();

        for (Stock stock : stocks.values()) {

            if (stock.getSymbol().toLowerCase().contains(lowerSearch) ||
                    stock.getCompany().toLowerCase().contains(lowerSearch)) {

                result.add(stock);
            }
        }

        return result;
    }

    /**
     * Advances the game by one week, updating the sales price of each stock based on a random percentage change.
     * The price change is between -5% and +5%, and the new price is rounded to 2 decimal places.
     * The price will not go below 1.00.
     */
    public void advance(){
        week++;

        for (Stock stock : stocks.values()) {

            // update trend
            stock.updateTrend(random);

            // small random noise
            double noise = (random.nextDouble() - 0.5) * 0.02;

            // combine trend + noise
            double changePercent = stock.getTrend() + noise;

            BigDecimal currentPrice = stock.getSalesPrice();
            BigDecimal multiplier = BigDecimal.valueOf(1 + changePercent);

            BigDecimal newPrice = currentPrice.multiply(multiplier)
                    .setScale(2, RoundingMode.HALF_UP);

            if (newPrice.compareTo(BigDecimal.ONE) < 0) {
                newPrice = BigDecimal.ONE;
            }

            stock.addNewSalesPrice(newPrice);

            // System.out.println(stock.getSymbol() + " → " + newPrice);
        }
    }

    /**
     * Allows a player to buy shares of a stock. The purchase is recorded in the transaction archive.
     *
     * @param player The player who is buying the shares.
     * @param share  The share being purchased.
     */
    public void buy(Player player, Share share) {
        Purchase purchase = new Purchase(share, week);
        purchase.commit(player, player.getArchive());
    }

    /**
     * Allows a player to sell shares of a stock. The sale is recorded in the transaction archive.
     *
     * @param player The player who is selling the shares.
     * @param share  The share being sold.
     */
    public void sell(Player player, Share share) {
        Sale sale = new Sale(share, week);
        sale.commit(player, player.getArchive());
    }

    /**
     * Retrieves a list of the top gainers (stocks with the highest price increase) on the exchange.
     *
     * @param limit The maximum number of gainers to return.
     * @return A list of Stock objects representing the top gainers.
     */
    public List<Stock> getGainers(int limit) {

        List<Stock> stockList = new ArrayList<>(stocks.values());

        stockList.sort((s1, s2) ->
                s2.getLatestPriceChange().compareTo(s1.getLatestPriceChange()));

        return stockList.subList(0, Math.min(limit, stockList.size()));
    }

    /**
     * Retrieves a list of the top losers (stocks with the highest price decrease) on the exchange.
     *
     * @param limit The maximum number of losers to return.
     * @return A list of Stock objects representing the top losers.
     */
    public List<Stock> getLosers(int limit) {

        List<Stock> stockList = new ArrayList<>(stocks.values());

        stockList.sort((s1, s2) ->
                s1.getLatestPriceChange().compareTo(s2.getLatestPriceChange()));

        return stockList.subList(0, Math.min(limit, stockList.size()));
    }
}
