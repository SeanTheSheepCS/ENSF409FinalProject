package server.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.model.OrderLine;

public class GetAllOrdersFromDatabase extends DatabaseConnector {

	public GetAllOrdersFromDatabase() {
		super();
	}

	/**
	 * retrieves all orders from database.
	 * 
	 * @return list of orderlines which is all orders in database.
	 */
	public ArrayList<OrderLine> getOrdersFromDatabase() {
		try {
			String query = "SELECT * FROM orderline";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			try {
				ArrayList<OrderLine> orderList = new ArrayList<OrderLine>();
				ResultSet rs = pStat.executeQuery();
				while (rs.next()) {
					OrderLine orderline = new OrderLine(rs.getDate("orderlineDate"), rs.getString("orderlineSupplier"),
							rs.getInt("orderlineQuantity"), rs.getString("orderlineItemDescription"));
					orderList.add(orderline);
				}

				if (orderList.isEmpty()) {
					return null;
				}
				return orderList;
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
