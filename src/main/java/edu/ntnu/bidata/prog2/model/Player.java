package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;

public class Player {
    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal money;
    private final Portfolio portfolio;

    public Player(String name, BigDecimal startingMoney, Portfolio portfolio) {
        this.name = name;
        this.startingMoney = startingMoney;
        this.money = startingMoney;
        this.portfolio = new Portfolio();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getStartingMoney() {
        return startingMoney;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
