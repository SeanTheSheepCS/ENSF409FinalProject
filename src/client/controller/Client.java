package client.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import client.view.GUI;
import common.model.Item;

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
    /** An arraylist containing the IDS of the items currenty on display on theFrame */
    private ArrayList<Integer> idsOfItemsOnDisplay;
    /** manages the communication for the client */
    protected CommunicationManager comsManager;
    /** the GUI attached to this client */
    private GUI theFrame;
    /** controls logins and logouts for this client */
    private PermissionController pControl;

    /**
     * creates a frame and listeners, also initializes the permission controller and coms manager.
     */
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
    
    /**
     * For use by classes that extend client such as customer and owner, generates a client with a given frame and connection already in place
     * 
     * @param frame the frame to initialize this client with
     * @param comsManager the CommunicationManager that this client is assigned to
     * @param pControl the permission controller that this client is assigned to
     */
    protected Client(GUI frame, CommunicationManager comsManager, PermissionController pControl)
    {
        try
        {
            idsOfItemsOnDisplay = new ArrayList<Integer>();
            theFrame = frame;
            this.pControl = pControl;
            this.comsManager = comsManager;
            if(comsManager.isConnected())
            {
                manageGetAllToolsRequest();
                theFrame.enterConnectedState();
            }
            else
            {
                theFrame.exitConnectedState();
            }
            prepareListeners();
        }
        catch(Exception e)
        {
            System.out.println("An unexpected error occurred while initializing the client.");
        }
    }
    
    /**
     * prepares the following listeners that the client needs.
     * -login button listener
     * -search button listener
     * -connect button listener
     * -list selection listener
     * -disconnect button listener
     */
    private void prepareListeners()
    {
        try
        {
            theFrame.getLoginButton().addActionListener(new LoginButtonListener(this));
            theFrame.getSearchButton().addActionListener(new SearchButtonListener(this));
            theFrame.getConnectButton().addActionListener(new ConnectButtonListener(this));
            theFrame.getDisconnectButton().addActionListener(new DisconnectButtonListener(this));
            theFrame.getList().addListSelectionListener(new ListListener(this, theFrame.getList()));
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
    
    /**
     * Handles a login request by giving asking the coms manager to send a message and giving the work to the permission controller.
     * 
     * @param username username that the GUI detected
     * @param password the password that the GUI detected
     */
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
    
    /**
     * Handles a search request by clearing the GUI's entries and then searching for a given term
     * 
     * @param searchTerm the term to search for
     */
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
    
    /**
     * tells the permission controller to change the permission to guest
     */
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
    
    /**
     * sends a message to get all items, reads all the items, and then displays them on the GUI
     */
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
    
    /**
     * manages a request to decrease the quantity of a given tool, returns whether it was successful since errors produced by this function should be managed elsewhere
     * 
     * @param idOfToolToDecreaseQuantityOf the ID of the item we want to decrease the quantity of
     * @param quantityToDecreaseBy how much the quantity should be decreased by
     * @return true if nothing went wrong, false otherwise
     */
    public boolean manageDecreaseQuantityRequest(int idOfToolToDecreaseQuantityOf, int quantityToDecreaseBy)
    {
        try
        {
            comsManager.sendMessage("DECQUANTITY " + idOfToolToDecreaseQuantityOf + " " + quantityToDecreaseBy);
            return true;
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(theFrame, "Please connect to the server before trying to get all tools...");
            return false;
        }
        catch(IOException ioe)
        {
            return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    /**
     * requests info for an item with a specific ID
     * 
     * @param specifiedID the ID of the tool we want info of
     */
    public String requestItemInfo(int specifiedID)
    {
        try
        {
            Item desiredItem = comsManager.readItemInfo(specifiedID);
            if(desiredItem != null)
            {
                return desiredItem.toStringWithoutIDOrDetailedSupplierInfo();
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
    
    /**
     * reads a sequence of items from the server read through the comsManager
     * 
     * @throws IOException
     */
    private void getSequenceOfItems() throws IOException
    {
        try
        {
            theFrame.setCursorToWaitState();
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
        finally
        {
            theFrame.setCursorToScrewdriver();
        }
    }
    
    /**
     * clears all entries in the GUI and clears the ID list within Client
     */
    public void clearEntries()
    {
        theFrame.clearListings();
        idsOfItemsOnDisplay.clear();
    }
    
    /**
     * closes the window
     */
    public void endSession()
    {
        theFrame.setVisible(false);
        if(comsManager.isConnected() == true)
        {
            theFrame.enterConnectedState();
        }
        else
        {
            theFrame.exitConnectedState();
        }
    }
    
    /**
     * opens the window
     */
    public void startSession()
    {
        theFrame.setVisible(true);
        if(comsManager.isConnected() == true)
        {
            theFrame.enterConnectedState();
        }
        else
        {
            theFrame.exitConnectedState();
        }
    }
    
    /**
     * sets up a connection at a given serverName and portNumber
     * 
     * @param serverName the IP to connect to
     * @param portNumber the portnumber (should always be 9898)
     * @throws IOException
     */
    public void setUpConnection(String serverName, int portNumber) throws IOException
    {
        comsManager.setUpConnection(serverName, portNumber);
        theFrame.enterConnectedState();
    }
    
    /**
     * closes the connection of this client
     */
    public void endConnection()
    {
        try
        {
            clearData();
            comsManager.endConnection();
            theFrame.exitConnectedState();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(theFrame, "Failed to end the connection with the server.");
        }
    }
    
    private void clearData()
    {
        idsOfItemsOnDisplay.clear();
        theFrame.clearListings();
        pControl.clearDataOnAllGUIS();
    }
    
    public GUI getFrame()
    {
        return theFrame;
    }
    
    /**
     * returns the ID at an index
     * 
     * @param index the index in the IDList we are intrested in
     * @return the ID at the given index
     */
    public int idAtIndex(int index)
    {
        return idsOfItemsOnDisplay.get(index);
    }
    
    /**
     * returns true if the client is connected, false otherwise
     * 
     * @return true if the client is connected, false otherwise
     */
    public boolean isConnected()
    {
        return comsManager.isConnected();
    }
    
    /**
     * returns the comsManager for this user
     * 
     * @return the comsManager for this user
     */
    public CommunicationManager getComsManager()
    {
        return comsManager;
    }
    
    public static void main(String[] args)
    {
        Client user = new Client();
    }
}
