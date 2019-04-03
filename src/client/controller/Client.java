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
public final class Client 
{
    private ArrayList<Integer> idsOfItemsOnDisplay;
    
    private Socket socket;
    private ObjectInputStream objectFromSocket;
    private ObjectOutputStream objectToSocket;
    private GUI theFrame;
    private PermissionController pControl;
    
    public Client()
    {
        try
        {
            idsOfItemsOnDisplay = new ArrayList<Integer>();
            theFrame = new GUI("Toolshop Application");
            pControl = new PermissionController(this,theFrame);
            prepareListeners();
        }
        catch(Exception e)
        {
            System.out.println("An unexpected error occurred while initializing the client.");
        }
    }
    
    public void setUpConnection(String serverName, int portNumber) throws IOException
    {
        socket = new Socket(serverName, portNumber);
        objectFromSocket = new ObjectInputStream(socket.getInputStream());
        objectToSocket = new ObjectOutputStream(socket.getOutputStream());
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
            sendStringMessage("LOGIN" + " " + username + " " + password);
            String messageFromServer = interpretStringMessage();
            if(messageFromServer.equals("ADMIN"))
            {
                pControl.changePermissionToAdmin();
            }
            else if(messageFromServer.equals("CUSTOMER"))
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
            sendStringMessage("SEARCH" + " " + searchTerm);
            readSequenceOfItems();
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
            sendStringMessage("GETALLITEMS");
            readSequenceOfItems();
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
            sendStringMessage("REQUESTITEMINFO " + specifiedID);
            String messageFromServer = interpretStringMessage();
            if(messageFromServer.equals("INVALIDQUERY"))
            {
                JOptionPane.showMessageDialog(theFrame, "Our servers were not able to handle your request.");
                return null;
            }
            else if(messageFromServer.equals("SENDINGITEM"))
            {
                Item requestedItem = (Item) objectFromSocket.readObject();
                return requestedItem.toString();
            }
            else
            {
                JOptionPane.showMessageDialog(theFrame, "A very severe error occurred. Please end the application.");
                return null;
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
    
    private void readSequenceOfItems() throws IOException
    {
        try
        {
            String message = "";
            do
            {
                message = interpretStringMessage();
                if(message.equals("INVALIDQUERY"))
                {
                    break;
                }
                Item sentItem = (Item) objectFromSocket.readObject();
                idsOfItemsOnDisplay.add(sentItem.getToolIDNumber());
                theFrame.addListingToDisplay(sentItem.getToolName() + ": $" + sentItem.getPrice());
                sendStringMessage("GOTITEM");
                System.out.println(message);
            }while(message.equals("TASKINPROGRESS"));
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
    
    private void sendToolToSocket(Item toolToSend)
    {
        
    }
    
    private void sendStringMessage(String message) throws IOException
    {
        objectToSocket.writeObject(message);
    }
    
    private String interpretStringMessage() throws IOException
    {
        try
        {
            String message = (String) objectFromSocket.readObject();
            return message;
        }
        catch(ClassNotFoundException cnfe)
        {
            JOptionPane.showMessageDialog(theFrame,"How are you running Java without the String class???????????");
            return "?";
        }
    }
    
    public void setActiveGUI(GUI newFrame)
    {
        if(newFrame != null)
        {
            theFrame.setVisible(false);
            theFrame = newFrame;
            theFrame.setVisible(true);
        }
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
