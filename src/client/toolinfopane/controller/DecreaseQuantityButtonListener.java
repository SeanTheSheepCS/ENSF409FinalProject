package client.toolinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.toolinfopane.view.ToolInfoPaneGUI;

/**
 * A listener that waits for the decrease quantity button to be pressed
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 6th 2019
 */
public class DecreaseQuantityButtonListener implements ActionListener
{
    ToolInfoPaneGUI parent;
    
    public DecreaseQuantityButtonListener(ToolInfoPaneGUI parent)
    {
        this.parent = parent;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
