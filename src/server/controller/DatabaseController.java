package server.controller;

import java.sql.Connection;

public class DatabaseController {
	private Connection connectionToDatabase;
	public DatabaseController() {
		
	}
	public String readDataFromDatabase() {
		return null;
		
	}
	public void decreaseItemQuantity() {
		
	}
	public boolean validateLogin(String credentials) {
		return false;
		
	}
	public Connection getConnectionToDatabase() {
		return connectionToDatabase;
	}
	public void setConnectionToDatabase(Connection connectionToDatabase) {
		this.connectionToDatabase = connectionToDatabase;
	}
}
