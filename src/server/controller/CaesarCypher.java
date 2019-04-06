package server.controller;

public class CaesarCypher implements PasswordDecoder {

	@Override
	public boolean decodePassword(String key, String toDecode) {
	char[] decoding = new char[toDecode.length()];
	for(int i = 0;i<decoding.length;i++){
		decoding[i] = toDecode.charAt(i);
		decoding[i]--;
	}

	String decoded = new String(decoding);
	if(key.equals(decoded)){
		return true;
	}
	return false;
	}
}