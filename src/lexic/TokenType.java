package lexic;

public enum TokenType {
	ENDFILE,ERROR,
    /* reserved words */
    IF,ELSE,INT,RETURN,VOID,WHILE,
    /* multicharacter tokens */
    ID,NUM,
    /* special symbols:+ - * / < <= > >= == != = ; , ( ) [ ] { } /* */
    PLUS,MINUS,MULT,OVER,LT,LE,GT,GE,EQ,NE,ASSIGN,SEMI,COMMA,LPAREN,RPAREN,LSQU,RSQU,LBRACE,RBRACE
}
