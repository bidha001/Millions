package edu.ntnu.bidata.prog2.model;

import edu.ntnu.bidata.prog2.transaction.TransactionArchive;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The Player class represents a player in the stock market game.
 * It contains information about the player's name, starting money, current money, portfolio, and transaction archive.
 * The class also provides methods to manage the player's money, calculate net worth, and determine the player's status (Novice, Investor, or Speculator).
 *
 * @author Binit Dhungana
 * @version 2026-02-11
 */
public class Player {
    // Net-worth growth required to be classed as an Investor (20% increase).
    private static final BigDecimal INVESTOR_MULTIPLIER = new BigDecimal("1.20");
    // Net-worth growth required to be classed as a Speculator (doubled).
    private static final BigDecimal SPECULATOR_MULTIPLIER = new BigDecimal("2.00");
    // Minimum number of weeks of actual trading required for Investor.
    private static final int INVESTOR_MIN_WEEKS = 10;
    // Minimum number of weeks of actual trading required for Speculator.

    private static final int SPECULATOR_MIN_WEEKS = 20;
    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal money;
    private final Portfolio portfolio;
    private final TransactionArchive archive;

    /**
     * Constructs a new Player object with the specified name and starting money.
     *
     * @param name          The name of the player.
     * @param startingMoney The initial amount of money the player has at the start of the game.
     * @throws IllegalArgumentException if the name is null or blank, or if the starting money is null or not positive.
     */
    public Player(String name, BigDecimal startingMoney) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (startingMoney == null || startingMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Starting money must be positive");
        }
        this.name = name;
        this.startingMoney = startingMoney;
        this.money = startingMoney;
        this.portfolio = new Portfolio();
        this.archive = new TransactionArchive();
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
     * Retrieves the portfolio of the player.
     *
     * @return The portfolio of the player.
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Retrieves the transaction archive of the player.
     *
     * @return The transaction archive of the player.
     */
    public TransactionArchive getArchive() {
        return archive;
    }

    /**
     * Adds the specified amount of money to the player's current money.
     *
     * @param amount The amount of money to add.
     * @throws IllegalArgumentException if the amount is null or not positive.
     */
    public void addMoney(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.money = this.money.add(amount);
    }

    /**
     * Subtracts the specified amount of money from the player's current money.
     *
     * @param amount The amount of money to subtract.
     * @throws IllegalArgumentException if the amount is null or not positive.
     * @throws IllegalStateException    if the player does not have enough money to withdraw the specified amount.
     */
    public void withdrawMoney(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.money.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds: have " + money + ", need " + amount);
        }
        this.money = this.money.subtract(amount);
    }

    /**
     * Calculates and returns the net worth of the player, which is the sum of the player's current money and the net worth of their portfolio.
     *
     * @return The net worth of the player.
     */
    public BigDecimal getNetWorth() {
        return money.add(portfolio.getNetWorth());
    }


    /**
     * Determines the status of the player based on their net worth and trading history.
     * The status can be "Novice", "Investor", or "Speculator" based on the following criteria:
     * A player is classified as a "Speculator" if they have traded for at least 20 weeks and their net worth is at
     * least double their starting money.
     * A player is classified as an "Investor" if they have traded for at least 10 weeks and their net worth is at
     * least 20% higher than their starting money.
     * Otherwise, the player is classified as a "Novice".
     *
     * @return The status of the player ("Novice", "Investor", or "Speculator").
     */
    public String getStatus() {
        int tradingWeeks = archive.countDistinctWeeks();
        BigDecimal netWorth = getNetWorth();

        BigDecimal speculatorThreshold = startingMoney.multiply(SPECULATOR_MULTIPLIER);
        BigDecimal investorThreshold = startingMoney.multiply(INVESTOR_MULTIPLIER);

        if (tradingWeeks >= SPECULATOR_MIN_WEEKS && netWorth.compareTo(speculatorThreshold) >= 0) {
            return "Speculator";
        }
        if (tradingWeeks >= INVESTOR_MIN_WEEKS && netWorth.compareTo(investorThreshold) >= 0) {
            return "Investor";
        }
        return "Novice";
    }
}
