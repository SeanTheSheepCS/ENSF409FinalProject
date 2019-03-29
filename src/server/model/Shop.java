package server.model;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * The shop for the tool shop application
 * 
 * @author Sean Kenny
 * @version 1.0
 * @since February 4th 2019
 *
 */
public class Shop 
{
    /** the inventory of the shop */
    private Inventory inventory;
    /** the list containing all the orders the shop has placed */
    private ArrayList<Order> orderList;
    /** the list containing all of the suppliers the shop recognizes */
    private ArrayList<Supplier> supplierList;
    
    /**
     * builds a shop with a given inventory
     * 
     * @param inventory the inventory of the new shop
     * @param supplierList the supplier list to build the inventory with
     */
    public Shop(Inventory inventory, ArrayList<Supplier> supplierList)
    {
        this.inventory = inventory;
        this.orderList = new ArrayList<Order>();
        this.supplierList = supplierList;
    }
    
    /**
     * sets the supplier list.
     * 
     * @param supplierList the new list of suppliers
     */
    public void setSupplierList(ArrayList<Supplier> supplierList)
    {
        this.supplierList = supplierList;
    }
    
    /**
     * gets the supplier list.
     * 
     * @return the list of suppliers
     */
    public ArrayList<Supplier> getSupplierList()
    {
        return supplierList;
    }
    
    /**
     * returns the inventory
     * 
     * @return the inventory
     */
    public Inventory getInventory()
    {
        return inventory;
    }
    
    /**
     * searches for a tool by name and returns it
     * 
     * @param itemName the name of the desired item
     * @return the desired item
     */
    public Item searchByName(String itemName)
    {
        return inventory.searchByName(itemName);
    }
    
    /**
     * searches for a tool by ID and returns it
     * 
     * @param itemID the ID of the desired item
     * @return the desired item
     */
    public Item searchByID(int itemID)
    {
        return inventory.searchByID(itemID);
    }
    
    /**
     * removes a given amount of one item from the inventory.
     * 
     * @param itemToBuy the item whose quantity should be reduced
     * @param quantityToBuy the quantity of that item that should be bought
     */
    public void buy(Item itemToBuy, int quantityToBuy)
    {
        Order theOrder = inventory.buy(itemToBuy,quantityToBuy);
        if (theOrder != null)
        {
            boolean hasAdded = false;
            for(Order previousOrder : orderList)
            {
                //If the dates match, the two orders should be consolidated into one.
                if(previousOrder.getDate().equals(theOrder.getDate()))
                {
                    previousOrder.combine(theOrder);
                    hasAdded = true;
                }
            }
            if(hasAdded == false)
            {
                //If you got here, you did not have an order to combine with.
                orderList.add(theOrder);
            }
            writeOrders();
        }
    }
    
    /**
     * writes the current orders present in the orderlist.
     */
    public void writeOrders()
    {
        System.out.print("\nThe orders.txt file was updated with new orders\n");
        try
        {
            BufferedWriter ordersTextFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("orders.txt")));
            for( Order currentOrder : orderList)
            {
                ordersTextFile.write(currentOrder.toString(),0,currentOrder.toString().length());
            }
            ordersTextFile.close();
        }
        catch (Exception e)
        {
            System.out.println("An error occured while writing orders. The latest order has been saved but not written.");
        }
    }
}
