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
    
    /**
     * Creates a searchButtonListener with a given Client and GUI
     * 
     * @param user the Client that will search for an item
     */
    public SearchButtonListener(Client user) 
    {
        this.user = user;
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
            user.manageSearchRequest(user.getFrame().getSearchTerm());
        }
        catch(NullPointerException npe)
        {
            JOptionPane.showMessageDialog(user.getFrame(), "Please wait for the window to be loaded completely before searching.");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(user.getFrame(), "An unexpected error occurred while searching. Try restarting the program.");
        }
    }

}
