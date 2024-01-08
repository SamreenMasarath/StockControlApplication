package com.stockcontrol.app;

/**
 * Interface for observers interested in stock level updates.
 */
public interface ASCStockObserver {

    /**
     * Called to notify the observer about an updated stock item.
     *
     * @param stockItem The updated stock item.
     */
    void updateStockLevel(ASCStockItem stockItem);
}
