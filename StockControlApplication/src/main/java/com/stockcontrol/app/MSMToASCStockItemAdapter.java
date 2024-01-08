package com.stockcontrol.app;

/**
 * Adapter class to convert an MSMStockItem to an ASCStockItem.
 */
public class MSMToASCStockItemAdapter extends ASCStockItem {

    private final MSMStockItem msmStockItem;

    /**
     * Constructor for the adapter.
     *
     * @param msmStockItem The MSMStockItem to be adapted.
     */
    public MSMToASCStockItemAdapter(MSMStockItem msmStockItem) {
        super(msmStockItem.getCode(), msmStockItem.getName(), msmStockItem.getDescription(),
                msmStockItem.getUnitPrice() / 100, msmStockItem.getUnitPrice() % 100,
                msmStockItem.getQuantityInStock());
        // Save a reference to the original MSMStockItem
        this.msmStockItem = msmStockItem;
    }

    /**
     * Overrides the getProductCode method to adjust the code format.
     *
     * @return The adapted product code.
     */
    @Override
    public String getProductCode() {
        int id = msmStockItem.getDepartmentId();
        String code = msmStockItem.getCode();
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

            default:
                break;
        }

        // Adjusted format for ASC system
        return dept + "-" + code + "-MSM";
    }

}
