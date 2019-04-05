package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.view.GUI;

/**
 * When the button is pressed, a search takes place in the database's list
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 5th 2019
 */
public final class SearchButtonListener implements ActionListener
{
    /** the Client that will search for an item */
    private Client user;
    /** the frame that will be changed as a result of this search */
    private GUI frame;
    
    /**
     * Creates a searchButtonListener with a given Client and GUI
     * 
     * @param user the Client that will search for an item
     * @param frame the frame that will be changed as a result of this search
     */
    public SearchButtonListener(Client user, GUI frame) 
    {
        this.user = user;
        this.frame = frame;
    }

    /**
     * The search button was pressed, asks the user to search for the search term the GUI produces
     * 
     * @param e unused
     */
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
