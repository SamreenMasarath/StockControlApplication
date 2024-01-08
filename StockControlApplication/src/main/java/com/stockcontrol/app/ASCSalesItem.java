package com.stockcontrol.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Represents a sales item with information such as date and time, product code,
 * quantity sold, unit price, and total price.
 */
public class ASCSalesItem {

    private String dateTime;
    private String productCode;
    private int quantitySold;
    private int unitPrice;
    private int totalPrice;

    /**
     * Constructor for ASCSalesItem class.
     *
     * @param dateTime The date and time of the sales item.
     * @param productCode The product code of the item sold.
     * @param quantitySold The quantity of the product sold.
     * @param unitPrice The unit price of the product.
     */
    public ASCSalesItem(String dateTime, String productCode, int quantitySold, int unitPrice) {
        this.dateTime = dateTime;
        this.productCode = productCode;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalPrice = this.quantitySold * this.unitPrice;
    }

    /**
     * Gets the date and time of the sales item.
     *
     * @return The date and time.
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Gets the product code of the sales item.
     *
     * @return The product code.
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Gets the quantity of the product sold.
     *
     * @return The quantity sold.
     */
    public int getQuantitySold() {
        return quantitySold;
    }

    /**
     * Gets the unit price of the product.
     *
     * @return The unit price.
     */
    public int getUnitPrice() {
        return unitPrice;
    }

    /**
     * Gets the total price of the sales item.
     *
     * @return The total price.
     */
    public int getTotalPrice() {
        return totalPrice;
    }

    /**
     * Loads sales data from the default directory.
     *
     * @return A list of ASCSalesItem objects.
     */
    public static List<ASCSalesItem> loadSalesData() {
        return loadSalesDataFromDirectory("src/main/resources/");
    }

    /**
     * Loads sales data from a specified directory.
     *
     * @param directoryPath The path of the directory containing sales data
     * files.
     * @return A list of ASCSalesItem objects.
     */
    private static List<ASCSalesItem> loadSalesDataFromDirectory(String directoryPath) {
        List<ASCSalesItem> salesItems = new ArrayList<>();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith("SalesTransactions_")) {
                    salesItems.addAll(loadSalesDataFromFile(file));
                }
            }
        }

        return salesItems;
    }

    /**
     * Loads sales data from a specified file.
     *
     * @param file The file containing sales data.
     * @return A list of ASCSalesItem objects.
     */
    private static List<ASCSalesItem> loadSalesDataFromFile(File file) {
        List<ASCSalesItem> salesItems = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                String dateTime = data[0];
                String productCode = data[1];
                int quantitySold = Integer.parseInt(data[2]);
                int unitPrice = Integer.parseInt(data[3]);

                ASCSalesItem salesItem = new ASCSalesItem(dateTime, productCode, quantitySold, unitPrice);
                salesItems.add(salesItem);
            }
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while loading sales data: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return salesItems;
    }
}
