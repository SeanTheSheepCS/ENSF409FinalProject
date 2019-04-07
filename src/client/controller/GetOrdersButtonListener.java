package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A listener that waits for the getOrders button that the owner has to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 7th 2019
 */
public class GetOrdersButtonListener implements ActionListener
{
    /** the Owner that the orders will be displayed for */
    private Owner parent;
    
    /**
     * creates the getOrdersButtonListener with a specified owner
     * 
     * @param parent the owner of the getOrders button
     */
    public GetOrdersButtonListener(Owner parent)
    {
        this.parent = parent;
    }

    /**
     * Triggers when the get all items button was pressed
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        parent.manageGetAllOrdersRequest();    
    }

}
