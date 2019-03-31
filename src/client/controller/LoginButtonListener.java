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
    private Client user;
    private GUI frame;
    
    public LoginButtonListener(Client user, GUI frame) 
    {
        this.user = user;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try
        {
            user.manageLoginRequest(frame.getUsername(), frame.getPassword());
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
