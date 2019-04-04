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
    private Owner ownerUser;
    private Customer customerUser;
    private GUI frame;
    
    public PermissionController(Client user, GUI frame)
    {
        this.user = user;
        this.frame = frame;
    }
    
    public void changePermissionToAdmin()
    {
        if(customerUser != null)
        {
            customerUser.endSession();
        }
        user.endSession();
        OwnerGUI ownerFrame = new OwnerGUI(frame.getTitle(), frame.getUsername());
        ownerFrame.getLogoutButton().addActionListener(new LogoutButtonListener(user, ownerFrame));
        Owner ownerUser = new Owner(ownerFrame);
    }
    
    public void changePermissionToCustomer()
    {
        if(ownerUser != null)
        {
            ownerUser.endSession();
        }
        user.endSession();
        CustomerGUI customerFrame = new CustomerGUI(frame.getTitle(), frame.getUsername());
        customerFrame.getLogoutButton().addActionListener(new LogoutButtonListener(user, customerFrame));
        Customer customerUser = new Customer(customerFrame);
    }
    
    public void changePermissionToGuest()
    {
        if(ownerUser != null)
        {
            ownerUser.endSession();
        }
        if(customerUser != null)
        {
            customerUser.endSession();
        }
        user.startSession();
    }
    
    public void manageInvalidLogin()
    {
        JOptionPane.showMessageDialog(frame, "Invalid credentials.");
    }
}
