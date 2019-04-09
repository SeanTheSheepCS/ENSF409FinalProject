package server.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.model.Item;

public class FindItemInDatabase extends DatabaseConnector {

	public FindItemInDatabase() {
		super();
	}

	/**
	 * finds an item based on id.
	 * 
	 * @param id of item.
	 * @return item if exists, else null.
	 */
	public Item getInfo(String id) {
		try {
			String query = "SELECT * FROM item WHERE itemID=(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			try {
				pStat.setInt(1, Integer.parseInt(id));

				ResultSet rs = pStat.executeQuery();
				if (rs.next()) {
					Item newItem = new Item(rs.getInt("itemID"), rs.getString("itemName"), rs.getInt("itemQuantity"),
							rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
					getSupplierForItem(newItem);

					return newItem;
				}
				return null;
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
