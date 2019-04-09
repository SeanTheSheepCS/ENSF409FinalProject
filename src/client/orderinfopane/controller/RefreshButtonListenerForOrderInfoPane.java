package client.orderinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.controller.Owner;
import client.orderinfopane.view.OrderInfoPaneGUI;

/**
 * A listener that waits for the refresh button to be pressed and then gets a new set of orders for it's OrderInfoParentGUI
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 9th 2019
 */
public class RefreshButtonListenerForOrderInfoPane implements ActionListener
{
    /** the GUI that contains the refresh button and wants new data */
    private OrderInfoPaneGUI parent;
    /** the owner we will ask for new data */
    private Owner ownerToAsk;
    
    /**
     * Creates a listener that knows which GUI the refresh button belongs to and who to ask for new data
     * 
     * @param ownerToAsk who to ask for new data if the refresh button is pressed
     * @param parent where to display new data if the refresh button is pressed
     */
    public RefreshButtonListenerForOrderInfoPane(Owner ownerToAsk, OrderInfoPaneGUI parent)
    {
        this.ownerToAsk = ownerToAsk;
        this.parent = parent;
    }
    
    /**
     * The refresh button was pressed so the owner should be asked for new data and it should be displayed on the OrderPaneInfoGUI
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        parent.refresh(ownerToAsk.getUpdatedOrders());
    }

}
