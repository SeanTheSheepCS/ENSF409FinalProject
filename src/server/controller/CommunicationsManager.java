package server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import common.model.Item;

/**
 * CommunicationsManager manages communications through a socket, it sends
 * strings and serializable items through to the other side.
 * 
 * @author: Jean-David Rousseau
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
	 * -stringToSocket
	 * 
	 * -stringFromSocket
	 * 
	 * boolean isStopped is used as a signal to terminate the thread.
	 * 
	 * DatabaseController databaseControl is used to handle instructions sent from
	 * this class.
	 */
	private ObjectOutputStream objectToSocket;
	private ObjectInputStream objectFromSocket;
	private PrintWriter stringToSocket;
	private BufferedReader stringFromSocket;
	private boolean isStopped;
	private DatabaseController databaseControl;
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
		databaseControl = new DatabaseController();
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
	 * *QUIT -> sends QUIT and stops thread. * ->does nothing if empty.
	 * 
	 * When sending multiple objects it sends TASKINPROGRESS, folllowed by item
	 * until last one where it sends TASKCOMPLETE then last item.
	 */
	public void interpretMessageFromClient() {
		try {
			String line = stringFromSocket.readLine();
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

				if (databaseControl.isValidLogin(username, password)) {
					if (databaseControl.isAdmin(username, password)) {
						sendMessageToClient("ADMIN");
					} else {
						sendMessageToClient("CUSTOMER");
					}
				} else {
					sendMessageToClient("INVALID");
				}
				break;
			case "LOGOUT":
				break;
			case "SEARCH":
				// Fix to find better algorithm to account for spaces
				String[] queryInfo = parseNQuery(words, 2);
				if (queryInfo.length < 2) {
					sendMessageToClient("INVALIDQUERY");
					break;
				}
				String query = queryInfo[1];
				Item[] itemList = databaseControl.search(query);
				for (int i = 0; i < itemList.length - 1; i++) {
					sendMessageToClient("TASKINPROGRESS");
					sendItem(itemList[i]);
				}
				sendMessageToClient("TASKCOMPLETE");
				sendItem(itemList[itemList.length - 1]);
				break;
			case "REQUESTITEMINFO":
				String[] idInfo = parseNQuery(words, 2);
				if (idInfo.length < 2) {
					sendMessageToClient("INVALIDQUERY");
					break;
				}
				String id = idInfo[1];
				Item item = databaseControl.getInfo(id);
				sendMessageToClient("SENDINGITEM");
				sendItem(item);
				break;
			case "GETALLITEMS":
				try {
					ArrayList<Item> allItems = databaseControl.getAllItems();
					for (int i = 0; i < allItems.size() - 1; i++) {
						sendMessageToClient("TASKINPROGRESS");
						sendItem(allItems.get(i));
					}
					sendMessageToClient("TASKCOMPLETE");
					sendItem(allItems.get(allItems.size() - 1));
				} catch (NullPointerException e) {
					sendMessageToClient("TASKCOMPLETE");
					sendItem(null);
				}
				break;
			case "DECQUANTITY":
				String[] itemInfo = parseNQuery(words, 3);
				if (itemInfo.length < 3) {
					sendMessageToClient("INVALIDQUERY");
					break;
				}
				String itemId = itemInfo[1];
				String quantity = itemInfo[2];
				databaseControl.decreaseItemQuantity(itemId, quantity);
				break;
			case "QUIT":
				sendMessageToClient("QUIT");
				isStopped = true;
				break;
			default:
				break;
			}
		} catch (IOException e) {
			System.err.println("Connection with client was terminated in controllerRun");
			isStopped = true;
		}
	}

	/**
	 * closes all IO streams.
	 */
	private void closeAllStreams() {
		try {
			objectToSocket.close();
			objectFromSocket.close();
			stringToSocket.close();
			stringFromSocket.close();
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
		String[] parse = new String[n];
		int counter = 0;
		for (int i = 0; i < original.length; i++) {
			if (original[i] != "") {
				parse[counter] = original[i];
				counter++;
				if (counter >= n) {
					break;
				}
			}
		}
		return parse;
	}

	/**
	 * sends item to socket.
	 * 
	 * @param item is object to be sent, must be serializable.
	 */
	private void sendItem(Item item) {
		try {
			objectToSocket.writeObject(item);
			//objectToSocket.reset();
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
		stringToSocket.println(message);
		stringToSocket.flush();
	}

	/**
	 * opens IO streams based on socket provided in c-tor.
	 */
	private void assignStreams() {
		try {
			objectToSocket = new ObjectOutputStream(socket.getOutputStream());
			objectFromSocket = new ObjectInputStream(socket.getInputStream());
			stringFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stringToSocket = new PrintWriter((socket.getOutputStream()), true);
		} catch (IOException e) {
			System.err.println("Failed to open streams in controllerRun");
			isStopped = true;
		}

	}
}
