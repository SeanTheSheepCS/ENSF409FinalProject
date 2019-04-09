package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import common.model.Item;
import common.model.OrderLine;
import server.model.DatabaseConnector;
import server.model.DecreaseItemQuantityInDatabase;
import server.model.FindItemInDatabase;
import server.model.GetAllItemsInDatabase;
import server.model.GetAllOrdersFromDatabase;
import server.model.LoginValidator;
import server.model.SearchDatabase;

/*
 * BUGS/FEATURES to fix/finish:
 * DONE Check if sending an Array of Items through socket is possible. that way it'll be faster. 
 * DONE Search function - search multiple words -> 2^n complexity. 
 * DONE Split interpretMessageFromClient() into multiple functions, that way it'll be much nicer and cleaner. 
 * DONE see all orders
 * Updates to client
 * 
 */
/**
 * CommunicationsManager manages communications through a socket, it sends
 * strings and serializable items through to the other side.
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since March 30th 2019
 */
public class CommunicationsManager implements Runnable {
	/**
	 * socket is communication port this class communicates through,
	 * 
	 * IO streams for objects and strings:
	 * 
	 * -objectToSocket
	 * 
	 * -objectFromSocket
	 * 
	 * boolean isStopped is used as a signal to terminate the thread.
	 * 
	 * DatabaseController databaseControl is used to handle instructions sent from
	 * this class.
	 */
	private ObjectOutputStream objectToSocket;
	private ObjectInputStream objectFromSocket;
	private boolean isStopped;
	private DatabaseConnector databaseControl;
	private Socket socket;

	/**
	 * c-tor for CommunicationsManager
	 * 
	 * constructs:
	 * 
	 * - IO streams based on socket provided
	 * 
	 * - DatabaseController
	 * 
	 * - and sets isStopped to false.
	 * 
	 * @param socket is socket to build IOstreams on.
	 */
	public CommunicationsManager(Socket socket) {
		this.socket = socket;
		assignStreams();
		isStopped = false;
	}

	/**
	 * run implements runnable, it communicates with socket until isStopped turns
	 * true, which then closes all streams.
	 */
	@Override
	public void run() {
		while (true) {
			if (isStopped) {
				closeAllStreams();
				break;
			}
			interpretMessageFromClient();
		}
	}

	/**
	 * Reads 1 line from Client and interprets it.
	 * 
	 * Current recognized messages:
	 * 
	 * *LOGIN -> checks if valid user/password combo, then sends if
	 * ADMIN,CUSTOMER,orINVALID.
	 * 
	 * *LOGOUT -> nothing.
	 * 
	 * *SEARCH -> Searches database and sends all possible items.
	 * 
	 * *REQUESTITEMINFO -> gets info of 1 item in database and sends it.
	 * 
	 * *GETALLITEMS -> sends all items in database.
	 * 
	 * *DECQUANTITY -> decreases quantity of 1 item in database, nothing sent.
	 * 
	 * *GETORDERS -> sends all orders in database
	 * 
	 * *QUIT -> sends QUIT and stops thread.
	 * 
	 * * ->does nothing if empty.
	 */
	public void interpretMessageFromClient() {
		try {
			String line = (String) objectFromSocket.readObject();
			String[] words = line.split(" ");
			switch (words[0]) {
			case "":
				break;
			case "LOGIN":
				String[] userInfo = parseNQuery(words, 3);
				if (userInfo.length < 3) {
					sendMessageToClient("INVALIDQUERY");
					break;
				}
				String username = userInfo[1];
				String password = userInfo[2];

				checkAndSendForCredentials(username, password);
				break;
			case "LOGOUT":
				break;
			case "SEARCH":
				String[] queryInfo = parseNQuery(words, 4);
				if (queryInfo.length <= 1) {
					getAllItems();
					break;
				}
				ArrayList<String> queries = new ArrayList<String>();
				queries.add(queryInfo[1]);
				if (queryInfo.length > 2) {
					queries.add(queryInfo[2]);
					if (queryInfo.length > 3) {
						queries.add(queryInfo[3]);
					}
				}
				searchAndSend(queries);
				break;
			case "REQUESTITEMINFO":
				String[] idInfo = parseNQuery(words, 2);
				if (idInfo.length < 2) {
					sendMessageToClient("INVALIDQUERY");
					break;
				}
				String id = idInfo[1];
				databaseControl = new FindItemInDatabase();
				Item item = ((FindItemInDatabase) databaseControl).getInfo(id);
				sendMessageToClient("SENDINGITEM");
				sendItem(item);
				databaseControl.closeConnection();
				break;
			case "GETALLITEMS":
				getAllItems();
				break;
			case "DECQUANTITY":
				String[] itemInfo = parseNQuery(words, 3);
				if (itemInfo.length < 3) {
					sendMessageToClient("INVALIDQUERY");
					break;
				}
				String itemId = itemInfo[1];
				String quantity = itemInfo[2];
				databaseControl = new DecreaseItemQuantityInDatabase();
				((DecreaseItemQuantityInDatabase) databaseControl).getItemForDecrease(itemId, quantity);
				databaseControl.closeConnection();
				break;
			case "GETORDERS":
				getAllOrders();
				break;
			case "QUIT":
				isStopped = true;
				break;
			default:
				break;
			}
		} catch (IOException e) {
			System.err.println("Connection with client was terminated in controllerRun");
			isStopped = true;
		} catch (ClassNotFoundException e1) {
			System.err.println("Class not found in Communications mangaer");
			isStopped = true;
		} catch (Exception e1) {
			System.err.println("Fatal exception in Communications mangaer");
			isStopped = true;
		}
	}

	/**
	 * gets the orders and sends them.
	 */
	private void getAllOrders() {
		databaseControl = new GetAllOrdersFromDatabase();
		ArrayList<OrderLine> allOrders = ((GetAllOrdersFromDatabase) databaseControl).getOrdersFromDatabase();
		sendOrderLines(allOrders);
		databaseControl.closeConnection();
	}

	/**
	 * sends orders through socket.
	 * 
	 * @param allOrders is arraylist of orders to be sent.
	 */
	private void sendOrderLines(ArrayList<OrderLine> allOrders) {
		try {
			objectToSocket.writeObject(allOrders);
			objectToSocket.reset();
			objectToSocket.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to send Orderline in communicationsManager");
		}
	}

	/**
	 * Calls to check validity of credentials then sends appropriate message about
	 * credentials.
	 * 
	 * @param username to check
	 * @param password to check
	 */
	private void checkAndSendForCredentials(String username, String password) {
		databaseControl = new LoginValidator();
		if (((LoginValidator) databaseControl).isValidLogin(username, password)) {
			if (((LoginValidator) databaseControl).isAdmin(username, password)) {
				sendMessageToClient("ADMIN");
			} else {
				sendMessageToClient("CUSTOMER");
			}
		} else {
			sendMessageToClient("INVALID");
		}
		databaseControl.closeConnection();
	}

	/**
	 * asks for search in database, then sends items to scket..
	 * 
	 * @param queries is search terms.
	 */
	private void searchAndSend(ArrayList<String> queries) {
		databaseControl = new SearchDatabase();
		ArrayList<Item> itemList = ((SearchDatabase) databaseControl).search(queries);
		sendItems(itemList);
		databaseControl.closeConnection();
	}

	/**
	 * getAllItems gets every item in database sends to socket. .
	 */
	private void getAllItems() {
		databaseControl = new GetAllItemsInDatabase();
		ArrayList<Item> allItems = ((GetAllItemsInDatabase) databaseControl).getAllItems();
		sendItems(allItems);
		databaseControl.closeConnection();
	}

	/**
	 * closes all IO streams.
	 */
	private void closeAllStreams() {
		try {
			objectToSocket.close();
			objectFromSocket.close();
		} catch (IOException e) {
			System.err.println("Failed to close all streams in controllerRun");
		}
	}

	/**
	 * Takes a split string and removes all empty strings.
	 * 
	 * @param original is split string provided.
	 * @param n        number of non-empty strings expected.
	 * @return String[] that doesn't contain empty strings of max size n.
	 */
	private String[] parseNQuery(String[] original, int n) {
		ArrayList<String> parse = new ArrayList<String>();
		for (int i = 0; i < original.length; i++) {
			if (original[i] != "") {
				parse.add(original[i]);
				if (parse.size() >= n) {
					break;
				}
			}
		}
		String[] finalized = new String[parse.size()];
		for (int i = 0; i < parse.size(); i++) {
			finalized[i] = parse.get(i);
		}
		return finalized;
	}

	/**
	 * sends item to socket.
	 * 
	 * @param item is object to be sent, must be serializable.
	 */
	private void sendItem(Item item) {
		try {
			objectToSocket.writeObject(item);
			objectToSocket.reset();
			objectToSocket.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to send Item in communicationsManager");
		}
	}

	/**
	 * sends arraylist of items to socket.
	 * 
	 * @param items is arraylist to send
	 */
	private void sendItems(ArrayList<Item> items) {
		try {
			objectToSocket.writeObject(items);
			objectToSocket.reset();
			objectToSocket.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to send Item in communicationsManager");
		}
	}

	/**
	 * sends string to socket.
	 * 
	 * @param message is string to be sent.
	 */
	private void sendMessageToClient(String message) {
		try {
			objectToSocket.writeObject(message);
			objectToSocket.reset();
			objectToSocket.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * opens IO streams based on socket provided in c-tor.
	 */
	private void assignStreams() {
		try {
			objectToSocket = new ObjectOutputStream(socket.getOutputStream());
			objectFromSocket = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.err.println("Failed to open streams in controllerRun");
			isStopped = true;
		}

	}
}
