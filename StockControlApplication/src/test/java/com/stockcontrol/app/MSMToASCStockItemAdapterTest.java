package com.stockcontrol.app;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * JUnit test class for MSMToASCStockItemAdapter.
 */
public class MSMToASCStockItemAdapterTest {

    /**
     * Test the MSMToASCStockItemAdapter by adapting an MSMStockItem.
     */
    @Test
    public void testAdapter() {
        MSMStockItem msmStockItem = new MSMStockItem(1, "234567",
                "RunEverywhere                                               Great trainers for running cross country",
                8850, 50);

        MSMToASCStockItemAdapter adapter = new MSMToASCStockItemAdapter(msmStockItem);

        // Test adapter methods
        assertEquals("RUN-234567-MSM", adapter.getProductCode());
        assertEquals("RunEverywhere", adapter.getProductTitle());
        assertEquals("Great trainers for running cross country", adapter.getProductDescription());
        assertEquals(88, adapter.getUnitPricePounds());
        assertEquals(50, adapter.getUnitPricePence());
        assertEquals(50, adapter.getQuantityInStock());

        // Test inherited methods
        assertEquals(
                "RUN-234567-MSM-RunEverywhere - Great trainers for running cross country - UNIT PRICE: £88.50 - QTY: 50",
                adapter.toString());
    }
}
