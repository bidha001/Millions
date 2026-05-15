package edu.ntnu.bidata.prog2.market;

import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.observer.GameEvent;
import edu.ntnu.bidata.prog2.observer.Observable;
import edu.ntnu.bidata.prog2.transaction.Purchase;
import edu.ntnu.bidata.prog2.transaction.Sale;
import edu.ntnu.bidata.prog2.transaction.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The Exchange class represents a stock exchange where players can buy and sell stocks.
 * It maintains a list of stocks, tracks the current trading week, and notifies observers
 * of significant events like transactions and week advancements.
 */
public class Exchange extends Observable {

    private final String name;
    private final Map<String, Stock> stockMap;
    private int week;
    private final Random random;

    /**
     * Constructs a new Exchange with the given name and stocks, using a default
     * {@link Random} for price updates.
     *
     * @param name   the exchange's name
     * @param stocks the stocks listed on the exchange
     * @throws IllegalArgumentException if any precondition is violated
     */
    public Exchange(String name, List<Stock> stocks) {
        this(name, stocks, new Random());
    }

    /**
     * Constructs a new Exchange with the given name, stocks, and Random instance.
     *
     * @param name   the exchange's name
     * @param stocks the stocks listed on the exchange
     * @param random the Random instance to use for price updates
     * @throws IllegalArgumentException if any precondition is violated
     */
    public Exchange(String name, List<Stock> stocks, Random random) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (stocks == null) {
            throw new IllegalArgumentException("Stocks list cannot be null");
        }
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }
        Map<String, Stock> indexed = new LinkedHashMap<>();
        for (Stock stock : stocks) {
            if (stock == null) {
                throw new IllegalArgumentException("Stocks list cannot contain null");
            }
            if (indexed.containsKey(stock.getSymbol())) {
                throw new IllegalArgumentException("Duplicate symbol: " + stock.getSymbol());
            }
            indexed.put(stock.getSymbol(), stock);
        }
        this.name = name;
        this.stockMap = indexed;
        this.week = 1;
        this.random = random;
    }

    /**
     * Returns the exchange's name.
     *
     * @return the exchange's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current trading week.
     *
     * @return the current trading week
     */
    public int getWeek() {
        return week;
    }

    /**
     * Checks if a stock with the given symbol is listed on the exchange.
     *
     * @param symbol the stock symbol
     * @return true if the stock is listed, false otherwise
     */
    public boolean hasStock(String symbol) {
        return symbol != null && stockMap.containsKey(symbol);
    }

    /**
     * Returns the stock with the given symbol.
     *
     * @param symbol the stock symbol
     * @return the stock with the given symbol
     * @throws IllegalArgumentException if no stock with the symbol exists
     */
    public Stock getStock(String symbol) {
        Stock stock = stockMap.get(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("No stock with symbol: " + symbol);
        }
        return stock;
    }

    /**
     * Returns a list of stocks whose symbol or company name contains the given
     * search string (case-insensitive). If the search string is null or blank,
     * returns all stocks.
     *
     * @param search the search string
     * @return the list of matching stocks
     */
    public List<Stock> findStocks(String search) {
        if (search == null || search.isBlank()) {
            return new ArrayList<>(stockMap.values());
        }
        String lower = search.toLowerCase();
        List<Stock> result = new ArrayList<>();
        for (Stock stock : stockMap.values()) {
            if (stock.getSymbol().toLowerCase().contains(lower)
                    || stock.getCompany().toLowerCase().contains(lower)) {
                result.add(stock);
            }
        }
        return result;
    }

    /**
     * Buys the given quantity of a stock for the given player. Creates and
     * commits a {@link Purchase} and notifies observers.
     *
     * @param symbol   the symbol of the stock to buy
     * @param quantity the quantity to buy
     * @param player   the buying player
     * @return the committed {@link Purchase}
     * @throws IllegalArgumentException if any argument is invalid
     * @throws IllegalStateException    if the purchase cannot be committed
     */
    public Transaction buy(String symbol, BigDecimal quantity, Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Stock stock = getStock(symbol); // throws if missing
        Share share = new Share(stock, quantity, stock.getSalesPrice());

        Purchase purchase = new Purchase(share, week);
        purchase.commit(player);

        notifyObservers(GameEvent.TRANSACTION_COMPLETED);
        return purchase;
    }

    /**
     * Sells the given quantity of a stock for the given player. Creates and
     * commits a {@link Sale} and notifies observers.
     *
     * @param symbol   the symbol of the stock to sell
     * @param quantity the quantity to sell
     * @param player   the selling player
     * @return the committed {@link Sale}
     * @throws IllegalArgumentException if any argument is invalid
     * @throws IllegalStateException    if the sale cannot be committed
     */
    public Transaction sell(String symbol, BigDecimal quantity, Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Stock stock = getStock(symbol);

        Sale sale = new Sale(stock, quantity, week);
        sale.commit(player);

        notifyObservers(GameEvent.TRANSACTION_COMPLETED);
        return sale;
    }

    /**
     * Advances the trading week by one. Updates each stock's price based on its
     * trend and a random noise factor, ensuring that prices do not fall below 1.
     * Notifies observers of the week advancement.
     */
    public void advance() {
        week++;
        for (Stock stock : stockMap.values()) {
            stock.updateTrend(random);
            double noise = (random.nextDouble() - 0.5) * 0.02;
            double changePercent = stock.getTrend() + noise;

            BigDecimal newPrice = stock.getSalesPrice()
                    .multiply(BigDecimal.valueOf(1 + changePercent))
                    .setScale(2, RoundingMode.HALF_UP);

            if (newPrice.compareTo(BigDecimal.ONE) < 0) {
                newPrice = BigDecimal.ONE;
            }
            stock.addNewSalesPrice(newPrice);
        }
        notifyObservers(GameEvent.WEEK_ADVANCED);
    }

    /**
     * Returns up to {@code limit} stocks with the largest positive price change
     * since the previous week, sorted descending by change.
     *
     * @param limit the maximum number of stocks to return
     * @return the top gainers
     * @throws IllegalArgumentException if {@code limit} is negative
     */
    public List<Stock> getGainers(int limit) {
        return topByChange(limit, true);
    }

    /**
     * Returns up to {@code limit} stocks with the largest negative price change
     * since the previous week, sorted ascending by change.
     *
     * @param limit the maximum number of stocks to return
     * @return the top losers
     * @throws IllegalArgumentException if {@code limit} is negative
     */
    public List<Stock> getLosers(int limit) {
        return topByChange(limit, false);
    }

    private List<Stock> topByChange(int limit, boolean descending) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit cannot be negative");
        }
        if (limit == 0 || stockMap.isEmpty()) {
            return Collections.emptyList();
        }
        List<Stock> sorted = new ArrayList<>(stockMap.values());
        if (descending) {
            sorted.sort((a, b) -> b.getLatestPriceChange().compareTo(a.getLatestPriceChange()));
        } else {
            sorted.sort((a, b) -> a.getLatestPriceChange().compareTo(b.getLatestPriceChange()));
        }
        int end = Math.min(limit, sorted.size());
        return new ArrayList<>(sorted.subList(0, end));
    }
}