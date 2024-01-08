package com.stockcontrol.app;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Manages the stock and sales of items in the Asher's Sports Collective
 * inventory system.
 */
public class ASCStockManager {

    private List<ASCStockItem> stockItems;
    private DefaultTableModel stockTableModel, salesTableModel;
    static final int LOW_STOCK_THRESHOLD = 5;

    /**
     * Constructs an ASCStockManager, loading stock items and initializing table
     * models.
     */
    public ASCStockManager() {
        // Load stock items from ASC and Mengda's Sports Mart (MSM)
        List<ASCStockItem> ascStockItems = ASCStockItem.loadStock();
        List<MSMStockItem> mengdaStockItems = MSMStockItem.loadStock();

        // Merge stock items from ASC and MSM, ensuring no duplicates
        for (MSMStockItem mengdaStockItem : mengdaStockItems) {
            boolean alreadyAdded = ascStockItems.stream()
                    .anyMatch(item -> item.getProductCode().equals(getASCProductCode(mengdaStockItem)));

            if (!alreadyAdded) {
                ASCStockItem ascStockItem = new MSMToASCStockItemAdapter(mengdaStockItem);
                ascStockItems.add(ascStockItem);
            }
        }

        stockItems = ascStockItems;
        initializeTableModel();
    }

    // Helper method to generate ASC product code from MSMStockItem
    private String getASCProductCode(MSMStockItem mengdaStockItem) {
        int id = mengdaStockItem.getDepartmentId();
        String code = mengdaStockItem.getCode();
        String dept = null;
        switch (id) {
            case 1:
                dept = "RUN";
                break;
            case 2:
                dept = "SWM";
                break;
            case 3:
                dept = "CYC";
                break;
        }
        return dept + "-" + code + "-MSM";
    }

    /**
     * Gets the list of stock items.
     *
     * @return The list of stock items.
     */
    public List<ASCStockItem> getStockItems() {
        return stockItems;
    }

    /**
     * Gets the table model for stock items.
     *
     * @return The stock table model.
     */
    public DefaultTableModel getStockTableModel() {
        return stockTableModel;
    }

    /**
     * Gets the table model for sales items.
     *
     * @return The sales table model.
     */
    public DefaultTableModel getSalesTableModel() {
        return salesTableModel;
    }

    /**
     * Gets the low stock threshold.
     *
     * @return The low stock threshold.
     */
    public static int getLowStockThreshold() {
        return LOW_STOCK_THRESHOLD;
    }

    /**
     * Updates the stock level and shows a warning if the stock is low.
     *
     * @param updatedItem The stock item being updated.
     */
    public void updateStockLevel(ASCStockItem updatedItem) {
        if (updatedItem.getQuantityInStock() < LOW_STOCK_THRESHOLD) {
            showLowStockWarning(updatedItem);
        }
    }

    // Helper method to show a low stock warning
    private void showLowStockWarning(ASCStockItem item) {
        JOptionPane.showMessageDialog(null,
                "Low stock warning for: " + item.getProductCode() + " QTY: " + item.getQuantityInStock(),
                "Low Stock Warning", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Buys new stock and notifies observers.
     *
     * @param newItem The new stock item.
     */
    public void buyStock(ASCStockItem newItem) {
        stockItems.add(newItem);
        ASCStockSubject.getInstance().notifyObservers(newItem);
    }

    /**
     * Sells stock, updates quantity, and records the sales transaction.
     *
     * @param productCode The product code of the item being sold.
     * @param quantitySold The quantity sold.
     */
    public void sellStock(String productCode, int quantitySold) {
        for (ASCStockItem item : stockItems) {
            if (item.getProductCode().equals(productCode)) {
                item.setQuantityInStock(item.getQuantityInStock() - quantitySold);
                ASCStockSubject.getInstance().notifyObservers(item);
                recordSalesTransaction(item, quantitySold);
                break;
            }
        }
    }

    // Helper method to record sales transaction
    private void recordSalesTransaction(ASCStockItem item, int quantitySold) {
        String timestamp = getCurrentDateTime();
        String salesFilePath = "src/main/resources/SalesTransactions_" + timestamp.replaceAll("[\\s:-]", "") + ".csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(salesFilePath, true))) {
            writer.println(timestamp + "," + item.getProductCode() + "," + quantitySold + ","
                    + item.getUnitPricePounds() + "," + +item.getUnitPricePence());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to sales transactions file. Please check the file path.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the current stock to a CSV file.
     */
    /**
     * Saves the current stock to a CSV file.
     */
    public void saveStockToCSV() {
        String csvFilePath = "src/main/resources/AshersSportsCollective.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath))) {
            for (ASCStockItem item : stockItems) {
                writer.println(item.getProductCode() + "," + item.getProductTitle() + "," + item.getProductDescription()
                        + "," + item.getUnitPricePounds() + "," + item.getUnitPricePence() + ","
                        + item.getQuantityInStock());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to CSV file. Please check the file path.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to initialize table models
    private void initializeTableModel() {
        String[] stockColumnNames
                = {"Product Code", "Product Title", "Product Description", "Unit Price (Pounds)", "Unit Price (ُPence)",
                    "Quantity in Stock"};

        stockTableModel = new DefaultTableModel(stockColumnNames, 0) {
            private static final long serialVersionUID = -1185274943993659109L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] salesColumnNames
                = {"Date and Time", "Product Code", "Quantity Sold", "Unit Price", "Total Price"};

        salesTableModel = new DefaultTableModel(salesColumnNames, 0) {
            private static final long serialVersionUID = 7935032288793891213L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // Helper method to update the stock table
    public void updateStockTable() {
        stockTableModel.setRowCount(0);

        for (ASCStockItem item : stockItems) {
            Object[] rowData
                    = {item.getProductCode(), item.getProductTitle(), item.getProductDescription(), item.getUnitPricePounds(),
                        item.getUnitPricePence(), item.getQuantityInStock()};
            stockTableModel.addRow(rowData);
        }
    }

    // Helper method to get the current date and time as a formatted string
    public void updateSalesTable() {
        salesTableModel.setRowCount(0);
        List<ASCSalesItem> salesItems = ASCSalesItem.loadSalesData();

        for (ASCSalesItem item : salesItems) {
            Object[] rowData
                    = {item.getDateTime(), item.getProductCode(), item.getQuantitySold(), item.getUnitPrice(),
                        item.getTotalPrice()};
            salesTableModel.addRow(rowData);
        }
    }

    // Helper method to show an error dialog with a specified message
    public String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }
}
