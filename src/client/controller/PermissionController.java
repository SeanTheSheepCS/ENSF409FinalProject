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
    /** the user that this permission controller belongs to */
    private Client user;
    /** if this user is logged in as an owner, this will be the new active client */
    private Owner ownerUser;
    /** if this user is logged in in as a customer, this will be the new active client */
    private Customer customerUser;
    /** the frame that is displayed for the user WITH THEIR CURRENT PERMISSIONS TAKEN INTO ACCOUNT */
    private GUI frame;
    
    /**
     * Creates a permission controller for a given user and frame 
     * 
     * @param user the Client this pController is for
     * @param frame the GUI this pController is for
     */
    public PermissionController(Client user, GUI frame)
    {
        this.user = user;
        this.frame = frame;
    }
    
    /**
     * changes the permission to admin for this user
     */
    public void changePermissionToAdmin()
    {
        if(customerUser != null)
        {
            customerUser.endSession();
        }
        user.endSession();
        OwnerGUI ownerFrame = new OwnerGUI(frame.getTitle(), frame.getUsername());
        ownerUser = new Owner(ownerFrame, user.getComsManager());
        ownerUser.startSession();
        ownerFrame.getLogoutButton().addActionListener(new LogoutButtonListener(user));
    }
    
    /**
     * changes the permission to customer for this user
     */
    public void changePermissionToCustomer()
    {
        if(ownerUser != null)
        {
            ownerUser.endSession();
        }
        user.endSession();
        CustomerGUI customerFrame = new CustomerGUI(frame.getTitle(), frame.getUsername());
        customerUser = new Customer(customerFrame, user.getComsManager());
        customerUser.startSession();
        customerFrame.getLogoutButton().addActionListener(new LogoutButtonListener(user));
    }
    
    /**
     * returns permission to guest for this user
     */
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
        frame.clearUsernameField();
        frame.clearPasswordField();
        user.startSession();
    }
    
    /**
     * displays a message if the credentials were invalid
     */
    public void manageInvalidLogin()
    {
        JOptionPane.showMessageDialog(frame, "Invalid credentials.");
    }
}
