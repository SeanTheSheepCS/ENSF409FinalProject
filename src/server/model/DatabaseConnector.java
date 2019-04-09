package server.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.model.Item;

/*
 * BUGS/FEATURES to fix/finish:
 * DONE ErrorChecking -> more
 * DONE Close Streams properly
 * DONE decreaseItemQuantity needs to reduce item quantity in SQL
 * DONE createNewOrderline needs completion
 * DONE change getInfo,getAllItems to be 1 line to initialize item instead of 6.
 * DONE get orders from database.  
 * DONE Open/Closed Principle
 * DONE Synchronized methods
 */
/**
 * DatabaseConnector connects to a database and asks/receives information from
 * that database.
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since March 30th 2019
 */
public abstract class DatabaseConnector implements JDBCredentials {
	protected Connection connectionToDatabase;

	/**
	 * c-tor initializes connection to SQL database.
	 */
	public DatabaseConnector() {

		initializeConnection();
	}

	/**
	 * initializes connection to SQL database using JDBCredentials.
	 */
	public void initializeConnection() {
		try {
			String url = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;
			connectionToDatabase = DriverManager.getConnection(url, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			connectionToDatabase = null;
		} catch (Exception e) {
			e.printStackTrace();
			connectionToDatabase = null;
		}
	}

	/**
	 * sets supplier for item provided.
	 * 
	 * @param item is item to set supplier on.
	 */
	protected void getSupplierForItem(Item item) {

		try {
			String query = "SELECT * FROM supplier WHERE supplierID =(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			try {
				pStat.setInt(1, item.getSupplier().getSupplierID());
				ResultSet rs = pStat.executeQuery();
				if (rs.next()) {
					item.getSupplier().setAddress(rs.getString("supplierAddress"));
					item.getSupplier().setCompanyName(rs.getString("supplierName"));
					item.getSupplier().setSalesContact(rs.getString("supplierSalesContact"));
				}
			} finally {
				pStat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * closes connection to database.
	 */
	public void closeConnection() {
		try {
			connectionToDatabase.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
