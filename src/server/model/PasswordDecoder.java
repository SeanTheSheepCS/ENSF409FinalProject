package server.model;
/**
 * Password Decoder is an interface that is meant to decode a
 * password and check if it is the same and the true password.
 * 
 * @author: Jean-David Rousseau, Sean Kenny
 * @version 1.0
 * @since April 2nd 2019
 */
public interface PasswordDecoder {

	/**
	 * decodePassword determines if provided password is correct when compared to key. 
	 * @param key is good password
	 * @param toDecode is unaltered pw
	 * @return true if correct
	 */
	public boolean decodePassword(String key, String toDecode);
}
