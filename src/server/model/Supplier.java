package server.model;

/**
 * The supplier for an item in the tool shop application
 * 
 * @author Sean Kenny
 * @version 1.0
 * @since February 4th 2019
 *
 */
public class Supplier 
{
    /** the ID of the supplier */
    private int supplierID;
    /** the name of the company */
    private String companyName;
    /** the address of the supplier */
    private String address;
    /** the sales contact of this supplier */
    private String salesContact;
    
    /**
     * constructs a supplier
     */
    public Supplier()
    {
        supplierID = 0;
        companyName = "UNKNOWN_SUPPLIER";
        address = "UNKNOWN_ADDRESS";
        salesContact = "NO_SALES_CONTACT";
    }
    
    /**
     * sets the ID of the supplier
     * 
     * @param supplierID the new supplierID
     */
    public void setSupplierID(int supplierID)
    {
        this.supplierID = supplierID;
    }
    
    /**
     * sets the company name of the supplier
     * 
     * @param companyName the new company name
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
    /**
     * sets the address of the supplier
     * 
     * @param address the new address
     */
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    /**
     * sets the sales contact of the supplier
     * 
     * @param salesContact the new sales contact
     */
    public void setSalesContact(String salesContact)
    {
        this.salesContact = salesContact;
    }
    
    /**
     * returns the supplier's ID
     * 
     * @return the supplier's ID
     */
    public int getSupplierID()
    {
        return supplierID;
    }
    
    /**
     * returns the supplier company's name
     * 
     * @return the supplier company's name
     */
    public String getCompanyName()
    {
        return companyName;
    }
    
    /**
     * returns the supplier's address
     * 
     * @return the supplier's address
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * returns the supplier's sales contact
     * 
     * @return the supplier's sales contact
     */
    public String getSalesContact()
    {
        return salesContact;
    }
    
    /**
     * returns a string representation of the information stored in supplier.
     * 
     * @return a string representation of the information stored in supplier.
     */
    public String toString()
    {
        return "Supplier with ID: " + supplierID + ", " + companyName + ". Located at " + address + " (Sales Contact: " + salesContact + ")."; 
    }
}
