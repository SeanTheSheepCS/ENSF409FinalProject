package client.view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;

import client.controller.LogoutButtonListener;

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
        super.northOptionsPanel.remove(searchField);
        super.northOptionsPanel.remove(searchButton);
    }
    
    private void addLogoutCapabilities()
    {
        greeting = new JLabel("Welcome, " + username);
        greeting.setPreferredSize(new Dimension(200, 20));
        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(75,10));
        
        super.northOptionsPanel.add(greeting);
        super.northOptionsPanel.add(logoutButton);
        super.northOptionsPanel.add(searchField);
        super.northOptionsPanel.add(searchButton);
    }
    
    public JButton getLogoutButton()
    {
        return logoutButton;
    }
}
