package server.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertItem extends DatabaseConnector {

	public InsertItem() {
		super();
	}

	/**
	 * inserts an item into database, ID must be unique.
	 * 
	 * @param itemID         of item
	 * @param itemName       of item
	 * @param itemPrice      of item
	 * @param itemSupplierID of item
	 * @param itemQuantity   of item
	 */
	private synchronized void insertItemPreparedStatement(int itemID, String itemName, double itemPrice,
			int itemSupplierID, int itemQuantity) {
		try {
			String query = "INSERT INTO item (itemID, itemName, itemPrice, itemSupplierID, itemQuantity) values (?,?,?,?,?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setInt(1, itemID);
			pStat.setString(2, itemName);
			pStat.setDouble(3, itemPrice);
			pStat.setInt(4, itemSupplierID);
			pStat.setInt(5, itemQuantity);
			int rowCount = pStat.executeUpdate();
			System.out.println("row Count = " + rowCount);
			pStat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads items.txt and sends it off to be added to database.
	 */
	protected void readItemFileAndSend() {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("items.txt"));
			try {
				String line;
				while (true) {

					line = reader.readLine();

					String[] tokens = line.split(";");
					int id = Integer.parseInt(tokens[0]);
					String name = tokens[1];
					int quantity = Integer.parseInt(tokens[2]);
					double price = Double.parseDouble(tokens[3]);
					int supplierId = Integer.parseInt(tokens[4]);
					// INSERT NEEDS ID,name,Price,SupplierID,Quantity
					insertItemPreparedStatement(id, name, price, supplierId, quantity);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("EndofFile");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} finally {
			closeConnection();
		}
	}
}
