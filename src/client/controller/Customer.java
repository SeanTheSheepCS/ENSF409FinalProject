package client.controller;

import client.view.CustomerGUI;

/**
 * The customer class for the tool shop application
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 4th 2019
 *
 */
final class Customer extends Client
{
    /**
     * creates a customer with a given CustomerGUI
     * 
     * @param frame the customer GUI this customer should use
     */
    public Customer(CustomerGUI frame, CommunicationManager comsManager, PermissionController pControl)
    {
        super(frame, comsManager, pControl);
    }
}
