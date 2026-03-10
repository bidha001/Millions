package edu.ntnu.bidata.prog2.service;

import edu.ntnu.bidata.prog2.market.Exchange;
import edu.ntnu.bidata.prog2.model.Stock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // Create stock market
        Map<String, Stock> stocks = new HashMap<>();

        stocks.put("AAPL", new Stock("Apple", "AAPL", new BigDecimal("150")));
        stocks.put("TSLA", new Stock("Tesla", "TSLA", new BigDecimal("200")));
        stocks.put("META", new Stock("Meta", "META", new BigDecimal("180")));

        Exchange exchange = new Exchange("NASDAQ", stocks);

        // Print stocks
        System.out.println("=== Stock Market ===");

        for (Stock stock : stocks.values()) {
            System.out.println(
                    stock.getCompany() +
                            " (" + stock.getSymbol() + ") : " +
                            stock.getSalesPrice()
            );
        }
        System.out.println("\nAdvancing week...");
        exchange.advance();

        System.out.println("\n=== Updated Prices ===");

        for (Stock stock : stocks.values()) {
            System.out.println(
                    stock.getCompany() +
                            " (" + stock.getSymbol() + ") : " +
                            stock.getSalesPrice()
            );
        }
        System.out.println("\nTop Gainer:");
        System.out.println(exchange.getGainers(1).get(0).getCompany());

        System.out.println("\nTop Loser:");
        System.out.println(exchange.getLosers(1).get(0).getCompany());
    }

}