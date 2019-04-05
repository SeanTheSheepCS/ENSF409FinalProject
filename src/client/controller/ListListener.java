package client.controller;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.toolinfopane.view.ToolInfoPaneGUI;
import client.view.GUI;

/**
 * A listener for when an entry in the list is selected
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
class ListListener implements ListSelectionListener
{
    /** the user that this client will use to get IDs associated with indices */
    private Client user;
    /** the listArea that was clicked */
    private JList<String> listArea;
    /** the frame that the popup should be associated with */
    private GUI frame;
    
    /**
     * creates a ListListener attached with the given elements
     * 
     * @param user the user that this client will use to get IDs associated with indices
     * @param frame the frame that the popup should be associated with
     * @param listArea the listArea that was clicked 
     */
    public ListListener(Client user, GUI frame, JList<String> listArea)
    {
        this.user = user;
        this.listArea = listArea;
        this.frame = frame;
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
                    ToolInfoPaneGUI infoPane = new ToolInfoPaneGUI(user, frame, toolInfo,toolID);
                }
            }
        }
    }
}
