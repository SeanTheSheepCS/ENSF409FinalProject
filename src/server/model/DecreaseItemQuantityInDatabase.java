package server.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.model.Item;
import common.model.OrderLine;

public class DecreaseItemQuantityInDatabase extends DatabaseConnector {

	public DecreaseItemQuantityInDatabase() {
		super();
	}

	/**
	 * gets the item from database, and then calls functions to create an order and
	 * update database as need be.
	 * 
	 * @param itemId
	 * @param quantity
	 */
	public void getItemForDecrease(String itemId, String quantity) {
		try {
			String query = "SELECT * FROM item WHERE itemID=(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			try {
				pStat.setInt(1, Integer.parseInt(itemId));
				ResultSet rs = pStat.executeQuery();
				if (rs.next()) {
					int currentQuantity = rs.getInt("itemQuantity");
					if (new OrderLine(Integer.parseInt(itemId), Integer.parseInt(quantity), currentQuantity)
							.needsOrder()) {
						Item item = new Item(Integer.parseInt(itemId), rs.getString("itemName"),
								rs.getInt("itemQuantity"), rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
						getSupplierForItem(item);
						OrderLine orderline = new OrderLine(item, 50 - (currentQuantity - Integer.parseInt(quantity)));
						updateDatabaseQuantity(itemId, 50);
						createNewOrderLineInDatabase(orderline);
					} else {
						updateDatabaseQuantity(itemId, currentQuantity - Integer.parseInt(quantity));
					}

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
	 * updates database to set quantity.
	 * 
	 * @param itemId      for item to be changed.
	 * @param newQuantity for item.
	 */
	private void updateDatabaseQuantity(String itemId, int newQuantity) {
		try {
			String statement = "UPDATE item SET itemQuantity = (?) WHERE itemID = (?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(statement);
			try {
				pStat.setInt(1, newQuantity);
				pStat.setInt(2, Integer.parseInt(itemId));
				pStat.executeUpdate();
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
	 * adds an orderline to the database.
	 * 
	 * @param orderline to be added.
	 */
	private synchronized void createNewOrderLineInDatabase(OrderLine orderline) {
		try {
			String query = "INSERT INTO orderline (orderlineDate, orderlineItemDescription, orderlineQuantity, orderlineSupplier) values (?,?,?,?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			try {
				pStat.setDate(1, new java.sql.Date(new java.util.Date().getTime()));

				pStat.setString(2, orderline.getItem().getToolName());
				pStat.setInt(3, orderline.getQuantity());
				pStat.setString(4, orderline.getItem().getSupplier().getCompanyName());
				pStat.executeUpdate();
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
}
