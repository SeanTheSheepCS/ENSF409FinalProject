package client.toolinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.view.GUI;
import client.toolinfopane.view.ToolInfoPaneGUI;


public class BuyButtonListener implements ActionListener
{
    ToolInfoPaneGUI parent;
    
    public BuyButtonListener(ToolInfoPaneGUI parent)
    {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        JOptionPane.showMessageDialog(parent, "Buy button pressed!");
    }
}