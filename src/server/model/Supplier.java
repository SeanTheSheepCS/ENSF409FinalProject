package server.model;

import java.io.Serializable;

/**
 * The supplier for an item in the tool shop application, made ClientServer
 * compatible.
 * 
 * @author Sean Kenny
 * @version 1.0
 * @since March 30th 2019
 * @author Jean-David Rousseau
 *
 */
public class Supplier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 834856632876385958L;
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
	public Supplier() {
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
	public void setSupplierID(int supplierID) {
		this.supplierID = supplierID;
	}

	/**
	 * sets the company name of the supplier
	 * 
	 * @param companyName the new company name
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * sets the address of the supplier
	 * 
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * sets the sales contact of the supplier
	 * 
	 * @param salesContact the new sales contact
	 */
	public void setSalesContact(String salesContact) {
		this.salesContact = salesContact;
	}

	/**
	 * @return the supplier's ID
	 */
	public int getSupplierID() {
		return supplierID;
	}

	/**
	 * @return the supplier company's name
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @return the supplier's address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the supplier's sales contact
	 */
	public String getSalesContact() {
		return salesContact;
	}

	/**
	 * @return a string representation of the information stored in supplier.
	 */
	@Override
	public String toString() {
		return "Supplier with ID: " + supplierID + ", " + companyName + ". Located at " + address + " (Sales Contact: "
				+ salesContact + ").";
	}
}
