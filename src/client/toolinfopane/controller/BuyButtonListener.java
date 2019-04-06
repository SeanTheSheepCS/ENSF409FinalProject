package client.toolinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.controller.Client;
import client.toolinfopane.view.ToolInfoPaneGUI;

/**
 * A listener that waits for the buy button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
public class BuyButtonListener implements ActionListener
{
    /** the user that will buy the item*/
    Client user;
    /** the ToolPaneInfoGUI whose buy button this is monitoring */
    ToolInfoPaneGUI parent;
    
    /**
     * generates a BuyButtonListener with a given parent
     * 
     * @param parent the ToolPaneInfoGUI whose buy button this is monitoring
     */
    public BuyButtonListener(Client user, ToolInfoPaneGUI parent)
    {
        this.user = user;
        this.parent = parent;
    }

    /**
     * the buy button was pressed, should send command to server to reduce quantity
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String numberToBuyAsString = JOptionPane.showInputDialog(parent, "How many would you like to buy?");
        try
        {
            int quantityToBuy = Integer.parseInt(numberToBuyAsString);
            boolean wasSuccessful = user.manageDecreaseQuantityRequest(parent.getToolIDOfItemOnDisplay(), quantityToBuy);
            if(wasSuccessful)
            {
                JOptionPane.showMessageDialog(parent, "Successfully bought item! (Disclaimer: This is a school project, no actual items will be sent, if you want to actually send us money that is up to you)");
            }
            else
            {
                JOptionPane.showMessageDialog(parent, "Failed to buy the item. Try again later...");
            }
        }
        catch(NumberFormatException nfe)
        {
            JOptionPane.showMessageDialog(parent,"Please enter a valid integer.");
        }
    }
}