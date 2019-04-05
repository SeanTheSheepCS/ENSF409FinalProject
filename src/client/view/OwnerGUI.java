package client.view;

/**
 * The GUI for the owner, displays item IDs
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since March 31st 2019
 * 
 */
public class OwnerGUI extends LoggedInUserGUI
{
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
    }
}
