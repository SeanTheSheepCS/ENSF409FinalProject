package client.view;

import javax.swing.JButton;

/**
 * The GUI for the owner, displays item IDs
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since March 31st 2019
 * 
 */
public final class OwnerGUI extends LoggedInUserGUI
{
    /** A button that displays all orders when it is pressed */
    private JButton getAllOrdersButton;
    
    /**
     * creates an OwnerGUI with a given title and username, the owner has decrease quantity priviledges
     * 
     * @param title the title of this GUI
     * @param username the username the owner logged in with
     */
    public OwnerGUI(String title, String username)
    {
        super(title,username);
        super.grantDecreaseQuantityPriviledges();
        addOrderButton();
    }
    
    /**
     * adds the "Get All Orders" button to the GUI
     */
    public void addOrderButton()
    {
        getAllOrdersButton = new JButton("Get All Orders");
        super.getSouthPanel().add(getAllOrdersButton);
    }
    
    /**
     * returns the getAllOrdersButton
     */
    public JButton getTheGetAllOrdersButton()
    {
        return getAllOrdersButton;
    }
}
