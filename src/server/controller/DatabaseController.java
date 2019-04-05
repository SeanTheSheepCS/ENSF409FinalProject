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
import server.model.OrderLine;
import server.model.Shop;

/*
 * ErrorChecking -> more
 * Close Streams properly
 */
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
//Fix for leaks
		try {
			String query = "SELECT * FROM users WHERE username =(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setString(1, username);
			ResultSet rs = pStat.executeQuery();
			if (rs.next()) {
				String passwordReceived = rs.getString("password");
				PasswordDecoder decode = new PasswordDecoder(password, passwordReceived);
				Boolean isValid = decode.decodePassword();

				if (isValid) {
					pStat.close();
					return true;
				}
			}
			pStat.close();
			return false;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;

	}

	public boolean isAdmin(String username, String password) {
		try {
			if (isValidLogin(username, password)) {
				String query = "SELECT * FROM users WHERE username =(?)";
				PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
				pStat.setString(1, username);
				ResultSet rs = pStat.executeQuery();
				if (rs.next()) {
					int admin = rs.getInt("isAdmin");
					pStat.close();
					if (admin == 0) {
						return false;
					} else {
						return true;
					}
				}
				pStat.close();
				return false;
			}
			return false;

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;

	}

	public void decreaseItemQuantity(String itemId, String quantity) {
		try {
			String query = "SELECT * FROM item WHERE itemID=(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setInt(1, Integer.parseInt(itemId));
			ResultSet rs = pStat.executeQuery();
			if (rs.next()) {
				int currentQuantity = rs.getInt("itemQuantity");
				checkNewOrder(currentQuantity, quantity);
			}
			pStat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	private void checkNewOrder(int currentQuantity, String quantity) {
		int quantityToOrder = currentQuantity -  Integer.parseInt(quantity);
		if(quantityToOrder < 40) {
			OrderLine orderLine = shop.createNewOrder(quantityToOrder);
			createNewOrderLineInDatabase(orderLine);
		}
	}
	private synchronized void createNewOrderLineInDatabase(OrderLine orderline) {
		try {
			String query = "INSERT INTO orderline (orderDate, orderlineID, orderlineItemDescription, orderlineQuantity, orderlineSupplier) values (?,?,?,?,?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			//pStat.setInt(1, Date);
			//pStat.setString(2, nextIDAvailable);
			pStat.setString(3, orderline.getItem().getToolName());
			pStat.setInt(4, orderline.getQuantity());
			pStat.setString(5, orderline.getItem().getSupplier().getCompanyName());
			int rowCount = pStat.executeUpdate();
			System.out.println("row Count = " + rowCount);
			pStat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Item> search(String search) {
		try {

			String query = "SELECT * FROM item WHERE itemName LIKE (?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setString(1, "%" + search + "%");
			ArrayList<Item> itemList = new ArrayList<Item>();
			Item newItem;
			ResultSet rs = pStat.executeQuery();
			while (rs.next()) {
				newItem = new Item(rs.getInt("itemID"), rs.getString("itemName"), rs.getInt("itemQuantity"),
						rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
				getSupplierForItem(newItem);
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
			if (rs.next()) {
				itemID = rs.getInt("itemID");
				itemName = rs.getString("itemName");
				itemPrice = rs.getDouble("itemPrice");
				itemSupplierID = rs.getInt("itemSupplierID");
				itemQuantity = rs.getInt("itemQuantity");
				Item newItem = new Item(itemID, itemName, itemQuantity, itemPrice, itemSupplierID);
				getSupplierForItem(newItem);
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
			ArrayList<Item> itemList = new ArrayList<Item>();
			int itemID;
			String itemName;
			double itemPrice;
			int itemSupplierID;
			int itemQuantity;
			ResultSet rs = pStat.executeQuery();
			while (rs.next()) {
				itemID = rs.getInt("itemID");
				itemName = rs.getString("itemName");
				itemPrice = rs.getDouble("itemPrice");
				itemSupplierID = rs.getInt("itemSupplierID");
				itemQuantity = rs.getInt("itemQuantity");
				Item newItem = new Item(itemID, itemName, itemQuantity, itemPrice, itemSupplierID);
				getSupplierForItem(newItem);
				itemList.add(newItem);
			}
			pStat.close();
			return itemList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void getSupplierForItem(Item item) {

		try {
			String query = "SELECT * FROM supplier WHERE supplierID =(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setInt(1, item.getSupplier().getSupplierID());
			ResultSet rs = pStat.executeQuery();
			if (rs.next()) {
				item.getSupplier().setAddress(rs.getString("supplierAddress"));
				item.getSupplier().setCompanyName(rs.getString("supplierName"));
				item.getSupplier().setSalesContact(rs.getString("supplierSalesContact"));
			}
			pStat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getOrdersFromDatabase() {

	}

	private void closeConnection() {
		try {
			connectionToDatabase.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void declareShop() {
		// NOTE: add implementation for database
		ArrayList<Item> itemList = new ArrayList<Item>();
		ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
		Inventory inventory = new Inventory(itemList);
		shop = new Shop(inventory, supplierList);

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

	public static void main(String[] args) {
		DatabaseController databaseController = new DatabaseController();
		databaseController.initializeConnection();
		ArrayList<Item> list = databaseController.search("its");
		for (Item e : list) {
			System.out.println(e);
		}
	}
}
