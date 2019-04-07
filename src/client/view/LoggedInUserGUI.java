package client.view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * The GUI for a user that is logged in, contains a search bar
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since March 31st 2019
 */
public class LoggedInUserGUI extends GUI
{
    /** displays a greeting at the top of the screen */
    private JLabel greeting;
    /** a button to log the user out */
    private JButton logoutButton;
    
    /** the username that this user used to log in */
    private String username;
    
    /**
     * creates a GUI for a user that is logged in
     * 
     * @param title the title of the GUI
     * @param username the username this user logged in with
     */
    public LoggedInUserGUI(String title, String username)
    {
        super(title);
        this.username = username;
        super.removeLoginCapabilities();
        addLogoutCapabilities();
    }
    
    /**
     * adds logout capabilities to the frame
     */
    private void addLogoutCapabilities()
    {
        greeting = new JLabel("Welcome, " + username);
        greeting.setPreferredSize(new Dimension(200, 20));
        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(75,10));
        
        super.getNorthOptionsPanel().add(greeting);
        super.getNorthOptionsPanel().add(logoutButton);
        super.getNorthOptionsPanel().add(getSearchField());
        super.getNorthOptionsPanel().add(getSearchButton());
    }
    
    /**
     * returns the logout button for this user
     */
    public JButton getLogoutButton()
    {
        return logoutButton;
    }
}
