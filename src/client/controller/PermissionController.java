package client.controller;

import javax.swing.JOptionPane;

import client.view.CustomerGUI;
import client.view.GUI;
import client.view.OwnerGUI;

/**
 * Controls the permissions for the Client and GUI
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @sincne March 30th 2019
 */
public final class PermissionController 
{
    private Client user;
    private GUI frame;
    private CustomerGUI customerFrame;
    private OwnerGUI ownerFrame;
    
    public PermissionController(Client user, GUI frame)
    {
        this.user = user;
        this.frame = frame;
    }
    
    public void changePermissionToAdmin()
    {
        ownerFrame = new OwnerGUI(frame.getTitle(), frame.getUsername());
        ownerFrame.getLogoutButton().addActionListener(new LogoutButtonListener(user, ownerFrame));
        user.setActiveGUI(ownerFrame);
    }
    
    public void changePermissionToCustomer()
    {
        customerFrame = new CustomerGUI(frame.getTitle(), frame.getUsername());
        customerFrame.getLogoutButton().addActionListener(new LogoutButtonListener(user, ownerFrame));
        user.setActiveGUI(customerFrame);
    }
    
    public void changePermissionToGuest()
    {
        user.setActiveGUI(frame);
    }
    
    public void manageInvalidLogin()
    {
        JOptionPane.showMessageDialog(frame, "Invalid credentials.");
    }
}
