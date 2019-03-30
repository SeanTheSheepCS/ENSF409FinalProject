package client.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI extends JFrame
{
    private JLabel header;
    
    private DefaultListModel<String> dataOnDisplay;
    private JList<String> dataStorage;
    private JScrollPane paneForData;
    
    private JButton searchButton;
    private JButton loginButton;
    
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField searchField;
    
    private JPanel northPanel;
    private JPanel centrePanel;
    
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
        
        northPanel = new JPanel();
    }
    
    private void assignComponentsToPanels()
    {
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
    }
    
    public void manageInvalidLogin()
    {
        
    }
    
    public JButton getSearchButton()
    {
        return searchButton;
    }
    
    public JButton getLoginButton()
    {
        return loginButton;
    }
}
