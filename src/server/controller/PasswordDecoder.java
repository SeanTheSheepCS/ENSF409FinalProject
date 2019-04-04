package server.controller;

public class PasswordDecoder {
	private String key;
	private char[] toDecode;
	public PasswordDecoder(String decoded, String toDecode) {
		this.toDecode=toDecode.toCharArray();
		key=decoded;
	}
	public boolean decodePassword() {
		for(int i=0;i<toDecode.length;i++) {
			toDecode[i]--;
		}
		
		String decoded = new String(toDecode);
		if(key.equals(decoded)) {
			return true;
		}
		return false;
	}
}
