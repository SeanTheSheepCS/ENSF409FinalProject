package server.controller;

import java.sql.Connection;
import java.util.ArrayList;

import server.model.Inventory;
import server.model.Item;
import server.model.Order;
import server.model.Shop;
import server.model.Supplier;

public class DatabaseController {
	private Connection connectionToDatabase;
	private Shop shop;

	public DatabaseController() {
		declareShop();
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

	public boolean isValidLogin(String username, String password) {
		return true;

	}

	public Connection getConnectionToDatabase() {
		return connectionToDatabase;
	}

	public void setConnectionToDatabase(Connection connectionToDatabase) {
		this.connectionToDatabase = connectionToDatabase;
	}

	public boolean isAdmin(String username, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	public Item[] search(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public Item getInfo(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Item[] getAllItems() {
		// TODO Auto-generated method stub
		return null;
	}
}
