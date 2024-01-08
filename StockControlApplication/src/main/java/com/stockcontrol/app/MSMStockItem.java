package com.stockcontrol.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Represents a Mengda's Sportymart stock item.
 * 
 * @author Steven Mead
 * @version 1.0
 * @since 1.0
 */
public class MSMStockItem
{
	private final int departmentId;
	private final String code;
	private final String nameAndDescription;
	private final int unitPrice;
	private int quantityInStock;

	/**
	 * Constructor
	 * 
	 * @param departmentId
	 * @param code
	 * @param titleAndDescription
	 * @param unitPrice
	 * @param quantityInStock
	 * @since 1.0
	 */
	public MSMStockItem(int departmentId, String code, String titleAndDescription, int unitPrice, int quantityInStock)
	{
		this.departmentId = departmentId;
		this.code = code;
		this.nameAndDescription = titleAndDescription;
		this.unitPrice = unitPrice;
		this.quantityInStock = quantityInStock;
	}

	/**
	 * Returns the department ID used by Mead's Modernity
	 * 
	 * @return the department id.
	 * @since 1.0
	 */
	public int getDepartmentId()
	{
		return departmentId;
	}

	/**
	 * Returns the code of this stock item.
	 * 
	 * @return a string with the 6-digit code.
	 * @since 1.0
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Returns the name of this stock item.
	 * 
	 * @return a string with the name.
	 * @since 1.0
	 */
	public String getName()
	{
		return nameAndDescription.substring(0, 59).replaceAll("\u00a0", "").stripTrailing();
	}

	/**
	 * Return the description of this stock item.
	 * 
	 * @return a string with the description.
	 * @since 1.0
	 */
	public String getDescription()
	{
		return nameAndDescription.substring(60, nameAndDescription.length());
	}

	/**
	 * Returns the stock item's unit price in pence
	 * 
	 * @return an integer that represents the unit price.
	 * @since 1.0
	 */
	public int getUnitPrice()
	{
		return unitPrice;
	}

	/**
	 * Return the quantity of this stock item currently available in stock.
	 * 
	 * @return an integer that represents to stock quantity available.
	 * @since 1.0
	 */
	public int getQuantityInStock()
	{
		return quantityInStock;
	}

	public String getHumanFriendlyUnitPrice()
	{

		final int pounds = getUnitPrice() / 100;
		final int pence = getUnitPrice() % 100;
		return String.format("%d.%02d", pounds, pence);
	}

	public void setQuanity(int newQuantity)
	{
		if (newQuantity >= 0)
		{
			quantityInStock = newQuantity;
		}
	}

	@Override
	public String toString()
	{
		return String.format("%d-%s - %s - %s - UNIT PRICE: £%s - QTY: %d", getDepartmentId(), getCode(), getName(),
				getDescription(), getHumanFriendlyUnitPrice(), getQuantityInStock());
	}

	/**
	 * Load a collection of MSMStockItems into a list
	 * 
	 * @return a list of the Mengda's Sportymart stock items.
	 * @since 1.0
	 */
	public static List<MSMStockItem> loadStock()
	{
		final List<MSMStockItem> loadedStock = new ArrayList<>();

		String csvFilePath = "src/main/resources/MengdasSportyMart.csv";

		try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath,StandardCharsets.UTF_8)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] data = line.split(",");

				// Extracting data from CSV
				int productDepartmentID = Integer.parseInt(data[0].replaceAll("\\D", ""));
				String productCode = data[1];
				String productNameAndDescription = data[2];
				int unitPrice = Integer.parseInt(data[3]);
				int quantityInStock = Integer.parseInt(data[4]);

				// Creating MSMStockItem object and adding to the list
				MSMStockItem stockItem = new MSMStockItem(productDepartmentID, productCode,
						productNameAndDescription, unitPrice, quantityInStock);
				loadedStock.add(stockItem);
			}
		}
		catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "Error reading CSV file. Please check the file path.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return loadedStock;
	}

}
