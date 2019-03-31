package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.view.GUI;

public final class SearchButtonListener implements ActionListener
{
    private Client user;
    private GUI frame;
    
    public SearchButtonListener(Client user, GUI frame) 
    {
        this.user = user;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try
        {
            user.manageSearchRequest(frame.getSearchTerm());
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(frame, "Please wait for the window to be loaded completely before searching.");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred while searching. Try restarting the program.");
        }
    }

}
