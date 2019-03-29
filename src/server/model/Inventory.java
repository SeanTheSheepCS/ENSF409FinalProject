package server.model;
import java.util.ArrayList;

/**
 * The inventory a tool shop application
 * 
 * @author Sean Kenny
 * @version 1.0
 * @since February 4th 2019
 *
 */
public class Inventory 
{
    /** a list containing all the tools in the inventory */
    private ArrayList<Item> toolList;
    
    /**
     * constructs an inventory with the given tool list.
     * 
     * @param toolList the tool list to supply the inventory with
     */
    public Inventory(ArrayList<Item> toolList)
    {
        this.toolList = toolList;
    }
    
    /**
     * searches for an item by the item's name and returns it, if no item is present it returns null.
     * 
     * @param itemName the name of the item
     * @return the item with that matching name, or null if none was found
     */
    public Item searchByName(String itemName)
    {
        for(Item currentItem : toolList)
        {
            if(currentItem.getToolName().equals(itemName))
            {
                return currentItem;
            }
        }
        return null;
    }
    
    /**
     * searches for an item by the item's ID and returns it, if no item is present it returns null.
     * 
     * @param itemID the name of the item
     * @return the item with that matching ID, or null if none was found
     */
    public Item searchByID(int itemID)
    {
        for(Item currentItem : toolList)
        {
            if(currentItem.getToolIDNumber() == itemID)
            {
                return currentItem;
            }
        }
        return null;
    }
    
    /**
     * removes quantity of a certain item and returns an order to order more of it if the quantity drops below 40, and returns null otherwise
     * 
     * @param itemToBuy the item whose quantity should be decreased
     * @param quantityToBuy the amount of the item that should be bought
     * @return an order for more of the item if more is needed, null otherwise
     */
    public Order buy(Item itemToBuy, int quantityToBuy)
    {
        if(itemToBuy.getQuantity() >= quantityToBuy)
        {
            itemToBuy.decreaseQuantity(quantityToBuy);
            if(itemToBuy.getQuantity() < 40)
            {
                Order theOrder = new Order();
                theOrder.addOrderLine(new OrderLine(itemToBuy, 50-itemToBuy.getQuantity()));
                return theOrder;
            }
            else
            {
                return null;
            }
        }
        else
        {
            System.out.println("The quantity is too low to remove that amount of items.");
            return null;
        }
    }
    
    /**
     * returns a string representation of the inventory.
     * 
     * @return a string representation of the inventory
     */
    public String toString()
    {
        String collection = "";
        for(Item currentItem : toolList)
        {
            collection = collection.concat(currentItem.toString() + "\n");
        }       
        return collection;
    }
}
