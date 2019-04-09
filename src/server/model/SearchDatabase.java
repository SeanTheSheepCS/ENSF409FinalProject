package server.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.model.Item;

public class SearchDatabase extends DatabaseConnector{

	public SearchDatabase() {
		super();
	}
	/**
	 * search finds every item in which the itemname contains the search string
	 * somewhere in it.
	 * 
	 * @param queries is string to be found in item names
	 * @return an arraylist of all items in the database that contain correct search
	 *         parameter. if its empty it returns null.
	 */
	public ArrayList<Item> search(ArrayList<String> queries) {
		ArrayList<Item> itemList = searchForAllKeywords(queries);
		itemList = searchForEachKeywordAndIDs(queries, itemList);
		if (itemList == null || itemList.isEmpty()) {
			return null;
		}
		return itemList;
	}

	private ArrayList<Item> searchForEachKeywordAndIDs(ArrayList<String> queries, ArrayList<Item> itemList) {
		try {
			if (itemList == null || itemList.isEmpty()) {
				itemList = new ArrayList<Item>();
			}

			String statement = "SELECT * FROM item WHERE itemName LIKE (?) OR itemID LIKE (?)";
			for (int i = 1; i < queries.size(); i++) {
				statement += " OR itemName LIKE (?) OR itemID LIKE (?)";
			}

			PreparedStatement pStat = connectionToDatabase.prepareStatement(statement);
			try {
				for (int i = 0; i < queries.size(); i++) {
					pStat.setString(2 * (i + 1) - 1, "%" + queries.get(i) + "%");// odd entries (itemName)
					pStat.setString(2 * (i + 1), "%" + queries.get(i) + "%");// even entries (itemID)
				}

				ResultSet rs = pStat.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("itemID");
					if (itemList.size() == 0) {
						Item newItem = new Item(id, rs.getString("itemName"), rs.getInt("itemQuantity"),
								rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
						getSupplierForItem(newItem);
						itemList.add(newItem);
					} else {
						for (int i = 0; i < itemList.size(); i++) {
							int idToCheck = itemList.get(i).getToolIDNumber();
							// If it exists in the list already don't add
							if (id == idToCheck) {
								break;
							}
							// if it doesn't exist in list, add to list
							else if (id > idToCheck && i >= itemList.size() - 1) {
								Item newItem = new Item(id, rs.getString("itemName"), rs.getInt("itemQuantity"),
										rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
								getSupplierForItem(newItem);
								itemList.add(newItem);
								break;
							}
						}
					}
				}

				if (itemList.isEmpty()) {
					return null;
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

	private ArrayList<Item> searchForAllKeywords(ArrayList<String> queries) {

		try {

			String statement = "SELECT * FROM item WHERE itemName LIKE (?)";
			for (int i = 1; i < queries.size(); i++) {
				statement += " AND itemName LIKE (?)";
			}

			PreparedStatement pStat = connectionToDatabase.prepareStatement(statement);
			try {
				for (int i = 0; i < queries.size(); i++) {
					pStat.setString(i + 1, "%" + queries.get(i) + "%");

				}

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
