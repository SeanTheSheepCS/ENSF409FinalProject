package server.controller;

import java.sql.Connection;

import server.model.Item;

public class DatabaseController {
	private Connection connectionToDatabase;

	public DatabaseController() {

	}

	public String readDataFromDatabase() {
		return null;

	}

	public void decreaseItemQuantity(String itemId, String quantity) {

	}

	public boolean isValidLogin(String username, String password) {
		return false;

	}

	public Connection getConnectionToDatabase() {
		return connectionToDatabase;
	}

	public void setConnectionToDatabase(Connection connectionToDatabase) {
		this.connectionToDatabase = connectionToDatabase;
	}

	public boolean isAdmin(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	public Item[] search(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public Item getInfo(String id) {
		// TODO Auto-generated method stub
		return null;
	}
}
