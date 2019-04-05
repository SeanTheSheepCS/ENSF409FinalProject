package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.view.GUI;

/**
 * A listener that waits for the logout button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since March 31st 2019
 */
public class LogoutButtonListener implements ActionListener
{
    /** the Client this logout button should log out */
    private Client user;
    /** the frame the logout should modify */
    private GUI frame;
    
    /**
     * generate a logout button listener with a given user that it should log out and frame that it should modify
     * 
     * @param user the Client that should be logged out if the button is pressed
     * @param frame the GUI that should be modified if the button is pressed
     */
    public LogoutButtonListener(Client user, GUI frame)
    {
        this.user = user;
        this.frame = frame;
    }
    
    /**
     * Logs the user out
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try
        {
            user.manageLogoutRequest();
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(frame, "Please wait for the window to be loaded completely before logging in.");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred while logging in. Try restarting the program.");
        }
    } 
}
