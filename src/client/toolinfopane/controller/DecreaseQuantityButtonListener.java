package client.toolinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.controller.Client;
import client.toolinfopane.view.ToolInfoPaneGUI;

/**
 * A listener that waits for the decrease quantity button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 6th 2019
 */
public final class DecreaseQuantityButtonListener implements ActionListener
{
    /** the user that will buy the item*/
    Client user;
    /** the ToolInfoPaneGUI that the decrease quantity button belongs to */
    ToolInfoPaneGUI parent;
    
    /**
     * Generates a DecreaseQuantityButtonListener with a given parent
     * 
     * @param user the user who will send the message to decrease quantity to the server
     * @param parent the ToolPaneInfoGUI whose decrease quantity button this is monitoring
     */
    public DecreaseQuantityButtonListener(Client user, ToolInfoPaneGUI parent)
    {
        this.user = user;
        this.parent = parent;
    }
    
    /**
     * The decrease quantity button was pressed, should send command to server to reduce quantity
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String numberToBuyAsString = JOptionPane.showInputDialog(parent, "How much would you like to decrease the quantity by?");
        try
        {
            int quantityToBuy = Integer.parseInt(numberToBuyAsString);
            if(quantityToBuy <= 0)
            {
                JOptionPane.showMessageDialog(parent, "Please enter an integer larger than zero.");
            }
            else
            {
                boolean wasSuccessful = user.manageDecreaseQuantityRequest(parent.getToolIDOfItemOnDisplay(), quantityToBuy);
                if(wasSuccessful)
                {
                    JOptionPane.showMessageDialog(parent, "Successfully decreased the item's quantity!");
                    parent.setUpdatedToolInfo(user.requestItemInfo(parent.getToolIDOfItemOnDisplay()));
                }
                else
                {
                    JOptionPane.showMessageDialog(parent, "Failed to decrease the items's quantity");
                }
            }
        }
        catch(NumberFormatException nfe)
        {
            JOptionPane.showMessageDialog(parent,"Please enter a valid integer.");
        }
    }
}
