package client.toolinfopane.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.controller.Client;
import client.toolinfopane.view.ToolInfoPaneGUI;

/**
 * A listener that waits for the refresh button to the tool info pane GUI to be pressed and then updates the toolInfoPaneGUI with new info from server
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 9th 2019
 */
public class RefreshButtonListenerForToolInfoPane implements ActionListener
{
    /** the ToolInfoPaneGUI that will recieve the updated information */
    private ToolInfoPaneGUI parent;
    /** the client that will be asked for updated information */
    private Client clientToAsk;
    
    /**
     * Creates a listener that knows what client to ask for information and which ToolInfoPaneGUI to put the updated information in
     * 
     * @param clientToAsk the client that should be asked for info
     * @param parent the ToolInfoPane that will be updated as a result of the refresh button being pressed
     */
    public RefreshButtonListenerForToolInfoPane(Client clientToAsk, ToolInfoPaneGUI parent)
    {
        this.clientToAsk = clientToAsk;
        this.parent = parent;
    }
    
    /**
     * gives the tool info of the ToolInfoPaneGUI a more recent value
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        parent.setUpdatedToolInfo(clientToAsk.requestItemInfo(parent.getToolIDOfItemOnDisplay()));
    }
}
