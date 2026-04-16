package edu.ntnu.bidata.prog2.io;

import edu.ntnu.bidata.prog2.model.Stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A CSV-based implementation of StockDataSource.
 * Reads and writes stock data in CSV format with the structure:
 *   symbol,company,price
 * Lines starting with '#' and empty lines are treated as comments and ignored.
 */
public class CsvStockDataSource implements StockDataSource {

    private static final String COMMENT_PREFIX = "#";
    private static final String SEPARATOR = ",";

    /**
     * Reads stock data from a CSV file.
     *
     * @param filePath the path to the CSV file
     * @return a map of stock symbols to Stock objects
     * @throws IOException if the file cannot be read or the format is invalid
     */
    @Override
    public Map<String, Stock> read(String filePath) throws IOException {

        Map<String, Stock> stocks = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // skip comments and blank lines
                if (line.startsWith(COMMENT_PREFIX) || line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(SEPARATOR);

                if (parts.length < 3) {
                    throw new IOException("Invalid CSV format on line " + lineNumber
                            + ": expected 'symbol,company,price'");
                }

                String symbol = parts[0].trim();
                String company = parts[1].trim();

                BigDecimal price;
                try {
                    price = new BigDecimal(parts[2].trim());
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid price on line " + lineNumber
                            + ": " + parts[2]);
                }

                Stock stock = new Stock(company, symbol, price);
                stocks.put(symbol, stock);
            }
        }

        return stocks;
    }

    /**
     * Writes stock data to a CSV file using the latest sales price of each stock.
     *
     * @param stocks   the map of stocks to write
     * @param filePath the path to write the file to
     * @throws IOException if the file cannot be written
     */
    @Override
    public void write(Map<String, Stock> stocks, String filePath) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("# Ticker,Name,Price");
            writer.newLine();

            for (Stock stock : stocks.values()) {
                writer.write(stock.getSymbol()
                        + SEPARATOR + stock.getCompany()
                        + SEPARATOR + stock.getSalesPrice().toString());
                writer.newLine();
            }
        }
    }
}