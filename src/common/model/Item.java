package common.model;

import java.io.Serializable;

/**
 * One item of the tool shop application
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since February 4th 2019
 *
 */
public final class Item implements Serializable
{

	/** The name of the tool */
    private String toolName;
    /** the ID number of the tool */
    private int toolIDNumber;
    /** the price of the tool */
    private double price;
    /** the supplier of the tool */
    private Supplier supplier;
    /** the quantity of the tool in stock */
    private int quantity;
    
    /**
     * builds an item with default parameters
     */
    public Item()
    {
        toolName = "NO_NAME_PROVIDED";
        toolIDNumber = -1;
        price = -1;
        supplier = null;
        quantity = 0;
    }
    
    public Item(int itemID, String itemName, int itemQuantity, double itemPrice, int itemSupplierID) {
    	toolName = itemName;
        toolIDNumber = itemID;
        price = itemPrice;
        supplier = new Supplier();
        supplier.setSupplierID(itemSupplierID);
        quantity = itemQuantity;
	}

	/**
     * sets the name of the tool
     * 
     * @param toolName the new name of the tool
     */
    public void setToolName(String toolName)
    {
        this.toolName = toolName;
    }
    
    /**
     * sets the ID of the tool
     * 
     * @param toolIDNumber the new ID number of the tool
     */
    public void setToolIDNumber(int toolIDNumber)
    {
        this.toolIDNumber = toolIDNumber;
    }
    
    /**
     * sets the price of the tool
     * 
     * @param price the new price of the tool
     */
    public void setPrice(double price)
    {
        this.price = price;
    }
    
    /**
     * sets the supplier of the tool
     * 
     * @param supplier the new supplier of the tool
     */
    public void setSupplier(Supplier supplier)
    {
        this.supplier = supplier;
    }
    
    /**
     * sets the quantity of the tool
     * 
     * @param quantity the new quantity of the tool
     */
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    
    /**
     * returns the name of the tool
     * 
     * @return the name of the tool
     */
    public String getToolName()
    {
        return toolName;
    }
    
    /**
     * returns the ID of the tool
     * 
     * @return the ID of the tool
     */
    public int getToolIDNumber()
    {
        return toolIDNumber;
    }
    
    /**
     * returns the price of the tool
     * 
     * @return the price of the tool
     */
    public double getPrice()
    {
        return price;
    }
    
    /**
     * returns the supplier of the tool
     * 
     * @return the supplier of the tool
     */
    public Supplier getSupplier()
    {
        if(supplier != null)
        {
            return supplier;
        }
        else
        {
            Supplier defaultSupplier = new Supplier();
            return defaultSupplier;
        }
    }
    
    /**
     * returns the quantity of the tool
     * 
     * @return the quantity of the tool
     */
    public int getQuantity()
    {
        return quantity;
    }
    
    /**
     * decreases the quantity of the tool by a certain amount unless the quantity would go into the negative if this was done.
     * 
     * @param quantityToDecrease the number you want to reduce the quantity by
     */
    public void decreaseQuantity(int quantityToDecrease)
    {
        if(quantityToDecrease <= quantity)
        {
            quantity = quantity - quantityToDecrease;
        }
    }
    
    /**
     * returns a string representation of the item
     * 
     * @return a string representation of the item
     */
    public String toString()
    {
        String collection = "";
        collection = collection.concat("Tool Name:          " + toolName + "\n");
        
        if(toolIDNumber != -1)
        {
            collection = collection.concat("Tool ID:            " + toolIDNumber + "\n");
        }
        else
        {
            collection = collection.concat("Tool ID:            " + "NO_ID_NUMBER_PROVIDED" + "\n");
        }
        
        if(price != -1)
        {
            collection = collection.concat("Price:              " + "$" + price + "\n");
        }
        else
        {
            collection = collection.concat("Price:              " + "NO_PRICE_PROVIDED" + "\n");
        }
        
        if(supplier != null)
        {
            collection = collection.concat("Supplier:           " + supplier + "\n");
        }
        else
        {
            collection = collection.concat("Supplier:           " + "NO_SUPPLIER_PROVIDED" + "\n");
        }
        
        collection = collection.concat("Quantity:           " + quantity + "\n");
        
        return collection;
    }
}
