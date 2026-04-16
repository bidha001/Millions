package edu.ntnu.bidata.prog2.io;

import edu.ntnu.bidata.prog2.model.Stock;

import java.io.IOException;
import java.util.Map;

/**
 * Represents a source capable of reading and writing stock data.
 * Implementations can support different file formats (CSV, JSON, XML, etc.),
 * making the system easy to extend.
 */
public interface StockDataSource {

    /**
     * Reads stock data from the specified file path.
     *
     * @param filePath the path to the file to read from
     * @return a map of stock symbols to Stock objects
     * @throws IOException if an I/O error occurs while reading the file
     */
    Map<String, Stock> read(String filePath) throws IOException;

    /**
     * Writes stock data to the specified file path.
     *
     * @param stocks   a map of stock symbols to Stock objects
     * @param filePath the path to the file to write to
     * @throws IOException if an I/O error occurs while writing the file
     */
    void write(Map<String, Stock> stocks, String filePath) throws IOException;
}