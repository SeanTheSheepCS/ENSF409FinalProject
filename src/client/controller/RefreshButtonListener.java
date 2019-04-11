package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Waits for the refresh button on the Client's GUI to be pressed and then gets new data from the server
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 9th 2019
 */
public class RefreshButtonListener implements ActionListener
{
    /** the client that will get new data when the button is pressed */
    private Client user;
    
    /**
     * creates the refresh button listener with a specified client that will ask for data and be updatred
     * 
     * @param user the user that will ask for data and be updated
     */
    public RefreshButtonListener(Client user)
    {
        this.user = user;
    }
    
    /**
     * clears the user's data entries and asks the client to get a new updated data set from the server
     * 
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        user.clearEntries();
        user.manageSearchRequest(user.getFrame().getSearchTerm());
    }

}
