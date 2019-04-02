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
    public OwnerGUI(String title, String username)
    {
        super(title,username);
        super.grantDecreaseQuantityPriviledges();
    }
}
