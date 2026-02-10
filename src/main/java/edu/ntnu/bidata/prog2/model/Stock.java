package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a stock with its company name, symbol, and historical prices.
 *
 */
public class Stock {
    private final String company;
    private final String symbol;
    private final List<BigDecimal> prices;

    public Stock(String company, String symbol, BigDecimal initialPrice) {
        this.company = company;
        this.symbol = symbol;
        this.prices = new java.util.ArrayList<>();
        this.prices.add(initialPrice);
    }

    /**
     *
     * @return
     */
    public String getCompany() {
        return company;
    }

    /**
     *
     * @return
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @return
     */
    public BigDecimal getSalesPrice() {
        return prices.get(prices.size() - 1);
    }

    /**
     *
     * @param newPrice
     */
    public void addNewSalesPrice(BigDecimal newPrice) {
        prices.add(newPrice);
    }
}
