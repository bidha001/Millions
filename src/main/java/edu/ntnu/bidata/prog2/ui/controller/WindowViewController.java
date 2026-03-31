package edu.ntnu.bidata.prog2.ui.controller;

import edu.ntnu.bidata.prog2.market.Exchange;
import edu.ntnu.bidata.prog2.model.Player;
import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;

import java.math.BigDecimal;
import java.util.Map;

public class WindowViewController {
    private Player player;
    private Exchange exchange;

    public void startNewGame(String name, BigDecimal startingMoney, Map<String, Stock> stocks) {
        this.player = new Player(name, startingMoney);
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

    public void buy(Share share) {
        exchange.buy(player, share);
    }

    public void sell(Stock stock, BigDecimal quantity) {

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
                stock.getSalesPrice()
        );

        exchange.sell(player, shareToSell);
    }

}
