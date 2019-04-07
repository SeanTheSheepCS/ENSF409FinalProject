package client.orderinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.orderinfopane.view.OrderInfoPaneGUI;

/**
 * A listener that waits for the backward arrow on the OrderInfoPaneGUI to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 7th 2019
 */
public class BackwardArrowButtonListener implements ActionListener 
{
    /** the orderInfoPaneGUI that the backward arrow button belongs to */
    OrderInfoPaneGUI parent;

    /**
     * Generates a backward arrow button listener with the specified parent
     * 
     * @param parent the OrderInfoPaneGUI that owns the backward arrow button
     */
    public BackwardArrowButtonListener(OrderInfoPaneGUI parent)
    {
        this.parent = parent;
    }
    
    /**
     * Triggers when the backward arrow button on the parent was pressed, asks the parent to go back one order
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        parent.goBackOneOrder();
    }
    
}
