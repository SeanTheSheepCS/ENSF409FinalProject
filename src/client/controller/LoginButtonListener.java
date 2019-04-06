package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.view.GUI;

/**
 * A listener that waits for the login button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since March 31st 2019
 */
public final class LoginButtonListener implements ActionListener
{
    /** the user that the login will modify */
    private Client user;
    
    /**
     * Creates a login button listener with the given elements 
     * 
     * @param user the user that the login will modify
     * @param frame the GUI that a successful login will modify
     */
    public LoginButtonListener(Client user) 
    {
        this.user = user;
    }

    /**
     * Gets fields from the GUI and attempts a login
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try
        {
            user.manageLoginRequest(user.getFrame().getUsername(), user.getFrame().getPassword());
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(user.getFrame(), "Please wait for the window to be loaded completely before logging in.");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(user.getFrame(), "An unexpected error occurred while logging in. Try restarting the program.");
        }
    }

}
