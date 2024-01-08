package com.stockcontrol.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for ASCStockSubject.
 */
public class ASCStockSubjectTest {

    private ASCStockSubject stockSubject;
    private ASCStockObserver observer1;
    private ASCStockObserver observer2;

    @BeforeEach
    public void setUp() {
        // Initialize an instance of ASCStockSubject and mock observers before each test
        stockSubject = ASCStockSubject.getInstance();
        observer1 = new MockObserver();
        observer2 = new MockObserver();
    }

    @Test
    public void testAddObserver() {
        // Test adding an observer to the subject
        System.out.println("addObserver");
        stockSubject.addObserver(observer1);
        assertTrue(stockSubject.getObservers().contains(observer1));
    }

    @Test
    public void testRemoveObserver() {
        // Test removing an observer from the subject
        System.out.println("removeObserver");
        stockSubject.addObserver(observer1);
        stockSubject.addObserver(observer2);

        stockSubject.removeObserver(observer1);
        assertTrue(stockSubject.getObservers().contains(observer2));
        assertEquals(4, stockSubject.getObservers().size());
    }

    @Test
    public void testNotifyObservers() {
        // Test notifying observers about an update
        System.out.println("notifyObservers");
        stockSubject.addObserver(observer1);
        stockSubject.addObserver(observer2);

        // Create an updated item and notify observers
        ASCStockItem updatedItem = new ASCStockItem("123", "TestProduct", "Test Description", 10, 99, 50);
        stockSubject.notifyObservers(updatedItem);

        assertTrue(((MockObserver) observer1).wasUpdated);
        assertTrue(((MockObserver) observer2).wasUpdated);
    }

    private static class MockObserver implements ASCStockObserver {

        // A mock observer implementation for testing purposes
        private boolean wasUpdated = false;

        @Override
        public void updateStockLevel(ASCStockItem updatedItem) {
            wasUpdated = true;
        }
    }
}
