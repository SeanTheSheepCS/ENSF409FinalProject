package client.view;

public class CustomerGUI extends LoggedInUserGUI
{
    public CustomerGUI(String title, String username)
    {
        super(title,username);
        super.grantBuyingPriviledges();
    }
}
