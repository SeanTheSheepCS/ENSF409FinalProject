package client.orderinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.orderinfopane.view.OrderInfoPaneGUI;

public class ForwardArrowButtonListener implements ActionListener 
{
    OrderInfoPaneGUI parent;

    public ForwardArrowButtonListener(OrderInfoPaneGUI parent)
    {
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        parent.advanceOneOrder();
    }
    
}
