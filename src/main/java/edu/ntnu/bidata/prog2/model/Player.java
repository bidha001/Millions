package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;

public class Player {
    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal money;
    private final Portfolio portfolio;

    public Player(String name, BigDecimal startingMoney, Portfolio portfolio) {
        this.name = name;

        //initial money that player starts with
        this.startingMoney = startingMoney;

        //money that player currently has after buying and selling shares
        this.money = startingMoney;

        //each player has a portfolio to hold their shares
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
