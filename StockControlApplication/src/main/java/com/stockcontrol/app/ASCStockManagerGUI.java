package com.stockcontrol.app;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * GUI for the ASCStockManager, providing a user interface for stock control
 * operations.
 */
public class ASCStockManagerGUI extends JFrame implements ASCStockObserver {

    private static final long serialVersionUID = -7499419580391087152L;
    private ASCStockManager stockManager;
    private JTable stockTable;

    /**
     * GUI for the ASCStockManager, providing a user interface for stock control
     * operations.
     */
    public ASCStockManagerGUI() {
        stockManager = new ASCStockManager();

        setTitle("Stock Control with low stock reporting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);

        initializeComponents();

        ASCStockSubject.getInstance().addObserver(this);
    }

    /**
     * Updates the stock level in response to changes in the stock manager.
     *
     * @param updatedItem The updated stock item.
     */
    @Override
    public void updateStockLevel(ASCStockItem updatedItem) {
        stockManager.updateStockLevel(updatedItem);
    }

    /**
     * Updates the stock level in response to changes in the stock manager.
     *
     * @param updatedItem The updated stock item.
     */
    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        stockTable = createStockTable();
        mainPanel.add(new JScrollPane(stockTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton buyButton = new JButton("Buy Stock");
        JButton sellButton = new JButton("Sell Stock");
        JButton salesButton = new JButton("Sales");

        buyButton.addActionListener(e -> openBuyStockDialog());
        sellButton.addActionListener(e -> openSellStockDialog());
        salesButton.addActionListener(e -> openSalesDialog());

        buttonPanel.add(buyButton);
        buttonPanel.add(sellButton);
        buttonPanel.add(salesButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Opens the "Buy Stock" dialog for purchasing new stock items.
     */
    private void openBuyStockDialog() {
        // Dialog for buying stock
        JDialog buyDialog = new JDialog(this, "Buy Stock", true);
        buyDialog.setSize(400, 300);
        buyDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField codeField = new JTextField(20);
        buyDialog.add(new JLabel("Product Code:"), gbc);
        gbc.gridx = 1;
        buyDialog.add(codeField, gbc);

        JTextField titleField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        buyDialog.add(new JLabel("Product Title:"), gbc);
        gbc.gridx = 1;
        buyDialog.add(titleField, gbc);

        JTextField descriptionField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        buyDialog.add(new JLabel("Product Description:"), gbc);
        gbc.gridx = 1;
        buyDialog.add(descriptionField, gbc);

        JTextField poundsField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        buyDialog.add(new JLabel("Unit Price (Pounds):"), gbc);
        gbc.gridx = 1;
        buyDialog.add(poundsField, gbc);

        JTextField penceField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 4;
        buyDialog.add(new JLabel("Unit Price (Pence):"), gbc);
        gbc.gridx = 1;
        buyDialog.add(penceField, gbc);

        JTextField quantityField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 5;
        buyDialog.add(new JLabel("Quantity in Stock:"), gbc);
        gbc.gridx = 1;
        buyDialog.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JButton buyButton = new JButton("Buy Stock");
        buyDialog.add(buyButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        JButton cancelButton = new JButton("Cancel");
        buyDialog.add(cancelButton, gbc);

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String productCode = codeField.getText();
                    String productTitle = titleField.getText();
                    String productDescription = descriptionField.getText();
                    int unitPricePounds = Integer.parseInt(poundsField.getText());
                    int unitPricePence = Integer.parseInt(penceField.getText());
                    int quantityInStock = Integer.parseInt(quantityField.getText());

                    // Create and add the new stock item
                    ASCStockItem newItem = new ASCStockItem(productCode, productTitle, productDescription,
                            unitPricePounds, unitPricePence, quantityInStock);

                    stockManager.buyStock(newItem);

                    stockManager.updateStockTable();
                    stockManager.saveStockToCSV();

                    // Close the dialog
                    buyDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(buyDialog, "Please enter valid numeric values.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> buyDialog.dispose());

        buyDialog.setLocationRelativeTo(this);
        buyDialog.setVisible(true);
    }

    /**
     * Opens the "Sell Stock" dialog for selling existing stock items.
     */
    private void openSellStockDialog() {
        int selectedRow = stockTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to sell.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract product details from the selected row
        String productCode = (String) stockManager.getStockTableModel().getValueAt(selectedRow, 0);
        String productTitle = (String) stockManager.getStockTableModel().getValueAt(selectedRow, 1);
        String productDescription = (String) stockManager.getStockTableModel().getValueAt(selectedRow, 2);
        int unitPricePounds = (int) stockManager.getStockTableModel().getValueAt(selectedRow, 3);
        int unitPricePence = (int) stockManager.getStockTableModel().getValueAt(selectedRow, 4);
        int quantityInStock = (int) stockManager.getStockTableModel().getValueAt(selectedRow, 5);

        openSellStockDialog(productCode, productTitle, productDescription, unitPricePounds, unitPricePence,
                quantityInStock);
    }

    /**
     * Opens the "Sell Stock" dialog for selling existing stock items.
     */
    private void openSellStockDialog(String productCode, String productTitle, String productDescription,
            int unitPricePounds, int unitPricePence, int quantityInStock) {
        // Create and configure the "Sell Stock" dialog
        JDialog sellDialog = new JDialog(this, "Sell Stock", true);
        sellDialog.setSize(400, 300);
        sellDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField codeField = new JTextField(20);
        codeField.setEditable(false);
        sellDialog.add(new JLabel("Product Code:"), gbc);
        gbc.gridx = 1;
        sellDialog.add(codeField, gbc);

        JTextField titleField = new JTextField(20);
        titleField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        sellDialog.add(new JLabel("Product Title:"), gbc);
        gbc.gridx = 1;
        sellDialog.add(titleField, gbc);

        JTextField descriptionField = new JTextField(20);
        descriptionField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        sellDialog.add(new JLabel("Product Description:"), gbc);
        gbc.gridx = 1;
        sellDialog.add(descriptionField, gbc);

        JTextField poundsField = new JTextField(20);
        poundsField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        sellDialog.add(new JLabel("Unit Price (Pounds):"), gbc);
        gbc.gridx = 1;
        sellDialog.add(poundsField, gbc);

        JTextField penceField = new JTextField(20);
        penceField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        sellDialog.add(new JLabel("Unit Price (Pence):"), gbc);
        gbc.gridx = 1;
        sellDialog.add(penceField, gbc);

        JTextField quantityField = new JTextField(20);
        quantityField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 5;
        sellDialog.add(new JLabel("Quantity in Stock:"), gbc);
        gbc.gridx = 1;
        sellDialog.add(quantityField, gbc);

        JTextField quantityToBeSold = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 6;
        sellDialog.add(new JLabel("Quantity to be Sold:"), gbc);
        gbc.gridx = 1;
        sellDialog.add(quantityToBeSold, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        JButton sellButton = new JButton("Sell Stock");
        sellDialog.add(sellButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        JButton cancelButton = new JButton("Cancel");
        sellDialog.add(cancelButton, gbc);

        // Pre-fill details in the fields
        codeField.setText(productCode);
        titleField.setText(productTitle);
        descriptionField.setText(productDescription);
        poundsField.setText(String.valueOf(unitPricePounds));
        penceField.setText(String.valueOf(unitPricePence));
        quantityField.setText(String.valueOf(quantityInStock));

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int quantitySold = Integer.parseInt(quantityToBeSold.getText());

                    if (quantitySold <= 0 || quantitySold > quantityInStock) {
                        JOptionPane.showMessageDialog(sellDialog, "Invalid quantity to sell.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    stockManager.sellStock(productCode, quantitySold);
                    stockManager.updateStockTable();
                    stockManager.saveStockToCSV();

                    sellDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(sellDialog, "Please enter a valid numeric value for quantity.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> sellDialog.dispose());

        sellDialog.setLocationRelativeTo(this);
        sellDialog.setVisible(true);
    }

    /**
     * Opens the "Sales Information" dialog for displaying sales details.
     */
    private void openSalesDialog() {
        JDialog salesDialog = new JDialog(this, "Sales Information", true);
        salesDialog.setSize(600, 300);
        salesDialog.setLayout(new BorderLayout());

        // Create a table for displaying sales information
        JTable salesTable = createSalesTable();
        salesDialog.add(new JScrollPane(salesTable), BorderLayout.CENTER);

        // Create a "Close" button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> salesDialog.dispose());
        salesDialog.add(closeButton, BorderLayout.SOUTH);

        // Set the dialog properties and make it visible
        salesDialog.setLocationRelativeTo(this);
        salesDialog.setVisible(true);
    }

    /**
     * Creates and returns a JTable for displaying stock information.
     *
     * @return The stock JTable.
     */
    private JTable createStockTable() {
        JTable stockTable = new JTable(stockManager.getStockTableModel());
        stockManager.updateStockTable();
        return stockTable;
    }

    /**
     * Creates and returns a JTable for displaying sales information.
     *
     * @return The sales JTable.
     */
    private JTable createSalesTable() {
        JTable salesTable = new JTable(stockManager.getSalesTableModel());
        stockManager.updateSalesTable();
        return salesTable;
    }

    /**
     * The main method to launch the ASCStockManagerGUI.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ASCStockManagerGUI());
    }
}
