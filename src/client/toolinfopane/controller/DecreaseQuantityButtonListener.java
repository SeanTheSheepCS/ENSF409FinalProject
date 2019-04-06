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
public class DecreaseQuantityButtonListener implements ActionListener
{
    /** the user that will buy the item*/
    Client user;
    /** the ToolInfoPaneGUI that the decrease quantity button belongs to */
    ToolInfoPaneGUI parent;
    
    public DecreaseQuantityButtonListener(Client user, ToolInfoPaneGUI parent)
    {
        this.user = user;
        this.parent = parent;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String numberToBuyAsString = JOptionPane.showInputDialog(parent, "How much would you like to decrease the quantity by?");
        try
        {
            int quantityToBuy = Integer.parseInt(numberToBuyAsString);
            boolean wasSuccessful = user.manageDecreaseQuantityRequest(parent.getToolIDOfItemOnDisplay(), quantityToBuy);
            if(wasSuccessful)
            {
                JOptionPane.showMessageDialog(parent, "Successfully decreased the item's quantity!");
            }
            else
            {
                JOptionPane.showMessageDialog(parent, "Failed to decrease the items's quantity");
            }
        }
        catch(NumberFormatException nfe)
        {
            JOptionPane.showMessageDialog(parent,"Please enter a valid integer.");
        }
    }
}
