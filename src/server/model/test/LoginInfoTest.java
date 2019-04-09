package server.model.test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import server.model.DatabaseConnector;
/**
 * LoginInfoTest is a testing class for DatabaseConnector
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since April 8th 2019
 */
public class LoginInfoTest {

	@Test
	public void test() {
		DatabaseConnector dbc = new DatabaseConnector();
		dbc.initializeConnection();
		try {
			String username = "admin";
			String password = "admin";
			boolean tell = dbc.isValidLogin(username, password);
			assertEquals(tell, true);
			tell = dbc.isAdmin(username, password);
			assertEquals(tell, true);

			username = "bob";
			password = "hunter2";
			tell = dbc.isValidLogin(username, password);
			assertEquals(tell, true);
			tell = dbc.isAdmin(username, password);
			assertEquals(tell, false);

			username = "89788878928347cnf0897nd09uhnjshu2n1s823y807426378y2ns3uh238y239b7rd2037do87yb02c3";
			password = "020029099748767928779823788987cxbu8r2b38r23u2bh2 3u8r238y723ryxbo3urxg uirygbouygx uyg coy2g3cb";
			tell = dbc.isValidLogin(username, password);
			assertEquals(tell, false);
			tell = dbc.isAdmin(username, password);
			assertEquals(tell, false);
		} catch (Exception e) {
			dbc.closeConnection();
			e.printStackTrace();
		}
	}

}
