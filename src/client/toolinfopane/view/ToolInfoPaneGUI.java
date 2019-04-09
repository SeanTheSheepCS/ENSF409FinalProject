package client.toolinfopane.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.controller.Client;
import client.controller.Owner;
import client.orderinfopane.view.OrderInfoPaneGUI;
import client.view.GUI;
import client.toolinfopane.controller.BuyButtonListener;
import client.toolinfopane.controller.DecreaseQuantityButtonListener;
import client.toolinfopane.controller.RefreshButtonListenerForToolInfoPane;

/**
 * The GUI that displays the information of a tool
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
public final class ToolInfoPaneGUI extends JDialog
{
    /** the GUI that this JDialog should be a child of */
    private GUI parent;
    /** the info of the tool */
    private String toolInfo;
    /** the ID of the tool on display */
    private int toolIDOfItemOnDisplay;
    
    private JLabel infoLabel;
    private JButton buyButton;
    private JButton decreaseQuantityButton;
    private JButton refreshButton;
    
    private JPanel northPanel;
    private JPanel centrePanel;
    private JPanel southPanel;
    
    /**
     * Generates a toolInfoPane with the given elements
     * 
     * @param user the user that we can communicate with
     * @param parent the parent of this JDialog
     * @param toolInfo the info of the tool that should be displayed
     * @param toolIDOfItemOnDisplay the ID of the tool that should be displayed
     */
    public ToolInfoPaneGUI(Client user, GUI parent, String toolInfo, int toolIDOfItemOnDisplay)
    {
        super(parent, "Tool Info");
        this.parent = parent;
        this.toolInfo = toolInfo;
        this.toolIDOfItemOnDisplay = toolIDOfItemOnDisplay;
        initializeComponents();
        addToPanels();
        prepareListeners(user);
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
        refreshButton = new JButton("🗘");
        
        northPanel = new JPanel();
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
        northPanel.add(refreshButton);
    }
    
    /**
     * prepares the listeners for the buy button and decrease quantity button
     */
    private void prepareListeners(Client user)
    {
        buyButton.addActionListener(new BuyButtonListener(user,this));
        decreaseQuantityButton.addActionListener(new DecreaseQuantityButtonListener(user,this));
        refreshButton.addActionListener(new RefreshButtonListenerForToolInfoPane(user,this));
    }
    
    /**
     * prepares the window with a proper size and with the panels put in
     */
    private void prepareWindow()
    {
        setLayout(new BorderLayout());
        add("Center", centrePanel);
        add("South", southPanel);
        add("North", northPanel);
        setSize(700,200);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * returns the ID of the item on display in this ToolInfoPaneGUI
     * 
     * @return the ID of the item on display in this ToolInfoPaneGUI
     */
    public int getToolIDOfItemOnDisplay()
    {
        return toolIDOfItemOnDisplay;
    }
    
    /**
     * updates toolInfo with a new String
     * 
     * @param updatedToolInfo the new tool info to be displayed in the pane
     */
    public void setUpdatedToolInfo(String updatedToolInfo)
    {
        toolInfo = updatedToolInfo;
        String toolInfoLabelFriendly = "<html>" + toolInfo.replaceAll("\n", "<br/>") + "</html>";
        infoLabel.setText(toolInfoLabelFriendly);
    }
}
