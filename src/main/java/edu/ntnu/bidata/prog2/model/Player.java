package edu.ntnu.bidata.prog2.model;


import java.math.BigDecimal;

/**
 * Represents a player in the stock market game.
 * Each player has a name, starting money, current money, and a portfolio of shares.
 */
public class Player {
    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal money;
    private final Portfolio portfolio;

    /**
     * Constructs a new Player with the specified name and starting money.
     *
     * @param name          The name of the player.
     * @param startingMoney The initial amount of money the player starts with.
     */
    public Player(String name, BigDecimal startingMoney) {
        this.name = name;

        //initial money that player starts with
        this.startingMoney = startingMoney;

        //money that player currently has after buying and selling shares
        this.money = startingMoney;

        //each player has a portfolio to hold their shares
        this.portfolio = new Portfolio();
    }

    /**
     * Retrieves the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the starting money of the player.
     *
     * @return The starting money of the player.
     */
    public BigDecimal getStartingMoney() {
        return startingMoney;
    }

    /**
     * Retrieves the current money of the player.
     *
     * @return The current money of the player.
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * Sets the current money of the player.
     *
     * @param money The new amount of money for the player.
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * Retrieves the portfolio of the player.
     *
     * @return The portfolio of the player.
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Calculates the net worth of the player, which is the sum of their current money and the net worth of their portfolio.
     *
     * @return The net worth of the player.
     */
    public BigDecimal getNetWorth() {
        return money.add(portfolio.getNetWorth());
    }

    /**
     * Determines the status of the player based on the number of weeks they have been playing.
     *
     * @param weeks The number of weeks the player has been playing.
     * @return The status of the player as a String ("Speculator", "Investor", or "Beginner").
     */
    public String getStatus(int weeks) {

        if (weeks >= 20) {
            return "Speculator";
        }

        if (weeks >= 10) {
            return "Investor";
        }

        return "Beginner";
    }
}
