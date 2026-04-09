package edu.ntnu.bidata.prog2.ui.controller;

import edu.ntnu.bidata.prog2.market.Exchange;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.Transaction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowViewController {
    private Player player;
    private Exchange exchange;

    public void startNewGame(String name, String moneyInput) {

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

        Map<String, Stock> stocks = loadStocks();

        this.player = new Player(name, money);
        this.exchange = new Exchange("Market", stocks);
    }

    public Player getPlayer() {
        return player;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void nextWeek() {
        exchange.advance();
    }

    public List<Stock> searchStocks(String query) {

        if (exchange == null) {
            return new ArrayList<>();
        }

        return exchange.findStocks(query);
    }

    private Map<String, Stock> loadStocks() {

        Map<String, Stock> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/Stocks.csv"))
        )) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length < 3) continue;

                String symbol = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);

                Stock stock = new Stock(name, symbol, new BigDecimal(price));
                map.put(symbol, stock);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading stocks!");
        }

        return map;
    }

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

    public void sell(Stock stock, String quantityInput) {

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

    public List<Share> getPortfolioData() {

        if (player == null) {
            return new ArrayList<>();
        }

        return player.getPortfolio().getAllShares();
    }

    public List<Transaction> getTransactionData() {

        if (player == null) {
            return new ArrayList<>();
        }

        return player.getArchive().getTransactions();
    }
}
