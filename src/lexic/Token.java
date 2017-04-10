package lexic;

public class Token {
	private String token = "";
	private TokenType tokeType = null;
	public Token(String token){
		this.token = token;
	}
	public Token(String token,TokenType tokeType){
		this.token = token;
		this.tokeType = tokeType;
	}
	public void setToken(String token){
		this.token = token;
	}
	public String getToken(){
		return token;
	}
	public void setType(TokenType tokeType){
		this.tokeType = tokeType;
	}
	public TokenType getType(){
		return tokeType;
	}
	public void append(char c) {
		token = token + c;
	}
    public boolean isType(TokenType type){
    	return this.tokeType.equals(type);
    }
	@Override
	public int hashCode() {
		return token.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Token) {
			return ((Token) obj).token.equals(token)
					&& ((Token) obj).tokeType.equals(tokeType);
		}
		return false;
	}
	@Override
	public String toString() {
		StringBuilder tokenString = new StringBuilder();
		
		switch (tokeType) {
		case IF:
		case ELSE:
		case INT:
		case RETURN:
		case VOID:
		case WHILE:
			tokenString.append("reserved word: "+token);
			break;
		case PLUS:
			tokenString.append("+");
			break;
		case MINUS:
			tokenString.append("-");
			break;
		case MULT:
			tokenString.append("*");
			break;
		case OVER:
			tokenString.append("/");
			break;
		case LT:
			tokenString.append("<");
			break;
		case LE:
			tokenString.append("<=");
			break;
		case GT:
			tokenString.append(">");
			break;
		case GE:
			tokenString.append(">=");
			break;
		case EQ:
			tokenString.append("==");
			break;
		case NE:
			tokenString.append("!=");
			break;
		case ASSIGN:
			tokenString.append("=");
			break;
		case SEMI:
			tokenString.append(";");
			break;
		case COMMA:
			tokenString.append(",");
			break;
		case LPAREN:
			tokenString.append("(");
			break;
		case RPAREN:
			tokenString.append(")");
			break;
		case LSQU:
			tokenString.append("[");
			break;
		case RSQU:
			tokenString.append("]");
			break;
		case LBRACE:
			tokenString.append("{");
			break;
		case RBRACE:
			tokenString.append("}");
			break;
		case ENDFILE:
			tokenString.append("EOF");
			break;
		case NUM:
			tokenString.append("NUM, val= "+token);
			break;
		case ID:
			tokenString.append("ID, name= "+token);
			break;
		case ERROR:
			tokenString.append("ERROR: "+token);
			break;
		default: /* should never happen */
			tokenString.append("Unknown token: "+token);
		}
		return tokenString.toString();
	}
}
