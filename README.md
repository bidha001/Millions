# Millions 💰

**Millions** is a JavaFX stock market simulation game developed as part of the **IDATx2003 Programmering 2** 
course at NTNU (spring 2026).

Players start with a given amount of capital, buy and sell shares on a simulated exchange, and try to grow their net 
worth week by week. Stock prices move randomly but with a persistent trend, and every transaction carries realistic 
commission and tax rules.

> **Disclaimer**: This application is the result of a university project for the course IDATx2003 Programmering 2 at NTNU.
All stock data, prices, and financial calculations are fictional and are intended for educational use only. 
This is not financial advice.

---

## Features

- **Buy and Sell Shares**: Full transaction flow with gross, commission, tax, and total breakdown.
- **Market Movers**: Live panel showing the week's top gainers and losers.
- **Searchable Stock List**: Filter stocks by symbol or company name.
- **Transaction History**: Every buy and sell is archived with a complete receipt.
- **Player Progression**: Advance from Novice → Investor → Speculator based on gains and time played.
- **Live Statistics**: Current price, highest price, lowest price, latest change, and historical prices per stock.
- **Sell All & Quit**: Liquidate the entire portfolio and receive a final performance summary.
- **Custom Stock Files**: Load any CSV file with your own stock data.
- **Auto-Updating UI**: Observer pattern keeps the GUI in sync with the model automatically.

---

## Tech Stack

**Language:**
- Java 25

**Frontend:**
- JavaFX 25.0.2 (pure Java, no FXML)

**Build & Test:**
- Maven 3.9+
- JUnit 5

---

## Folder Structure

```
src
├── main
│   └── java/edu/ntnu/bidata/prog2
│       ├── calculator         # Transaction calculators (Purchase, Sale)
│       ├── io                 # File handling (CSV reader/writer)
│       ├── market             # Exchange
│       ├── model              # Player, Portfolio, Stock, Share
│       ├── observer           # Observer pattern (GameObserver, Observable)
│       ├── transaction        # Purchase, Sale, Factory, Archive
│       └── ui
│           ├── controller     # WindowViewController
│           └── view           # WindowView (JavaFX)
└── test
    └── java/edu/ntnu/bidata/prog2
        ├── calculator         # Calculator unit tests
        └── model              # Stock, Portfolio, Player tests
```

## Getting Started

### Prerequisites
- Java 25+
- Maven 3.9+
- A CSV file with stock data (see [CSV Format](#csv-format) below)

### Run the App

```bash
# Clone the project
git clone <your-repo-url>
cd Millions

# Build and run
mvn clean package
mvn javafx:run
```

Maven will automatically download JavaFX and JUnit dependencies on first run.

### Run the Tests

```bash
mvn test
```

The project includes 50+ unit tests covering the business-critical classes (calculators, Stock, Portfolio, Player).

---

## CSV Format

Stock data files use a simple comma-separated format:

```
# S&P 500 Companies by Market Cap
# Ticker,Name,Price

NVDA,Nvidia,191.27
AAPL,Apple Inc.,276.43
MSFT,Microsoft,404.68
```

**Rules:**
- Lines starting with `#` are comments
- Blank lines are ignored
- Each data line is `symbol,company,price`
- Use a period `.` as the decimal separator

## User Flow

- Start a new game (enter name, starting money, and select a CSV file)
- Explore the stock list or search by symbol/company name
- Select a stock and view its statistics
- Enter a quantity and click BUY or SELL
- Review the transaction preview, then confirm
- Advance to the next week and watch prices update
- Track progress in the Transactions list
- Click SELL ALL & QUIT to end the game and see a final summary

---

## Architecture

The project follows the **Model-View-Controller (MVC)** pattern combined with the **Observer pattern** for UI updates.

| Layer        | Responsibility                                      |
|--------------|-----------------------------------------------------|
| Model        | Business logic, domain objects, state management    |
| View         | Presentation (JavaFX) — purely visual, no logic     |
| Controller   | Coordinates user actions and prepares display data  |

**Design Patterns Used:**

| Pattern   | Where                                                               |
|-----------|---------------------------------------------------------------------|
| Observer  | `Exchange extends Observable`, `WindowView implements GameObserver` |
| Factory   | `TransactionFactory` creates Purchase/Sale transactions             |
| Strategy  | `TransactionCalculator` interface (Purchase/Sale variants)          |

File I/O goes through the `StockDataSource` interface, making it easy to add new formats (JSON, XML) without touching 
existing code (Open/Closed Principle).

---

## License

This project is licensed for educational use only as part of the IDATx2003 Programmering 2 course at NTNU.

---

## Author

- Binit Dhungana

This project was developed as part of the IDATx2003 Programmering 2 course at NTNU (spring 2026). Work covered all areas 
of the application: domain model, business logic, file handling, JavaFX GUI, unit testing, and documentation.

---

## Use of AI Tools

During the development of this project, AI tools such as ChatGPT and Claude were used for clarification, guidance, and 
idea refinement. These tools supported the development process by helping identify design improvements, refactor code 
toward stricter MVC separation, and apply best practices like the Observer and Factory patterns. All code was thoroughly
reviewed, adapted, and tested to ensure a clear understanding of every decision. The overall architecture, feature set,
and implementation choices were the author's own.