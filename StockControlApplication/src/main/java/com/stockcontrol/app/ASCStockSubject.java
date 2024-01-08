package com.stockcontrol.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject class responsible for managing and notifying observers about changes
 * in the stock.
 */
public class ASCStockSubject {

    private static ASCStockSubject instance;
    private List<ASCStockObserver> observers;

    // Private constructor to enforce singleton pattern
    private ASCStockSubject() {
        observers = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of ASCStockSubject.
     *
     * @return The ASCStockSubject instance.
     */
    public static ASCStockSubject getInstance() {
        if (instance == null) {
            instance = new ASCStockSubject();
        }
        return instance;
    }

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(ASCStockObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(ASCStockObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers about an updated stock item.
     *
     * @param updatedItem The updated stock item.
     */
    public void notifyObservers(ASCStockItem updatedItem) {
        for (ASCStockObserver observer : observers) {
            observer.updateStockLevel(updatedItem);
        }
    }

    /**
     * Gets a copy of the list of observers.
     *
     * @return A list of observers.
     */
    public List<ASCStockObserver> getObservers() {
        return new ArrayList<>(observers);
    }
}
