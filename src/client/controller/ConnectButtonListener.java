package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import client.view.GUI;

/**
 * A listener that waits for the connect button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
final class ConnectButtonListener implements ActionListener
{
    /** the user that the connection should be set up with */
    private Client user;
    /** the GUI that should change if the connection was successful */
    private GUI frame;
    
    /**
     * generate a connect button listener with a given client to connect and a GUI that should change if the connection is successful
     * 
     * @param user the user that the connection should be set up with
     * @param frame the GUI that should change if the connection is successful
     */
    public ConnectButtonListener(Client user, GUI frame) 
    {
        this.user = user;
        this.frame = frame;
    }

    /**
     * Creates prompt on the GUI for an IP address and then connects to a server at that given IP
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try
        {
            frame.setCursorToWaitState();
            //GET THE IP AND SUCH!
            String ipToConnectTo = JOptionPane.showInputDialog("Please enter the IP you want to connect to.");
            user.setUpConnection(ipToConnectTo,9898);
            JOptionPane.showMessageDialog(frame, "Connection successfully made!");
            user.manageGetAllToolsRequest();
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(frame, "Unable to connect to the server. Make sure your server is currently running.");
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(frame, "Please wait for the window to be loaded completely before logging in.");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred while connecting. Try restarting the program.");
        }
        finally
        {
            frame.setCursorToScrewdriver();
        }
    }

}
