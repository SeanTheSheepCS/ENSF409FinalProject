package server.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import common.model.Item;
/**
 * SearchTest is a testing class for DatabaseConnector
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since April 8th 2019
 */
public class SearchTest {

	@Test
	public void test() {

		DatabaseConnector dbc = new DatabaseConnector();
		dbc.initializeConnection();
		try {
			ArrayList<String> queries = new ArrayList<String>();

			queries.add("its");
			ArrayList<Item> list1 = dbc.search(queries);
			ArrayList<Item> list2 = dbc.search(queries);

			for (int i = 0; i < list1.size(); i++) {
				assertEquals(list1.get(i).toString(), list2.get(i).toString());
			}

			queries.add("1");
			list2 = dbc.search(queries);

			assertNotEquals(list1.size(), list2.size());

			queries = null;
			list2 = dbc.search(queries);
			assertEquals(list2,null);

			list1 = dbc.search(queries);
			assertEquals(list2,list1);
			dbc.closeConnection();

		} catch (Exception e) {
			dbc.closeConnection();
			e.printStackTrace();
			assertEquals(1,2);
		}
	}

}
