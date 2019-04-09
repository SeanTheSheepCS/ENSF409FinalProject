package server.model;

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
import common.model.OrderLine;

/*
 * BUGS/FEATURES to fix/finish:
 * ErrorChecking -> more
 * Close Streams properly
 * DONE decreaseItemQuantity needs to reduce item quantity in SQL
 * DONE createNewOrderline needs completion
 * DONE change getInfo,getAllItems to be 1 line to initialize item instead of 6.
 * DONE get orders from database.  
 * Open/Closed Principle
 * Synchronized methods
 */
/**
 * DatabaseConnector connects to a database and asks/receives information from
 * that database.
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since March 30th 2019
 */
public class DatabaseConnector implements JDBCredentials {
	private Connection connectionToDatabase;

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
			try {
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
				return false;
			} finally {
				pStat.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

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
				try {
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
					return false;
				} finally {
					pStat.close();
				}

			}
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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

	/**
	 * sets supplier for item provided.
	 * 
	 * @param item is item to set supplier on.
	 */
	private void getSupplierForItem(Item item) {

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
	 * retrieves all orders from database.
	 * 
	 * @return ArrayList<OrderLine> which is all orders in database.
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

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
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
