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

import client.model.Item;
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
    private ArrayList<Item> itemsOnDisplay;
    
    private Socket socket;
    private ObjectInputStream objectFromSocket;
    private ObjectOutputStream objectToSocket;
    private BufferedReader stringInputFromSocket;
    private PrintWriter stringOutputToSocket;
    private GUI theFrame;
    private PermissionController pControl;
    
    public Client()
    {
        try
        {
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
        stringInputFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stringOutputToSocket = new PrintWriter(socket.getOutputStream(), true);
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
            stringOutputToSocket.println("LOGIN" + " " + username + " " + password);
            String messageFromServer = stringInputFromSocket.readLine();
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
            stringOutputToSocket.println("SEARCH" + " " + searchTerm);
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
            stringOutputToSocket.println("GETALLITEMS");
            readSequenceOfItems();
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(theFrame, "Please connect to the server before trying to get all tools...");
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(theFrame, "An IOException occurred while managing a request to get all tools!");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(theFrame, "An unexpected error occurred while managing a request to get all tools!");
        }
    }
    
    private void readSequenceOfItems() throws IOException
    {
        try
        {
            String message = "";
            do
            {
                message = stringInputFromSocket.readLine();
                Item sentItem = (Item) objectFromSocket.readObject();
                itemsOnDisplay.add(sentItem);
                theFrame.addListingToDisplay(sentItem.toString());
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
    
    public void setActiveGUI(GUI newFrame)
    {
        if(newFrame != null)
        {
            theFrame.setVisible(false);
            theFrame = newFrame;
            theFrame.setVisible(true);
        }
    }
    
    public static void main(String[] args)
    {
        Client user = new Client();
    }
}
