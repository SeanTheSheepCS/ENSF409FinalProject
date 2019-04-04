package client.controller;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.toolinfopane.view.ToolInfoPaneGUI;
import client.view.GUI;

class ListListener implements ListSelectionListener
{
    private Client user;
    private JList<String> listArea;
    private GUI frame;
    
    public ListListener(Client user, GUI frame, JList<String> listArea)
    {
        this.user = user;
        this.listArea = listArea;
        this.frame = frame;
    }

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
