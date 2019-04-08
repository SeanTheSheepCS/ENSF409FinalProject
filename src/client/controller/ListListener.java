package client.controller;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.toolinfopane.view.ToolInfoPaneGUI;

/**
 * A listener for when an entry in the list is selected
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
final class ListListener implements ListSelectionListener
{
    /** the user that this client will use to get IDs associated with indices */
    private Client user;
    /** the listArea that was clicked */
    private JList<String> listArea;
    
    /**
     * creates a ListListener attached with the given elements
     * 
     * @param user the user that this client will use to get IDs associated with indices
     * @param listArea the listArea that was clicked 
     */
    public ListListener(Client user, JList<String> listArea)
    {
        this.user = user;
        this.listArea = listArea;
    }

    /**
     * This triggers twice when a list index is clicked. Only clicks when the value was adjusting count
     * 
     * @param e unused
     */
    @Override
    public void valueChanged(ListSelectionEvent e) 
    {
        if(e.getValueIsAdjusting())
        {
            int index =  listArea.getSelectedIndex();
            if(index >= 0)
            {
                int toolID = user.idAtIndex(index);
                String toolInfo = user.requestItemInfo(toolID);
                if(toolInfo != null)
                {
                    ToolInfoPaneGUI infoPane = new ToolInfoPaneGUI(user, user.getFrame(), toolInfo,toolID);
                }
            }
        }
    }
}
