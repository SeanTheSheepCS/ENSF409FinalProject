package server.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * class that extends Database Connector to login/credential checking in database functionality. 
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since April 4th 2019
 */
public class LoginValidator extends DatabaseConnector{
	public LoginValidator() {
		super();
	}
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
}
