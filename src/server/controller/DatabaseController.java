package server.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.model.Item;
import common.model.Supplier;
import server.model.Inventory;
import server.model.Shop;

public class DatabaseController implements JDBCredentials {
	private Connection connectionToDatabase;
	private Shop shop;

	public DatabaseController() {
		declareShop();
		initializeConnection();
	}

	public void initializeConnection() {
		try {
			String url = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;
			connectionToDatabase = DriverManager.getConnection(url, USERNAME, PASSWORD);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

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

	protected void readSupplierFileAndSend() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("suppliers.txt"));
			try {
				String line;
				while (true) {

					line = reader.readLine();

					String[] tokens = line.split(";");
					int supplierID = Integer.parseInt(tokens[0]);
					String companyName = tokens[1];
					String address = tokens[2];
					String salesContact = tokens[3];
//INSERT NEEDS supplierID, companyName, address, salesContact
					insertSupplierPreparedStatement(supplierID, companyName, address, salesContact);
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

	private synchronized void insertSupplierPreparedStatement(int supplierID, String companyName, String address,
			String salesContact) {
		try {
			String query = "INSERT INTO supplier (supplierID, supplierName, supplierAddress, supplierSalesContact) values (?,?,?,?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setInt(1, supplierID);
			pStat.setString(2, companyName);
			pStat.setString(3, address);
			pStat.setString(4, salesContact);
			int rowCount = pStat.executeUpdate();
			System.out.println("row Count = " + rowCount);
			pStat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeConnection() {
		try {
			connectionToDatabase.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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

	public String readDataFromDatabase() {
		return null;

	}

	private void declareShop() {
		// NOTE: add implementation for database
		ArrayList<Item> itemList = new ArrayList<Item>();
		ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
		Inventory inventory = new Inventory(itemList);
		shop = new Shop(inventory, supplierList);

	}

	public void decreaseItemQuantity(String itemId, String quantity) {

	}

	/*
	 * admin - admin joe - customer bob - hunter2
	 */
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isValidLogin(String username, String password) {
		return true;

	}

	public boolean isAdmin(String username, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	public ArrayList<Item> search(String search) {
		try {
			
			String query = "SELECT * FROM item WHERE itemName LIKE '%or%'";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			ArrayList<Item> itemList=new ArrayList<Item>();
			int itemID;
			String itemName; 
			double itemPrice;
			int itemSupplierID;
			int itemQuantity;
			ResultSet rs = pStat.executeQuery();
			while (rs.next()) {
				itemID = rs.getInt("itemID");
				itemName=rs.getString("itemName");
				itemPrice=rs.getDouble("itemPrice");
				itemSupplierID=rs.getInt("itemSupplierID");
				itemQuantity=rs.getInt("itemQuantity");
				Item newItem=new Item(itemID,itemName,itemQuantity,itemPrice,itemSupplierID);
				itemList.add(newItem);
			}
			pStat.close();
			return itemList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Item getInfo(String id) {
		try {
			String query = "SELECT * FROM item WHERE itemID=(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setInt(1, Integer.parseInt(id));
			int itemID;
			String itemName; 
			double itemPrice;
			int itemSupplierID;
			int itemQuantity;
			ResultSet rs = pStat.executeQuery();
			if(rs.next()) {
				itemID = rs.getInt("itemID");
				itemName=rs.getString("itemName");
				itemPrice=rs.getDouble("itemPrice");
				itemSupplierID=rs.getInt("itemSupplierID");
				itemQuantity=rs.getInt("itemQuantity");
				Item newItem=new Item(itemID,itemName,itemQuantity,itemPrice,itemSupplierID);
				pStat.close();
				return newItem;
			}
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Item> getAllItems() {

		try {
			String query = "SELECT * FROM item";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			ArrayList<Item> itemList=new ArrayList<Item>();
			int itemID;
			String itemName; 
			double itemPrice;
			int itemSupplierID;
			int itemQuantity;
			ResultSet rs = pStat.executeQuery();
			while (rs.next()) {
				itemID = rs.getInt("itemID");
				itemName=rs.getString("itemName");
				itemPrice=rs.getDouble("itemPrice");
				itemSupplierID=rs.getInt("itemSupplierID");
				itemQuantity=rs.getInt("itemQuantity");
				Item newItem=new Item(itemID,itemName,itemQuantity,itemPrice,itemSupplierID);
				itemList.add(newItem);
			}
			pStat.close();
			return itemList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		DatabaseController databaseController = new DatabaseController();
		databaseController.initializeConnection();
		System.out.println(databaseController.getInfo("1000").toString());
	}
}
