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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the main window view.
 * Handles user actions, coordinates between the view and the model,
 * and prepares display-ready data for the view (so the view contains
 * no business logic or formatting).
 */
public class WindowViewController {
    private Player player;
    private Exchange exchange;

    /**
     * Starts a new game with the given name, starting money, and stock data file.
     *
     * @param name       the player's name (letters and spaces only)
     * @param moneyInput the starting money as a string
     * @param filePath   the path to the stock data file
     * @throws IllegalArgumentException if any input is invalid or the file cannot be read
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
        this.exchange = new Exchange("Market", stocks);
    }

    /**
     * Retrieves the current player.
     *
     * @return the current Player, or null if no game has been started
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the current exchange.
     *
     * @return the current Exchange, or null if no game has been started
     */
    public Exchange getExchange() {
        return exchange;
    }

    /**
     * Advances the game to the next week.
     */
    public void nextWeek() {
        exchange.advance();
    }

    /**
     * Searches for stocks whose symbol or company name contains the given query.
     *
     * @param query the search string
     * @return a list of matching stocks, or an empty list if no game is active
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

        Share share = new Share(
                stock,
                quantity,
                stock.getSalesPrice(),
                null
        );

        exchange.buy(player, share);
    }

    /**
     * Sells a given quantity of the selected share for the current player.
     *
     * @param selectedShare the share to sell
     * @param quantityInput the quantity to sell as a string
     * @throws IllegalArgumentException if the input is invalid, no game is active,
     *                                  or the player doesn't own enough shares
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

        Share existingShare = player.getPortfolio().getShareByStock(stock);

        if (existingShare == null) {
            throw new IllegalArgumentException("You don't own this share!");
        }

        if (quantity.compareTo(existingShare.getQuantity()) > 0) {
            throw new IllegalArgumentException("You don't own that many shares!");
        }

        Share shareToSell = new Share(
                stock,
                quantity,
                existingShare.getPurchasePrice(),
                stock.getSalesPrice()
        );

        exchange.sell(player, shareToSell);
    }

    /**
     * Sells all shares in the player's portfolio at current market prices.
     * Each sale goes through Exchange.sell(), so each is recorded as a proper
     * transaction in the archive and fires observer events.
     *
     * @throws IllegalArgumentException if no game is active
     */
    public void sellAllShares() {
        if (player == null) {
            throw new IllegalArgumentException("Start a game first!");
        }

        // Copy the list — selling modifies the portfolio while we iterate
        List<Share> shares = new ArrayList<>(player.getPortfolio().getAllShares());

        for (Share share : shares) {
            Stock stock = share.getStock();
            Share toSell = new Share(
                    stock,
                    share.getQuantity(),
                    share.getPurchasePrice(),
                    stock.getSalesPrice()
            );
            exchange.sell(player, toSell);
        }
    }

    /**
     * Retrieves a map of player information for display in the view.
     *
     * @return a map with keys: name, money, netWorth, week, status
     */
    public Map<String, String> getPlayerInfo() {

        Map<String, String> info = new HashMap<>();

        if (player == null || exchange == null) {
            return info;
        }

        int week = exchange.getWeek();

        info.put("name", player.getName());
        info.put("money", player.getMoney().toString());
        info.put("netWorth", player.getNetWorth().toString());
        info.put("week", String.valueOf(week));
        info.put("status", player.getStatus(week));

        return info;
    }

    /**
     * Retrieves all shares in the player's portfolio.
     *
     * @return a list of shares, or an empty list if no game is active
     */
    public List<Share> getPortfolioData() {

        if (player == null) {
            return new ArrayList<>();
        }

        return player.getPortfolio().getAllShares();
    }

    /**
     * Retrieves all transactions from the player's archive.
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
     * Registers an observer to be notified of changes in the game model.
     * Call this after startNewGame() — before that, no exchange exists yet.
     *
     * @param observer the observer to register
     */
    public void addGameObserver(GameObserver observer) {
        if (exchange != null) {
            exchange.addObserver(observer);
        }
    }

    /**
     * Calculates the current total value of a share (current price × quantity).
     *
     * @param share the share
     * @return the current value as a string with 2 decimal places, or "0.00" if the share is null
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
     * Builds a display-ready summary of a stock's key statistics.
     *
     * @param stock the stock
     * @return a formatted string with price, high, low, and change, or an empty string if null
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
     * Builds a preview text of the costs associated with a potential purchase.
     *
     * @param stock         the stock being considered
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

        Share share = new Share(stock, quantity, stock.getSalesPrice(), null);
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
     * @param share         the share being considered for sale
     * @param quantityInput the quantity as a string
     * @return a multi-line string showing gross, commission, tax, and amount received
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
        Share previewShare = new Share(
                stock, quantity, share.getPurchasePrice(), stock.getSalesPrice());
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
     * Builds a receipt text of the most recent transaction in the player's archive.
     *
     * @return a formatted receipt string, or empty if no transactions exist
     */
    public String getLastTransactionReceipt() {
        if (player == null || player.getArchive().getTransactions().isEmpty()) {
            return "";
        }

        List<Transaction> all = player.getArchive().getTransactions();
        Transaction last = all.get(all.size() - 1);
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
     * Builds a final summary of the player's performance.
     *
     * @return a multi-line string with name, status, starting money, final money,
     *         result, and weeks played
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
                + "\nStatus:       " + player.getStatus(exchange.getWeek())
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
     * Builds a display-ready string for a stock's market movement.
     *
     * @param stock the stock
     * @return a formatted line with symbol and change (e.g., "AAPL  +5.23")
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
     * Formats a BigDecimal value to 2 decimal places for display.
     *
     * @param value the value to format
     * @return the value as a string with 2 decimal places
     */
    private String format(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }
}