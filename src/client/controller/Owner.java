package client.controller;

import java.io.IOException;

import javax.swing.JOptionPane;

import client.view.OwnerGUI;
import common.model.Item;

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
     */
    public Owner(OwnerGUI frame)
    {
        super(frame);
    }
    
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
            JOptionPane.showMessageDialog(super.getFrame(), "An IOException occurred while managing a request to get a tool!");
            return null;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(super.getFrame(), "An unexpected error occurred while managing a request to get a tool!");
            return null;
        }
    }
}
