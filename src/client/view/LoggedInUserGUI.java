package client.view;

import javax.swing.JButton;
import javax.swing.JLabel;

public class LoggedInUserGUI extends GUI
{
    private JLabel greeting;
    private JButton logoutButton;
    
    private String username;
    
    public LoggedInUserGUI(String title, String username)
    {
        super(title);
        removeLoginCapabilities();
        addLogoutCapabilities();
    }
    
    private void removeLoginCapabilities()
    {
        super.northOptionsPanel.remove(usernameField);
        super.northOptionsPanel.remove(passwordField);
        super.northOptionsPanel.remove(loginButton);
    }
    
    private void addLogoutCapabilities()
    {
        greeting = new JLabel("Welcome, " + username);
        logoutButton = new JButton("Logout");
        
        super.northOptionsPanel.add(greeting);
        super.northOptionsPanel.add(logoutButton);
    }
}
