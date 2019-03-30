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

	private void declareShop() {
		//NOTE: add implementation for database
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
				if (words.length < 3) {
					break;
				}
				String username = null, password = null;
				for (int i = 1; i < words.length; i++) {
					if (words[i] != "") {
						if (username == null) {
							username = words[i];
						} else if (password == null) {

						}
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendItem(Item item) {

	}

	public void sendMessageToClient(String message) {

	}

}
