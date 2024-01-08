package com.stockcontrol.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for ASCStockItem.
 */
public class ASCStockItemTest {

    private ASCStockItem instance;

    @BeforeEach
    public void setUp() {
        // Initialize an instance of ASCStockItem for testing
        instance = new ASCStockItem("123", "TestProduct", "Test Description", 10, 99, 50);
    }

    @Test
    public void testGetProductCode() {
        System.out.println("getProductCode");
        String expResult = "123";
        String result = instance.getProductCode();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetProductTitle() {
        System.out.println("getProductTitle");
        String expResult = "TestProduct";
        String result = instance.getProductTitle();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetProductDescription() {
        System.out.println("getProductDescription");
        String expResult = "Test Description";
        String result = instance.getProductDescription();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetUnitPricePounds() {
        System.out.println("getUnitPricePounds");
        int expResult = 10;
        int result = instance.getUnitPricePounds();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetUnitPricePence() {
        System.out.println("getUnitPricePence");
        int expResult = 99;
        int result = instance.getUnitPricePence();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetQuantityInStock() {
        System.out.println("getQuantityInStock");
        int expResult = 50;
        int result = instance.getQuantityInStock();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetQuantityInStock() {
        System.out.println("setQuantityInStock");
        int newQuantity = 0;
        instance.setQuantityInStock(newQuantity);
        assertEquals(newQuantity, instance.getQuantityInStock());
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        String expResult = "123-TestProduct - Test Description - UNIT PRICE: £10.99 - QTY: 50";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}
