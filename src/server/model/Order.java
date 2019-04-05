package server.model;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * An order the tool shop application writes
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since February 4th 2019
 *
 */
public class Order 
{
    /** the list of order lines that make up the order */
    private ArrayList<OrderLine> orderLineList;
    /** th edate the order was placed */
    private LocalDate dateOrdered;
    
    /**
     * generates an empty order
     */
    public Order()
    {
        orderLineList = new ArrayList<OrderLine>();
        dateOrdered = LocalDate.now();
    }
    
    /**
     * adds an order line to the order
     * 
     * @param orderLineToAdd the order line to add
     */
    public void addOrderLine(OrderLine orderLineToAdd)
    {
        orderLineList.add(orderLineToAdd);
    }
    
    /**
     * returns the order lines of the order
     * 
     * @return the order lines that make up the order
     */
    public ArrayList<OrderLine> getOrderLines()
    {
        return orderLineList;
    }
    
    /**
     * returns the date
     * 
     * @return the date
     */
    public LocalDate getDate()
    {
        return dateOrdered;
    }
    
    /**
     * combines two orders, placing the order lines of orderToCombine in this order
     * 
     * @param orderToCombine the order to combine with this one
     */
    public void combine(Order orderToCombine)
    {
        for(OrderLine orderLineToAdd : orderToCombine.getOrderLines())
        {
            orderLineList.add(orderLineToAdd);
        }
    }
    
    /**
     * returns a string representation of the order
     * 
     * @return a string representation of the order
     */
    public String toString()
    {
        String result = "";
        result = result.concat("****************************************\n");
        result = result.concat("Date Ordered: " + dateOrdered + "\n");
        result = result.concat("\n");
        for(OrderLine currentOrderLine : orderLineList)
        {
            result = result.concat(currentOrderLine.toString() + "\n");
        }
        result = result.concat("****************************************\n");
        return result;
    }
}
