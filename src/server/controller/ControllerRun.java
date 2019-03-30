package server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import server.model.Inventory;
import server.model.Item;
import server.model.Order;
import server.model.OrderLine;
import server.model.Shop;
import server.model.Supplier;

public class ControllerRun implements Runnable {
	private ObjectOutputStream objectToSocket;
	private ObjectInputStream objectFromSocket;
	private PrintWriter stringToSocket;
	private BufferedReader stringFromSocket;

	private DatabaseController databaseControl;
	private Shop shop;

	private Socket socket;

	public ControllerRun(Socket socket) {
		this.socket = socket;
		assignStreams();
		databaseControl = new DatabaseController();
		declareShop();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

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
				String[] queryInfo = parseNQuery(words, 2);
				if (queryInfo.length < 2) {
					break;
				}
				String query = queryInfo[1];
				Item[] itemList = databaseControl.search(query);
				for (int i = 0; i < itemList.length - 1; i++) {
					sendItem(itemList[i]);
					sendMessageToClient("TASKINPROGRESS");
				}
				sendMessageToClient("TASKCOMPLETE");
				sendItem(itemList[itemList.length - 1]);
				break;
			case "REQUESTITEMINFO":
				String[] idInfo = parseNQuery(words, 2);
				if (idInfo.length < 2) {
					break;
				}
				String id = idInfo[1];
				Item item = databaseControl.getInfo(id);
				sendItem(item);
				break;
			case "GETALLITEMS":
				//NOTE: Complete
				break;
			case "DECQUANTITY":
				String[] itemInfo = parseNQuery(words, 3);
				if (itemInfo.length < 3) {
					break;
				}
				String itemId = itemInfo[1];
				String quantity = itemInfo[2];
				databaseControl.decreaseItemQuantity(itemId, quantity);
				break;
			case "QUIT":
				sendMessageToClient("QUIT");
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

	public void sendItem(Item item) {

	}

	public void sendMessageToClient(String message) {

	}

	private void declareShop() {
		// NOTE: add implementation for database
		ArrayList<Item> itemList = new ArrayList<Item>();
		ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
		Order order = new Order();
		Inventory inventory = new Inventory(itemList);
		shop = new Shop(inventory, supplierList);

	}

	private void assignStreams() {
		try {
			objectToSocket = new ObjectOutputStream(socket.getOutputStream());
			objectFromSocket = new ObjectInputStream(socket.getInputStream());
			stringFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stringToSocket = new PrintWriter((socket.getOutputStream()), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
