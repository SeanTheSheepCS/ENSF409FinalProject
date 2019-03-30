package client.controller;

import client.view.GUI;

/**
 * Controls the permissions for the Client and GUI
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @sincne March 30th 2019
 */
public class PermissionController 
{
    private Client user;
    private GUI frame;
    
    public PermissionController(Client user, GUI frame)
    {
        this.user = user;
        this.frame = frame;
    }
    
    public void changePermissionToAdmin()
    {
        
    }
    
    public void changePermissionToCustomer()
    {
        
    }
    
    public void changePermissionToGuest()
    {
        
    }
}
