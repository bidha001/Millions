package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The Stock class represents a stock in the stock market.
 * It contains information about the company,
 * the stock symbol, and a list of sales prices for the stock.
 *
 * @author Binit Dhungana
 * @version 2026-02-11
 */
public class Stock {
    private final String company;
    private final String symbol;
    //declaration of Stock has a list of price
    private final List<BigDecimal> prices;

    /**
     * Constructs a new Stock object with the specified company name, stock symbol, and initial price.
     *
     * @param company      The name of the company associated with this stock.
     * @param symbol       The stock symbol, which is a unique identifier for the stock.
     * @param initialPrice The initial sales price of the stock.
     */
    public Stock(String company, String symbol, BigDecimal initialPrice) {
        this.company = company;
        this.symbol = symbol;
        // create an empty list to store prices
        this.prices = new ArrayList<>();
        // Add the initial price to the list of prices
        this.prices.add(initialPrice);
    }

    /**
     * Retrieves the name of the company associated with this stock.
     *
     * @return The name of the company.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Retrieves the stock symbol, which is a unique identifier for the stock.
     *
     * @return The stock symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Retrieves the current sales price of the stock, which is the last price in the list.
     *
     * @return The current sales price of the stock.
     */
    public BigDecimal getSalesPrice() {
        return prices.get(prices.size() - 1);
    }

    /**
     * Adds a new sales price to the list of prices.
     *
     * @param newPrice The new sales price to be added.
     */
    public void addNewSalesPrice(BigDecimal newPrice) {
        prices.add(newPrice);
    }
}
