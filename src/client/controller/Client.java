package client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import common.model.Item;
import client.view.GUI;

/**
 * The Client class for the tool shop application
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since March 30th 2019
 *
 */
public class Client 
{
    private ArrayList<Integer> idsOfItemsOnDisplay;
    private CommunicationManager comsManager;
    private GUI theFrame;
    private PermissionController pControl;
    
    public Client()
    {
        try
        {
            idsOfItemsOnDisplay = new ArrayList<Integer>();
            theFrame = new GUI("Toolshop Application");
            pControl = new PermissionController(this,theFrame);
            comsManager = new CommunicationManager();
            prepareListeners();
        }
        catch(Exception e)
        {
            System.out.println("An unexpected error occurred while initializing the client.");
        }
    }
    
    public Client(GUI frame)
    {
        try
        {
            idsOfItemsOnDisplay = new ArrayList<Integer>();
            theFrame = frame;
            pControl = new PermissionController(this,theFrame);
            comsManager = new CommunicationManager();
            prepareListeners();
        }
        catch(Exception e)
        {
            System.out.println("An unexpected error occurred while initializing the client.");
        }
    }
    
    private void prepareListeners()
    {
        try
        {
            theFrame.getLoginButton().addActionListener(new LoginButtonListener(this,theFrame));
            theFrame.getSearchButton().addActionListener(new SearchButtonListener(this,theFrame));
            theFrame.getConnectButton().addActionListener(new ConnectButtonListener(this,theFrame));
            theFrame.getList().addListSelectionListener(new ListListener(this, theFrame, theFrame.getList()));
        }
        catch(NullPointerException npe)
        {
            System.out.println("Tried to prepare listeners before the GUI was prepared!");
        }
        catch(Exception e)
        {
            System.out.println("An unexpected exception occurred when preparing the listeners!");
        }
    }
    
    public void manageLoginRequest(String username, String password)
    {
        try
        {
            String loginValidation = comsManager.sendLoginMessageAndReturnServerOutput(username, password);
            if(loginValidation.equals("ADMIN"))
            {
                pControl.changePermissionToAdmin();
            }
            else if(loginValidation.equals("CUSTOMER"))
            {
                pControl.changePermissionToCustomer();
            }
            else
            {
                pControl.manageInvalidLogin();
            }
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(theFrame, "Please connect to the server before trying to validate a login...");
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(theFrame, "An IOException occurred while managing a login request!");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(theFrame, "An unexpected error occurred while managing a login request!");
        }
    }
    
    public void manageSearchRequest(String searchTerm)
    {
        try
        {
            clearEntries();
            comsManager.sendMessage("SEARCH" + " " + searchTerm);
            getSequenceOfItems();
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(theFrame, "Please connect to the server before trying to search...");
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(theFrame, "An IOException occurred while managing a search request!");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(theFrame, "An unexpected error occurred while managing a search request!");
        }
    }
    
    public void manageLogoutRequest()
    {
        try
        {
            pControl.changePermissionToGuest();
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(theFrame, "Please connect to the server before trying to logout...");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(theFrame, "An unexpected error occurred while managing a logout request!");
        }
    }
    
    public void manageGetAllToolsRequest()
    {
        try
        {
            comsManager.sendMessage("GETALLITEMS");
            getSequenceOfItems();
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(theFrame, "Please connect to the server before trying to get all tools...");
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(theFrame, "An IOException occurred while managing a request to get all tools!");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(theFrame, "An unexpected error occurred while managing a request to get all tools!");
        }
    }
    
    public String requestItemInfo(int specifiedID)
    {
        try
        {
            Item desiredItem = comsManager.readItemInfo(specifiedID);
            if(desiredItem != null)
            {
                return desiredItem.toString();
            }
            else
            {
                JOptionPane.showMessageDialog(theFrame, "A very severe error has occurred, please end the application.");
                return "A very severe error has occurred, please end the application.";
            }
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(theFrame, "Please connect to the server before trying to get a tool...");
            return null;
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(theFrame, "An IOException occurred while managing a request to get a tool!");
            return null;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(theFrame, "An unexpected error occurred while managing a request to get a tool!");
            return null;
        }
    }
    
    private void getSequenceOfItems() throws IOException
    {
        try
        {
            clearEntries();
            ArrayList<Item>listOfSentItems = comsManager.readSequenceOfItems();
            for(Item sentItem : listOfSentItems)
            {
                idsOfItemsOnDisplay.add(sentItem.getToolIDNumber());
                theFrame.addListingToDisplay(sentItem.getToolName() + ": $" + sentItem.getPrice());
            }
        }
        catch(ClassNotFoundException cnfe)
        {
            JOptionPane.showMessageDialog(theFrame, "The server sent invalid data. Only a portion of the data will be shown.");
        }
        catch(NullPointerException npe)
        {
            //Null should be sent if the database is empty, so we don't need to do anything!
        }
    }
    
    public void clearEntries()
    {
        theFrame.clearListings();
        idsOfItemsOnDisplay.clear();
    }
    
    public void endSession()
    {
        theFrame.setVisible(false);
    }
    
    public void startSession()
    {
        theFrame.setVisible(true);
    }
    
    public void setUpConnection(String serverName, int portNumber) throws IOException
    {
        comsManager.setUpConnection(serverName, portNumber);
    }
    
    public void endConnection()
    {
        
    }
    
    public int idAtIndex(int index)
    {
        return idsOfItemsOnDisplay.get(index);
    }
    
    public static void main(String[] args)
    {
        Client user = new Client();
    }
}
