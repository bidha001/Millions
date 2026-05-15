package edu.ntnu.bidata.prog2.ui.controller;

import edu.ntnu.bidata.prog2.calculator.PurchaseCalculator;
import edu.ntnu.bidata.prog2.calculator.SaleCalculator;
import edu.ntnu.bidata.prog2.calculator.TransactionCalculator;
import edu.ntnu.bidata.prog2.io.CsvStockDataSource;
import edu.ntnu.bidata.prog2.io.StockDataSource;
import edu.ntnu.bidata.prog2.market.Exchange;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.observer.GameObserver;
import edu.ntnu.bidata.prog2.transaction.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * The WindowViewController serves as the intermediary between the UI and the game model.
 * It manages the Player and Exchange instances, processes user inputs, and provides
 * data in a format suitable for display. It also handles error checking and throws
 * IllegalArgumentException with user-friendly messages when inputs are invalid.
 */
public class WindowViewController {
    private Player player;
    private Exchange exchange;

    /**
     * Initializes a new game with the given player name, starting money, and stock data file.
     *
     * @param name       the player's name (must contain only letters and spaces)
     * @param moneyInput the starting money as a string (must be a positive number)
     * @param filePath   the path to the stock data file (must not be empty)
     * @throws IllegalArgumentException if any input is invalid
     */
    public void startNewGame(String name, String moneyInput, String filePath) {

        if (name == null || !name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Name must contain only letters!");
        }

        if (moneyInput == null || moneyInput.isBlank()) {
            throw new IllegalArgumentException("Money cannot be empty!");
        }

        BigDecimal money;
        try {
            money = new BigDecimal(moneyInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input for money: '" + moneyInput + "'");
        }

        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Money must be greater than 0!");
        }

        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Please select a stock data file!");
        }

        Map<String, Stock> stocks;
        try {
            StockDataSource dataSource = new CsvStockDataSource();
            stocks = dataSource.read(filePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load stock file: " + e.getMessage());
        }

        if (stocks.isEmpty()) {
            throw new IllegalArgumentException("The selected file contains no valid stocks!");
        }

        this.player = new Player(name, money);
        this.exchange = new Exchange("Market", new ArrayList<>(stocks.values()));
    }

    /**
     * Retrieves the current player.
     * Exposed as part of the controller's public API — useful for tests
     * and potential future features that need to inspect player state.
     *
     * @return the current Player, or null if no game has been started
     */
    @SuppressWarnings("unused")
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the current exchange.
     * Exposed as part of the controller's public API — useful for tests
     * and potential future features that need to inspect exchange state.
     *
     * @return the current Exchange, or null if no game has been started
     */
    @SuppressWarnings("unused")
    public Exchange getExchange() {
        return exchange;
    }


    public void nextWeek() {
        exchange.advance();
    }

    /**
     * Searches for stocks matching the given query string.
     *
     * @param query the search query (e.g., part of a stock symbol)
     * @return a list of matching stocks, or an empty list if no exchange is active
     */
    public List<Stock> searchStocks(String query) {

        if (exchange == null) {
            return new ArrayList<>();
        }

        return exchange.findStocks(query);
    }

    /**
     * Buys a given quantity of the selected stock for the current player.
     *
     * @param stock         the stock to buy
     * @param quantityInput the quantity to buy as a string
     * @throws IllegalArgumentException if the input is invalid or no game is active
     */
    public void buy(Stock stock, String quantityInput) {

        if (player == null) {
            throw new IllegalArgumentException("Start a game first!");
        }
        if (stock == null) {
            throw new IllegalArgumentException("Select a stock first!");
        }
        if (quantityInput == null || quantityInput.isBlank()) {
            throw new IllegalArgumentException("Quantity cannot be empty!");
        }

        BigDecimal quantity;
        try {
            quantity = new BigDecimal(quantityInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity: '" + quantityInput + "'");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0!");
        }

        exchange.buy(stock.getSymbol(), quantity, player);
    }

    /**
     * Sells a given quantity of the selected share from the player's portfolio.
     *
     * @param selectedShare the share to sell (must belong to the player)
     * @param quantityInput the quantity to sell as a string
     * @throws IllegalArgumentException if the input is invalid, the share is not owned,
     *                                  or no game is active
     */
    public void sell(Share selectedShare, String quantityInput) {

        if (player == null) {
            throw new IllegalArgumentException("Start a game first!");
        }
        if (selectedShare == null) {
            throw new IllegalArgumentException("Select a share first!");
        }
        if (quantityInput == null || quantityInput.isBlank()) {
            throw new IllegalArgumentException("Quantity cannot be empty!");
        }

        BigDecimal quantity;
        try {
            quantity = new BigDecimal(quantityInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity: '" + quantityInput + "'");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0!");
        }

        Stock stock = selectedShare.getStock();
        BigDecimal owned = player.getPortfolio().getTotalQuantity(stock.getSymbol());
        if (quantity.compareTo(owned) > 0) {
            throw new IllegalArgumentException(
                    "You don't own that many shares! (own " + owned + ")");
        }

        exchange.sell(stock.getSymbol(), quantity, player);
    }

    /**
     * Sells all shares in the player's portfolio, grouped by stock symbol.
     * Each stock's total quantity is sold in a single transaction.
     *
     * @throws IllegalArgumentException if no game is active
     */
    public void sellAllShares() {
        if (player == null) {
            throw new IllegalArgumentException("Start a game first!");
        }

        // Group total quantity per stock symbol — separate lots become a single Sale.
        Map<String, Stock> stocksBySymbol = new LinkedHashMap<>();
        Map<String, BigDecimal> totalsBySymbol = new LinkedHashMap<>();

        for (Share share : player.getPortfolio().getShares()) {
            String symbol = share.getStock().getSymbol();
            stocksBySymbol.putIfAbsent(symbol, share.getStock());
            totalsBySymbol.merge(symbol, share.getQuantity(), BigDecimal::add);
        }

        for (String symbol : stocksBySymbol.keySet()) {
            exchange.sell(symbol, totalsBySymbol.get(symbol), player);
        }
    }

    /**
     * Retrieves key information about the player for display in the UI.
     *
     * @return a map of player attributes (name, money, net worth, week, status),
     *         or an empty map if no game is active
     */
    public Map<String, String> getPlayerInfo() {

        Map<String, String> info = new HashMap<>();

        if (player == null || exchange == null) {
            return info;
        }

        int week = exchange.getWeek();

        info.put("name", player.getName());
        info.put("money", format(player.getMoney()));
        info.put("netWorth", format(player.getNetWorth()));
        info.put("week", String.valueOf(week));
        info.put("status", player.getStatus());

        return info;
    }

    /**
     * Retrieves the player's current portfolio shares.
     *
     * @return a list of shares in the player's portfolio, or an empty list if no game is active
     */
    public List<Share> getPortfolioData() {

        if (player == null) {
            return new ArrayList<>();
        }

        return player.getPortfolio().getShares();
    }

    /**
     * Retrieves the player's transaction history from their archive.
     *
     * @return a list of transactions, or an empty list if no game is active
     */
    public List<Transaction> getTransactionData() {

        if (player == null) {
            return new ArrayList<>();
        }

        return player.getArchive().getTransactions();
    }

    /**
     * Adds a GameObserver to the exchange to receive updates on game events.
     *
     * @param observer the GameObserver to add
     */
    public void addGameObserver(GameObserver observer) {
        if (exchange != null) {
            exchange.addObserver(observer);
        }
    }

    /**
     * Calculates the current market value of a share based on its stock's sales price.
     *
     * @param share the share to evaluate
     * @return the current value of the share as a string, or "0.00" if the share is null
     */
    public String getShareValue(Share share) {
        if (share == null) {
            return "0.00";
        }

        BigDecimal value = share.getStock()
                .getSalesPrice()
                .multiply(share.getQuantity())
                .setScale(2, RoundingMode.HALF_UP);

        return value.toString();
    }

    /**
     * Builds a display string with key details about a stock for the stock list.
     *
     * @param stock the stock to format
     * @return a string with price, high, low, and change, or an empty string if stock is null
     */
    public String getStockDetails(Stock stock) {
        if (stock == null) {
            return "";
        }

        return "Price: " + format(stock.getSalesPrice())
                + " | High: " + format(stock.getHighestPrice())
                + " | Low: " + format(stock.getLowestPrice())
                + " | Change: " + format(stock.getLatestPriceChange());
    }

    /**
     * Builds a preview text of the cost of a potential purchase.
     *
     * @param stock         the stock to buy
     * @param quantityInput the quantity as a string
     * @return a multi-line string showing gross, commission, tax, and total cost
     * @throws IllegalArgumentException if the quantity is invalid
     */
    public String previewPurchase(Stock stock, String quantityInput) {
        BigDecimal quantity;
        try {
            quantity = new BigDecimal(quantityInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity: '" + quantityInput + "'");
        }

        Share share = new Share(stock, quantity, stock.getSalesPrice());
        TransactionCalculator calc = new PurchaseCalculator(share);

        return "Stock:       " + stock.getSymbol()
                + "\nQuantity:    " + quantity
                + "\nPrice:       " + format(stock.getSalesPrice())
                + "\n--------------------"
                + "\nGross:       " + format(calc.calculateGross())
                + "\nCommission:  " + format(calc.calculateCommission())
                + "\nTax:         " + format(calc.calculateTax())
                + "\n--------------------"
                + "\nTotal cost:  " + format(calc.calculateTotal());
    }

    /**
     * Builds a preview text of the proceeds from a potential sale.
     *
     * @param share         the share to sell
     * @param quantityInput the quantity as a string
     * @return a multi-line string showing gross, commission, tax, and total proceeds
     * @throws IllegalArgumentException if the quantity is invalid
     */
    public String previewSale(Share share, String quantityInput) {
        BigDecimal quantity;
        try {
            quantity = new BigDecimal(quantityInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity: '" + quantityInput + "'");
        }

        Stock stock = share.getStock();
        Share previewShare = new Share(stock, quantity, share.getPurchasePrice());
        TransactionCalculator calc = new SaleCalculator(previewShare);

        return "Stock:       " + stock.getSymbol()
                + "\nQuantity:    " + quantity
                + "\nSale price:  " + format(stock.getSalesPrice())
                + "\n--------------------"
                + "\nGross:       " + format(calc.calculateGross())
                + "\nCommission:  " + format(calc.calculateCommission())
                + "\nTax:         " + format(calc.calculateTax())
                + "\n--------------------"
                + "\nYou receive: " + format(calc.calculateTotal());
    }

    /**
     * Builds a receipt string for the last transaction in the player's archive.
     *
     * @return a multi-line string with transaction details and calculations,
     *         or an empty string if no transactions exist
     */
    public String getLastTransactionReceipt() {
        if (player == null || player.getArchive().getTransactions().isEmpty()) {
            return "";
        }

        List<Transaction> all = player.getArchive().getTransactions();
        Transaction last = all.getLast();
        TransactionCalculator calc = last.getCalculator();

        return "=== Transaction Receipt ==="
                + "\nType:        " + last.getClass().getSimpleName()
                + "\nWeek:        " + last.getWeek()
                + "\nStock:       " + last.getShare().getStock().getSymbol()
                + "\nQuantity:    " + last.getShare().getQuantity()
                + "\n--------------------"
                + "\nGross:       " + format(calc.calculateGross())
                + "\nCommission:  " + format(calc.calculateCommission())
                + "\nTax:         " + format(calc.calculateTax())
                + "\n--------------------"
                + "\nTotal:       " + format(calc.calculateTotal());
    }

    /**
     * Builds a final summary string with the player's performance at the end of the game.
     *
     * @return a multi-line string summarizing the player's name, status, weeks played,
     *         starting money, final money, and net result, or an empty string if no game is active
     */
    public String getFinalSummary() {
        if (player == null || exchange == null) {
            return "";
        }

        BigDecimal starting = player.getStartingMoney();
        BigDecimal finalMoney = player.getMoney();
        BigDecimal diff = finalMoney.subtract(starting);
        String sign = diff.signum() >= 0 ? "+" : "";

        return "=== Final Summary ==="
                + "\nPlayer:       " + player.getName()
                + "\nStatus:       " + player.getStatus()
                + "\nWeeks played: " + exchange.getWeek()
                + "\n--------------------"
                + "\nStarted with: " + format(starting)
                + "\nEnded with:   " + format(finalMoney)
                + "\nResult:       " + sign + format(diff);
    }

    /**
     * Retrieves the top gainers (stocks with the biggest price increase since last week).
     *
     * @param limit the maximum number of gainers to return
     * @return a list of top gaining stocks, or an empty list if no game is active
     */
    public List<Stock> getGainers(int limit) {
        if (exchange == null) {
            return new ArrayList<>();
        }
        return exchange.getGainers(limit);
    }

    /**
     * Retrieves the top losers (stocks with the biggest price decrease since last week).
     *
     * @param limit the maximum number of losers to return
     * @return a list of top losing stocks, or an empty list if no game is active
     */
    public List<Stock> getLosers(int limit) {
        if (exchange == null) {
            return new ArrayList<>();
        }
        return exchange.getLosers(limit);
    }

    /**
     * Formats a stock's symbol and price change for display in the market movers list.
     *
     * @param stock the stock to format
     * @return a string with the stock symbol and price change, or an empty string if stock is null
     */
    public String formatMarketMover(Stock stock) {
        if (stock == null) {
            return "";
        }
        BigDecimal change = stock.getLatestPriceChange();
        String sign = change.signum() >= 0 ? "+" : "";
        return String.format("%-6s %s%s", stock.getSymbol(), sign, format(change));
    }

    /**
     * Helper method to format BigDecimal values to a string with 2 decimal places.
     *
     * @param value the BigDecimal value to format
     * @return a string representation of the value with 2 decimal places
     */
    private String format(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }
}