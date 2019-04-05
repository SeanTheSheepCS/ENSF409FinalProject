package client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import common.model.Item;

/**
 * Manager communication for the client
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 4th 2019
 *
 */
public class CommunicationManager 
{
    private Socket socket;
    private ObjectInputStream objectFromSocket;
    private ObjectOutputStream objectToSocket;
    
    public CommunicationManager()
    {
        socket = null;
        objectFromSocket = null;
        objectToSocket = null;
    }
    
    public void setUpConnection(String serverName, int portNumber) throws IOException
    {
        socket = new Socket(serverName, portNumber);
        objectFromSocket = new ObjectInputStream(socket.getInputStream());
        objectToSocket = new ObjectOutputStream(socket.getOutputStream());
    }
    
    public void endConnection() throws IOException
    {
        objectFromSocket.close();
        objectToSocket.close();
        socket.close();
    }
    
    public Item readItem() throws IOException, ClassNotFoundException
    {
        return (Item) readObject();
    }
    
    public String readString() throws IOException
    {
        try
        {
            return (String) readObject();
        }
        catch(ClassNotFoundException cnfe)
        {
            //How is this even possible?
            System.err.println("HOW ON EARTH ARE YOU RUNNING JAVA WITHOUT THE STRING CLASS????????");
            System.exit(1);
            return "YOU SHOULD NEVER GET HERE!!!!";
        }
    }
    
    private Object readObject() throws IOException, ClassNotFoundException
    {
        return objectFromSocket.readObject();
    }
    
    public void sendMessage(String message) throws IOException
    {
        objectToSocket.writeObject(message);
    }
    
    public ArrayList<Item> readSequenceOfItems() throws IOException, ClassNotFoundException
    {
        ArrayList<Item> itemsOnDisplay = new ArrayList<Item>();
        try
        {
            String message = "";
            do
            {
                message = readString();
                if(message.equals("INVALIDQUERY"))
                {
                    break;
                }
                Item sentItem = readItem();
                itemsOnDisplay.add(sentItem);
                sendMessage("GOTITEM");
            }while(message.equals("TASKINPROGRESS"));
            return itemsOnDisplay;
        }
        catch(NullPointerException npe)
        {
            //Null should be sent if the database is empty, so we don't need to do anything!
            return itemsOnDisplay;
        }
    }
    
    public Item readItemInfo(int specifiedID) throws IOException, ClassNotFoundException
    {
        sendMessage("REQUESTITEMINFO " + specifiedID);
        String messageFromServer = readString();
        if(messageFromServer.equals("INVALIDQUERY"))
        {
            return null;
        }
        else if(messageFromServer.equals("SENDINGITEM"))
        {
            return readItem();
        }
        else
        {
            return null;
        }
    }
    
    public String sendLoginMessageAndReturnServerOutput(String username, String password) throws IOException
    {
        sendMessage("LOGIN" + " " + username + " " + password);
        String messageFromServer = readString();
        return messageFromServer;
    }
}
