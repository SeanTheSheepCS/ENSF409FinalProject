package server.controller;
/*
 * BUGS/FEATURES to fix/finish:
 * Convert to Interface and make a class that implemetns called CaesarCypherPassword. 
 */
/**
 * Password Decoder is a class (future interface) that is meant to decode a
 * password and check if it i the same and the true password.
 * 
 * @author: Jean-David Rousseau
 * @version 1.0
 * @since April 2nd 2019
 */
public class PasswordDecoder {
	/**
	 * key is the correct password. 
	 * toDecode is the password to check if it is correct. 
	 */
	private String key;
	private char[] toDecode;

	/**
	 * c-tor assigns password and password to decode, future c-tor will be blank.
	 * 
	 * @param decoded
	 * @param toDecode
	 */
	public PasswordDecoder(String decoded, String toDecode) {
		this.toDecode = toDecode.toCharArray();
		key = decoded;
	}

	/**
	 * function decodes password for a simple caesar cypher.
	 * 
	 * @return true if correct password
	 */
	public boolean decodePassword() {
		for (int i = 0; i < toDecode.length; i++) {
			toDecode[i]--;
		}

		String decoded = new String(toDecode);
		if (key.equals(decoded)) {
			return true;
		}
		return false;
	}
}
