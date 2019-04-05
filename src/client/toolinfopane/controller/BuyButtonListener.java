package client.toolinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.view.GUI;
import client.toolinfopane.view.ToolInfoPaneGUI;

/**
 * A listener that waits for the buy button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
public class BuyButtonListener implements ActionListener
{
    /** the ToolPaneInfoGUI whose buy button this is monitoring */
    ToolInfoPaneGUI parent;
    
    /**
     * generates a BuyButtonListener with a given parent
     * 
     * @param parent the ToolPaneInfoGUI whose buy button this is monitoring
     */
    public BuyButtonListener(ToolInfoPaneGUI parent)
    {
        this.parent = parent;
    }

    /**
     * the buy button was pressed, should send command to server to reduce quantity
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        JOptionPane.showMessageDialog(parent, "Buy button pressed!");
        //TODO: Implementation
    }
}