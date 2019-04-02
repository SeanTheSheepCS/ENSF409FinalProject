package client.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.model.Item;

/**
 * The GUI for the client
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since March 30th 2019
 *
 */
public class GUI extends JFrame
{
    private JLabel header;
    
    private DefaultListModel<String> stringDataOnDisplay;
    private JList<String> dataStorage;
    private JScrollPane paneForData;
    
    protected JButton searchButton;
    protected JButton loginButton;
    protected JButton connectButton;
    
    protected JTextField usernameField;
    protected JTextField passwordField;
    protected JTextField searchField;
    
    protected JPanel northOptionsPanel;
    
    private JPanel northPanel;
    private JPanel centrePanel;
    private JPanel southPanel;
    
    public GUI(String title)
    {
        super(title);
        try
        {
            setResizable(false);
            
            initializeComponents();
            assignComponentsToPanels();
            
            setLayout(new BorderLayout());
            add("North", northPanel);
            add("Center", centrePanel);
            add("South", southPanel);
            
            pack();
            setVisible(true);
        }
        catch(Exception e)
        {
            System.out.println("An unexpected exception occurred while loading the elements of the GUI.");
        }
    }
    
    private void initializeComponents()
    {
        header = new JLabel("Tool Shop Application");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        
        usernameField = new JTextField();
        passwordField = new JTextField();
        searchField = new JTextField();
        
        searchButton = new JButton("Search");
        loginButton = new JButton("Login");
        connectButton = new JButton("Connect");
        connectButton.setHorizontalAlignment(SwingConstants.CENTER);
        
        stringDataOnDisplay = new DefaultListModel<String>();
        dataStorage = new JList<String>(stringDataOnDisplay);
        dataStorage.setFont(new Font("Times New Roman", Font.BOLD, 12));
        dataStorage.setVisibleRowCount(15);
        dataStorage.setPrototypeCellValue("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        paneForData = new JScrollPane(dataStorage);
        
        northPanel = new JPanel();
        northOptionsPanel = new JPanel();
        centrePanel = new JPanel();
        southPanel = new JPanel();
    }
    
    private void assignComponentsToPanels()
    {
        northOptionsPanel.setLayout(new BoxLayout(northOptionsPanel, BoxLayout.X_AXIS));
        northOptionsPanel.add(usernameField);
        northOptionsPanel.add(passwordField);
        northOptionsPanel.add(loginButton);
        northOptionsPanel.add(searchField);
        northOptionsPanel.add(searchButton);
        
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(header);
        northPanel.add(northOptionsPanel);
        
        centrePanel.add(paneForData);
        
        southPanel.add(connectButton);
    }
    
    public void addListingToDisplay(String itemAsString)
    {
        stringDataOnDisplay.addElement(itemAsString);
    }
    
    public JButton getSearchButton()
    {
        return searchButton;
    }
    
    public JButton getLoginButton()
    {
        return loginButton;
    }
    
    public JButton getConnectButton()
    {
        return connectButton;
    }
    
    public String getUsername() throws NullPointerException
    {
        return usernameField.getText();
    }
    
    public String getPassword() throws NullPointerException
    {
        return passwordField.getText();
    }
    
    public String getSearchTerm() throws NullPointerException
    {
        return searchField.getText();
    }
}
