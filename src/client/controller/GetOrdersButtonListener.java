package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GetOrdersButtonListener implements ActionListener
{
    private Owner parent;
    
    public GetOrdersButtonListener(Owner parent)
    {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        parent.manageGetAllOrdersRequest();    
    }

}
