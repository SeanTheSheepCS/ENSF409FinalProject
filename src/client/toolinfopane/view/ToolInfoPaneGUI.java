package client.toolinfopane.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.controller.Client;
import client.view.GUI;
import client.toolinfopane.controller.BuyButtonListener;

public class ToolInfoPaneGUI extends JDialog
{
    private Client user;
    private GUI parent;
    private String toolInfo;
    
    private JLabel infoLabel;
    private JButton buyButton;
    private JButton decreaseQuantityButton;
    
    private JPanel centrePanel;
    private JPanel southPanel;
    
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
    
    private void initializeComponents()
    {
        infoLabel = new JLabel(toolInfo);
        buyButton = new JButton("Buy");
        decreaseQuantityButton = new JButton("Decrease Quantity");
    }
    
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
    
    private void prepareListeners()
    {
        buyButton.addActionListener(new BuyButtonListener(this));
    }
    
    private void prepareWindow()
    {
        setLayout(new BorderLayout());
        add("Center", centrePanel);
        add("South", southPanel);
        setSize(400,140);
        setResizable(false);
        setVisible(true);
    }
}
