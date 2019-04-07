package common.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * An orderline for orders in the tool shop application
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since February 4th 2019
 *
 */
public class OrderLine implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** the item the order line is to order */
    private Item itemToOrder;
    /** the quantity of that item to order */
    private int quantityToOrder;
    private boolean needsOrder;
	private String supplier;
	private String itemName;
	private Date date;
    /**
     * generates an order line for a certain quantity of the given item
     * @param itemToOrder the item to order
     * @param quantityToOrder the quantity to order
     */
    public OrderLine(Item itemToOrder, int quantityToOrder)
    {
        this.itemToOrder = itemToOrder;
        this.quantityToOrder = quantityToOrder;
    }
    public OrderLine(int parseInt, int quantitySold, int currentQuantity) {
		if(currentQuantity - quantitySold < 40) {
			setNeedsOrder(true);
		}else {
			setNeedsOrder(false);
		}
		
	}
	public OrderLine(Date date, String itemName, int quantity, String supplier) {
		this.date = date;
		this.itemName=itemName;
		this.quantityToOrder = quantity;
		this.supplier = supplier;
	}
	public Item getItem() {return itemToOrder;}
    /**
     * returns a string representation of the order line
     * 
     * @return a string representation of the order line
     */
    public String toString()
    {
        String result = "";
        result = result.concat("Date: " + date.toString() + "\n");
        result = result.concat("Item Description: " + itemName + "\n");
        result = result.concat("Amount ordered:   " + quantityToOrder + "\n");
        result = result.concat("Supplier:         " + supplier + "\n");
        return result;
    }
	public int getQuantity() {
		return quantityToOrder;
	}
	public boolean needsOrder() {
		return needsOrder;
	}
	public void setNeedsOrder(boolean needsOrder) {
		this.needsOrder = needsOrder;
	}
}