package com.stockcontrol.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Represents a stock item with information such as product code, product title,
 * product description, unit price, and quantity in stock.
 */
public class ASCStockItem {

    private String productCode;
    private String productTitle;
    private String productDescription;
    private int unitPricePounds;
    private int unitPricePence;
    private int quantityInStock;

    /**
     * Constructor for ASCStockItem class.
     *
     * @param productCode The product code of the stock item.
     * @param productTitle The product title.
     * @param productDescription The product description.
     * @param unitPricePounds The unit price in pounds.
     * @param unitPricePence The unit price in pence.
     * @param quantityInStock The quantity of the item in stock.
     */
    public ASCStockItem(String productCode, String productTitle, String productDescription, int unitPricePounds,
            int unitPricePence, int quantityInStock) {
        this.productCode = productCode;
        this.productTitle = limitStringLength(productTitle, 120);
        this.productDescription = limitStringLength(productDescription, 500);
        this.unitPricePounds = unitPricePounds;
        this.unitPricePence = unitPricePence;
        this.quantityInStock = quantityInStock;
    }

    /**
     * Limits the length of a string to a specified maximum length.
     *
     * @param input The input string.
     * @param maxLength The maximum length allowed.
     * @return The truncated or original string.
     */
    private String limitStringLength(String input, int maxLength) {
        if (input.length() > maxLength) {
            return input.substring(0, maxLength).trim();
        }
        return input;
    }

    /**
     * Gets the product code of the stock item.
     *
     * @return The product code.
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Gets the product title of the stock item.
     *
     * @return The product title.
     */
    public String getProductTitle() {
        return productTitle;
    }

    /**
     * Gets the product description of the stock item.
     *
     * @return The product description.
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Gets the unit price in pounds of the stock item.
     *
     * @return The unit price in pounds.
     */
    public int getUnitPricePounds() {
        return unitPricePounds;
    }

    /**
     * Gets the unit price in pence of the stock item.
     *
     * @return The unit price in pence.
     */
    public int getUnitPricePence() {
        return unitPricePence;
    }

    /**
     * Gets the quantity of the stock item in stock.
     *
     * @return The quantity in stock.
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Sets the quantity of the stock item in stock.
     *
     * @param newQuantity The new quantity value.
     */
    public void setQuantityInStock(int newQuantity) {
        if (newQuantity >= 0) {
            quantityInStock = newQuantity;
        }
    }

    /**
     * Generates a formatted string representation of the stock item.
     *
     * @return The formatted string.
     */
    @Override
    public String toString() {
        return String.format("%s-%s - %s - UNIT PRICE: £%s - QTY: %d", getProductCode(), getProductTitle(),
                getProductDescription(), (getUnitPricePounds() + "." + getUnitPricePence()), getQuantityInStock());
    }

    /**
     * Loads stock data from a CSV file.
     *
     * @return A list of ASCStockItem objects representing the loaded stock.
     */
    public static List<ASCStockItem> loadStock() {
        final List<ASCStockItem> loadedStock = new ArrayList<>();
        String csvFilePath = "src/main/resources/AshersSportsCollective.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // Extracting data from CSV
                String productCode = data[0];
                String productTitle = data[1];
                String productDescription = data[2];
                int unitPricePounds = Integer.parseInt(data[3]);
                int unitPricePence = Integer.parseInt(data[4]);
                int quantityInStock = Integer.parseInt(data[5]);

                // Creating ASCStockItem object and adding to the list
                ASCStockItem stockItem = new ASCStockItem(productCode, productTitle, productDescription,
                        unitPricePounds, unitPricePence, quantityInStock);
                loadedStock.add(stockItem);
            }
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error reading CSV file. Please check the file path.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return loadedStock;
    }
}
