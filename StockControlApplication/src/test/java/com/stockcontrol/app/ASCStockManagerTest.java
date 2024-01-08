package com.stockcontrol.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for ASCStockManager.
 */
public class ASCStockManagerTest {

    private ASCStockManager stockManager;

    @BeforeEach
    void setUp() {
        // Initialize an instance of ASCStockManager before each test
        stockManager = new ASCStockManager();
        stockManager.getStockItems().clear();
    }

    @Test
    void buyStock() {
        int initialSize = stockManager.getStockItems().size();
        ASCStockItem newItem = createTestStockItem();
        stockManager.buyStock(newItem);
        int newSize = stockManager.getStockItems().size();

        assertEquals(initialSize + 1, newSize, "Stock size should increase after buying stock");
    }

    @Test
    void sellStock() {
        // Buy an initial stock item
        ASCStockItem newItem = createTestStockItem();
        stockManager.buyStock(newItem);

        int initialQuantity = stockManager.getStockItems().get(0).getQuantityInStock();
        String productCode = newItem.getProductCode();

        // Sell a quantity of stock
        stockManager.sellStock(productCode, 5);

        // Retrieve the updated item after selling
        ASCStockItem updatedItem = stockManager.getStockItems().stream()
                .filter(item -> item.getProductCode().equals(productCode)).findFirst().orElse(null);

        assertNotNull(updatedItem, "Item should not be null");
        assertEquals(initialQuantity - 5, updatedItem.getQuantityInStock(),
                "Stock quantity should decrease after selling stock");
    }

    private ASCStockItem createTestStockItem() {
        // Helper method to create a test stock item
        return new ASCStockItem("SWM4564523", "Test Product", "Test Description", 10, 99, 20);
    }
}
