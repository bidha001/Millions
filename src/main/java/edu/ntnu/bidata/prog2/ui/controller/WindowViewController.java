package edu.ntnu.bidata.prog2.ui.controller;

import edu.ntnu.bidata.prog2.io.CsvStockDataSource;
import edu.ntnu.bidata.prog2.io.StockDataSource;
import edu.ntnu.bidata.prog2.market.Exchange;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the main window view.
 * Handles user actions and coordinates between the view and the model.
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

        BigDecimal money;
        try {
            money = new BigDecimal(moneyInput);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input for money!");
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

        BigDecimal quantity;
        try {
            quantity = new BigDecimal(quantityInput);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid quantity!");
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

        BigDecimal quantity;
        try {
            quantity = new BigDecimal(quantityInput);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid quantity!");
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
}