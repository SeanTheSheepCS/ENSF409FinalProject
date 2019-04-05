package client.view;

public class CustomerGUI extends LoggedInUserGUI
{
    /**
     * generates a CustomerGUI with buying priviledges
     * 
     * @param title the title of the GUI
     * @param username the username that this user logged in with
     */
    public CustomerGUI(String title, String username)
    {
        super(title,username);
        super.grantBuyingPriviledges();
    }
}
