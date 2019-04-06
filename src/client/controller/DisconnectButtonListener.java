package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Waits for the disconnect button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 6th 2019
 */
public class DisconnectButtonListener implements ActionListener
{
    /** the Client that will be disconnected */
    private Client user;
    
    /**
     * creates a disconnect button listener for a given client
     * 
     * @param user the Client to disconnect
     */
    public DisconnectButtonListener(Client user)
    {
        this.user = user;
    }

    /**
     * ends the user's connection with the server
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        user.endConnection();
    }
    
}
