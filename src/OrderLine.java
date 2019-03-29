/**
 * An orderline for orders in the tool shop application
 * 
 * @author Sean Kenny
 * @version 1.0
 * @since February 4th 2019
 *
 */
public class OrderLine 
{
    /** the item the order line is to order */
    private Item itemToOrder;
    /** the quantity of that item to order */
    private int quantityToOrder;
    
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
    
    /**
     * returns a string representation of the order line
     * 
     * @return a string representation of the order line
     */
    public String toString()
    {
        String result = "";
        result = result.concat("Item Description: " + itemToOrder.getToolName() + "\n");
        result = result.concat("Amount ordered:   " + quantityToOrder + "\n");
        result = result.concat("Supplier:         " + itemToOrder.getSupplier().getCompanyName() + "\n");
        return result;
    }
}
