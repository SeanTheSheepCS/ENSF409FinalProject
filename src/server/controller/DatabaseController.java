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
 * BUGS/FEATURES to fix/finish:
 * ErrorChecking -> more
 * Close Streams properly
 * decreaseItemQuantity needs to reduce item quantity in SQL
 * createNewOrderline needs completion
 * change getInfo,getAllItems to be 1 line to initialize item instead of 6.
 * get orders from database.  
 * 
 */
/**
 * DatabaseController connects to a database and asks/receives information from
 * that database.
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since March 30th 2019
 */
public class DatabaseController implements JDBCredentials {
	private Connection connectionToDatabase;
	private Shop shop;

	/**
	 * c-tor declares a shop (to be used in buy/decrease quantity functionality).
	 * initializes connection to SQL database.
	 */
	public DatabaseController() {
		declareShop();
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
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * User/PW to test admin - admin
	 * 
	 * joe - customer
	 * 
	 * bob - hunter2
	 */
	/**
	 * isValidLogin determines if username/pw combo exists in the SQL database.
	 * 
	 * @param username of user
	 * @param password of user
	 * @return true if correct un/pw combo.
	 */
	public boolean isValidLogin(String username, String password) {
		try {
			String query = "SELECT * FROM users WHERE username =(?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			pStat.setString(1, username);
			ResultSet rs = pStat.executeQuery();
			if (rs.next()) {
				String passwordReceived = rs.getString("password");
				PasswordDecoder decode = new CaesarCypher();
				Boolean isValid = decode.decodePassword(password, passwordReceived);

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

	/**
	 * isAdmin determines if username/password is a correct combo as well as
	 * checking if it has admin priviledges.
	 * 
	 * @param username of user
	 * @param password of user.
	 * @return true if un/pw combo is correct and user is admin.
	 */
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

	/**
	 * in dev
	 * 
	 * @param itemId
	 * @param quantity
	 */
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

	/**
	 * in dev
	 * 
	 * @param currentQuantity
	 * @param quantity
	 */
	private void checkNewOrder(int currentQuantity, String quantity) {
		int quantityToOrder = currentQuantity - Integer.parseInt(quantity);
		if (quantityToOrder < 40) {
			OrderLine orderLine = shop.createNewOrder(quantityToOrder);
			createNewOrderLineInDatabase(orderLine);
		}
	}

	/**
	 * in dev
	 * 
	 * @param orderline
	 */
	private synchronized void createNewOrderLineInDatabase(OrderLine orderline) {
		try {
			String query = "INSERT INTO orderline (orderDate, orderlineID, orderlineItemDescription, orderlineQuantity, orderlineSupplier) values (?,?,?,?,?)";
			PreparedStatement pStat = connectionToDatabase.prepareStatement(query);
			// pStat.setInt(1, Date);
			// pStat.setString(2, nextIDAvailable);
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

	/**
	 * search finds every item in which the itemname contains the search string
	 * somewhere in it.
	 * 
	 * @param queries is string to be found in item names
	 * @return an arraylist of all items in the database that contain correct search
	 *         parameter. if its empty it returns null.
	 */
	public ArrayList<Item> search(ArrayList<String> queries) {
		try {
			ArrayList<Item> itemList = searchForAllKeywords(queries);
			PreparedStatement pStat;
			for (String query : queries) {
				String statement = "SELECT * FROM item WHERE itemName LIKE (?)";
				pStat = connectionToDatabase.prepareStatement(statement);
				pStat.setString(1, "%" + query + "%");

				Item newItem;
				ResultSet rs = pStat.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("itemID");
					for(int i=0;i<itemList.size();i++) {
						int idToCheck = itemList.get(i).getToolIDNumber();
						//If it exists in the list already don't add
						if(id==idToCheck) {
							break;
						}
						//If it's smaller than current -> then add in current location
						else if(id<idToCheck) {
							newItem = new Item(id, rs.getString("itemName"), rs.getInt("itemQuantity"),
									rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
							getSupplierForItem(newItem);
							addItemToListInPosition(itemList, newItem, i);
							break;
						}
						//if it doesn't exist in list, add to list
						else if  (id>idToCheck && i >= itemList.size()-1) {
							newItem = new Item(id, rs.getString("itemName"), rs.getInt("itemQuantity"),
									rs.getDouble("itemPrice"), rs.getInt("itemSupplierID"));
							getSupplierForItem(newItem);
							itemList.add(newItem);
							break;
						}
					}
				}
			}
			pStat.close();
			if (itemList.isEmpty()) {
				return null;
			}
			return itemList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void addItemToListInPosition(ArrayList<Item> itemList, Item newItem, int i) {
		// TODO Auto-generated method stub
		
	}

	private ArrayList<Item> searchForAllKeywords(ArrayList<String> queries) {
		// TODO Auto-generated method stub
		return null;
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

	/**
	 * getAllItems gets every item in database.
	 * 
	 * @return Arraylist of items in database.
	 */
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

	/**
	 * sets supplier for item provided.
	 * 
	 * @param item is item to set supplier on.
	 */
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

	/**
	 * in dev
	 */
	public void getOrdersFromDatabase() {

	}

	/**
	 * closes connection to database.
	 */
	private void closeConnection() {
		try {
			connectionToDatabase.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * declares a new shop for field.
	 */
	private void declareShop() {
		// NOTE: add implementation for database
		ArrayList<Item> itemList = new ArrayList<Item>();
		ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
		Inventory inventory = new Inventory(itemList);
		shop = new Shop(inventory, supplierList);

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

	/**
	 * inserts a supplier into supplier database. ID must be unique.
	 * 
	 * @param supplierID   of supplier
	 * @param companyName  of supplier
	 * @param address      of supplier
	 * @param salesContact of supplier
	 */
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

	/**
	 * reads suppliers.txt and sends it off to be added to database.
	 */
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

	/*
	 * Testing
	 */
	public static void main(String[] args) {
		DatabaseController databaseController = new DatabaseController();
		databaseController.initializeConnection();
		ArrayList<Item> list = databaseController.search("its");
		for (Item e : list) {
			System.out.println(e);
		}
	}
}
