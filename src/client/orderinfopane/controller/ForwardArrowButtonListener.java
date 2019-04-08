package client.orderinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.orderinfopane.view.OrderInfoPaneGUI;

/**
 * A listener that waits for the forward arrow on the OrderInfoPaneGUI to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 7th 2019
 */
public final class ForwardArrowButtonListener implements ActionListener 
{
    /** the orderInfoPaneGUI that the forward arrow button belongs to */
    OrderInfoPaneGUI parent;

    /**
     * Generates a forward arrow button listener with the specified parent
     * 
     * @param parent the OrderInfoPaneGUI that owns the forward arrow button
     */
    public ForwardArrowButtonListener(OrderInfoPaneGUI parent)
    {
        this.parent = parent;
    }
    
    /**
     * Triggers when the forward arrow button on the parent was pressed, asks the parent to go forward one order
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        parent.advanceOneOrder();
    }
    
}
