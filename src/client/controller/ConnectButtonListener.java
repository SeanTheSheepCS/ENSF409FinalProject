package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import client.view.GUI;

public final class ConnectButtonListener implements ActionListener
{
    private Client user;
    private GUI frame;
    
    public ConnectButtonListener(Client user, GUI frame) 
    {
        this.user = user;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try
        {
            //GET THE IP AND SUCH!
            user.setUpConnection("localhost", 9898);
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
    }

}
