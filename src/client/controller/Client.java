package client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

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
public class Client 
{
    private Socket socket;
    private ObjectInputStream objectFromSocket;
    private ObjectOutputStream objectToSocket;
    private BufferedReader stringInputFromSocket;
    private PrintWriter stringOutputToSocket;
    private GUI theFrame;
    private PermissionController pControl;
    
    public Client(String serverName, int portNumber, boolean shouldConnect)
    {
        try
        {
            if(shouldConnect)
            {
                setUpConnection(serverName, portNumber);
            }
            theFrame = new GUI("Toolshop application");
            pControl = new PermissionController(this,theFrame);
            prepareListeners();
        }
        catch(IOException ioe)
        {
            System.out.println("An IOException occurred while initializing the client, maybe the server is not open?");
        }
        catch(Exception e)
        {
            System.out.println("An unexpected error occurred while initializing the client.");
        }
    }
    
    private void setUpConnection(String serverName, int portNumber) throws IOException
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
    
    public Item readItemFromSocket()
    {
        return null;
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
                theFrame.manageInvalidLogin();
            }
        }
        catch(NullPointerException npe)
        {
            System.out.println("Please prepare everything before trying to validate a login...");
        }
        catch(IOException ioe)
        {
            System.out.println("An IOException occurred while managing a login request!");
        }
        catch(Exception e)
        {
            System.out.println("An unexpected error occurred while managing a login request!");
        }
    }
    
    public void sendToolToSocket(Item toolToSend)
    {
        
    }
    
    public void requestToolInfoFromSocket()
    {
        
    }
    
    public void interpretServerOutput(String output)
    {
        
    }
    
    public void sendStringToGUI(String message)
    {
        
    }
    
    public static void main(String[] args)
    {
        Client user = new Client("localhost", 9898, false);
    }
}
