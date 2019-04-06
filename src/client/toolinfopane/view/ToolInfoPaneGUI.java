package client.toolinfopane.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.controller.Client;
import client.view.GUI;
import client.toolinfopane.controller.BuyButtonListener;
import client.toolinfopane.controller.DecreaseQuantityButtonListener;

/**
 * The GUI that displays the information of a tool
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
public class ToolInfoPaneGUI extends JDialog
{
    /** the Client that should ask for tool info */
    private Client user;
    /** the GUI that this JDialog should be a child of */
    private GUI parent;
    /** the info of the tool */
    private String toolInfo;
    
    private JLabel infoLabel;
    private JButton buyButton;
    private JButton decreaseQuantityButton;
    
    private JPanel centrePanel;
    private JPanel southPanel;
    
    /**
     * Generates a toolInfoPane with the given elements
     * 
     * @param user the user that we can communicate with
     * @param parent the parent of this JDialog
     * @param toolInfo the info of the tool that should be displayed
     * @param toolID the ID of the tool that should be displayed
     */
    public ToolInfoPaneGUI(Client user, GUI parent, String toolInfo, int toolID)
    {
        super(parent, "Tool Info");
        this.user = user;
        this.parent = parent;
        this.toolInfo = toolInfo;
        initializeComponents();
        addToPanels();
        prepareListeners();
        prepareWindow();
    }
    
    /**
     * initialize the components of the JDialog
     */
    private void initializeComponents()
    {
        String toolInfoLabelFriendly = "<html>" + toolInfo.replaceAll("\n", "<br/>") + "</html>";
        infoLabel = new JLabel(toolInfoLabelFriendly);
        buyButton = new JButton("Buy");
        decreaseQuantityButton = new JButton("Decrease Quantity");
        
        centrePanel = new JPanel();
        southPanel = new JPanel();
    }
    
    /**
     * add the components, including the buy button and decrease quantity button if applicable
     */
    private void addToPanels()
    {
        centrePanel.add(infoLabel);
        if(parent.hasBuyingPriviledges())
        {
            southPanel.add(buyButton);
        }
        if(parent.hasDecreaseQuantityPriviledges())
        {
            southPanel.add(decreaseQuantityButton);
        }
    }
    
    /**
     * prepares the listeners for the buy button and decrease quantity button
     */
    private void prepareListeners()
    {
        buyButton.addActionListener(new BuyButtonListener(this));
        decreaseQuantityButton.addActionListener(new DecreaseQuantityButtonListener(this));
    }
    
    /**
     * prepares the window with a proper size and with the panels put in
     */
    private void prepareWindow()
    {
        setLayout(new BorderLayout());
        add("Center", centrePanel);
        add("South", southPanel);
        setSize(700,200);
        setResizable(false);
        setVisible(true);
    }
    
    public void sendBuyMessage()
    {
        
    }
    
    public void sendDecreaseQuantityMessage()
    {
        
    }
}
