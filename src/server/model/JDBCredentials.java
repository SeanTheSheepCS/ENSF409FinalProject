package server.model;
/**
 * Interface that stores info needed to connect to the tooshopitem database. 
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since April 2nd 2019
 */
public interface JDBCredentials {
	static final String HOSTNAME= "127.0.0.1";;
	static final String PORT= "3306";
	static final String DATABASE= "toolshopitem";
	static final String USERNAME="root";
	static final String PASSWORD=null;
}
