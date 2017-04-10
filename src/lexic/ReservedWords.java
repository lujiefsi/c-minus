package lexic;

import java.util.HashMap;

public class ReservedWords {
	private static HashMap<String, TokenType> reservedWords = new HashMap<String, TokenType>();
	static {
		reservedWords.put("if", TokenType.IF);
		reservedWords.put("else", TokenType.ELSE);
		reservedWords.put("int", TokenType.INT);
		reservedWords.put("void", TokenType.VOID);
		reservedWords.put("return", TokenType.RETURN);
		reservedWords.put("while", TokenType.WHILE);
	}
	public static TokenType get(String key){
		return reservedWords.get(key);
	}
}
