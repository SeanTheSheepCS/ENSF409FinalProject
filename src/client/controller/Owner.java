package client.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import client.orderinfopane.view.OrderInfoPaneGUI;
import client.view.OwnerGUI;
import common.model.Item;
import common.model.OrderLine;

/**
 * The owner class for the tool shop application
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 4th 2019
 *
 */
public final class Owner extends Client
{
    /**
     * creates an Owner with a given OwnerGUI
     * 
     * @param frame the OwnerGUI that this owner will use
     * @param cManager the CommunicationManager that will handle communication between the owner and the server
     * @param pControl the permission controller assigned to this owner
     */
    public Owner(OwnerGUI frame, CommunicationManager cManager, PermissionController pControl)
    {
        super(frame, cManager, pControl);
        frame.getTheGetAllOrdersButton().addActionListener(new GetOrdersButtonListener(this));
    }
    
    /**
     * Asks the server for all the order lines it has and creates an OrderInfoPaneGUI displaying all of the order lines
     */
    public void manageGetAllOrdersRequest()
    {
        ArrayList<OrderLine> orderList = getUpdatedOrders();
        if(orderList != null)
        {
            OrderInfoPaneGUI infoPane = new OrderInfoPaneGUI(this, orderList);
        }
    }
    
    /**
     * returns an updated list of orders from the server
     * 
     * @return an ArrayList of OrderLines containing the most recent orders
     */
    public ArrayList<OrderLine> getUpdatedOrders()
    {
        try
        {
            super.getComsManager().sendMessage("GETORDERS");
            return super.getComsManager().readOrderLines();
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(super.getFrame(), "Please connect to the server before trying to get orders...");
            return null;
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(super.getFrame(), "Please connect to the server before trying to get orders...");
            return null;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(super.getFrame(), "An unexpected error occurred while trying to get all orders!");
            return null;
        }
    }
    
    /**
     * requests the detailed info of an item with a specified ID
     * 
     * @param specifiedID the ID of the item we want the information of
     * @return a String representation of the item with a detailed description
     */
    public String requestItemInfo(int specifiedID)
    {
        try
        {
            Item desiredItem = super.comsManager.readItemInfo(specifiedID);
            if(desiredItem != null)
            {
                return desiredItem.toString();
            }
            else
            {
                JOptionPane.showMessageDialog(super.getFrame(), "A very severe error has occurred, please end the application.");
                return "A very severe error has occurred, please end the application.";
            }
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(super.getFrame(), "Please connect to the server before trying to get a tool...");
            return null;
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(super.getFrame(), "An error occurred while trying to get all items, please doublecheck that you are connected to the server!");
            ioe.printStackTrace();
            return null;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(super.getFrame(), "An unexpected error occurred while managing a request to get a tool!");
            return null;
        }
    }
}
