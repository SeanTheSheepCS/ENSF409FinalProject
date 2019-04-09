package server.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.model.Item;
/**
 * class that extends Database Connector to provide getting all items in database functionality. 
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since April 4th 2019
 */
public class GetAllItemsInDatabase extends DatabaseConnector {

	public GetAllItemsInDatabase() {
		super();
	}

	/**
	 * getAllItems gets every item in database.
	 * 
	 * @return Arraylist of items in database.
	 */
	public ArrayList<Item> getAllItems() {

		try {
			String query = "SELECT * FROM item";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			try {
				ArrayList<Item> itemList = new ArrayList<Item>();

				ResultSet rs = pStat.executeQuery();
				while (rs.next()) {
					Item newItem = new Item(rs.getInt("itemID"), rs.getString("itemName"), rs.getInt("itemQuantity"),
							rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
					getSupplierForItem(newItem);
					itemList.add(newItem);
				}

				return itemList;
			} finally {
				pStat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
