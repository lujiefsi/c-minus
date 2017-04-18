package lexic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

enum DfaState {
	START,INASSIGN,INCOMMENT,INNUM,INID,DONE, INLT, INGT, INNE, INOVER,INUNCOMMENT
}
public class Scan {
	private final int EOF = -1;
	private boolean EOF_FLAG = false;
	
	private boolean traceScan = false;
	private Reader fileReader;
	private BufferedReader  in = null;

	private String line="";
	private int linePos = 0;
	private int lineNo = 0;
	
	public Scan(String sourceFile){
		try {
			fileReader = new FileReader(sourceFile);
			in = new BufferedReader(fileReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Token getToken(){
		Token currentToken = new Token("");
		DfaState state = DfaState.START;
		boolean append = false;
		while (state!=DfaState.DONE){
			int c = getNextChar();
			append  = true;
			switch (state){ 
				case START:
					if (Character.isDigit(c)){
						state = DfaState.INNUM;
					}
					else if (Character.isAlphabetic(c)){
						state = DfaState.INID;
					}
					else if (c == '='){
						state = DfaState.INASSIGN;
					}else if (c=='<'){
						state = DfaState.INLT;
					}else if (c=='>'){
						state = DfaState.INGT;
					}else if (c=='!'){
						state = DfaState.INNE;
					}else if (c == '/'){
						state = DfaState.INOVER;
					}
					else if ((c == ' ') || (c == '\t') || (c == '\n')){
						append = false;
					}else{
						state = DfaState.DONE;
						switch (c){ 
							case EOF:
								append = false;
								currentToken.setType(TokenType.ENDFILE);
								break;
							case '+':
								currentToken.setType(TokenType.PLUS);
								break;
							case '-':
								currentToken.setType(TokenType.MINUS);
								break;
							case '*':
								currentToken.setType(TokenType.MULT);
								break;
							case ';':
								currentToken.setType(TokenType.SEMI);
								break;
							case ',':
								currentToken.setType(TokenType.COMMA);
								break;
							case '(':
								currentToken.setType(TokenType.LPAREN);
								break;
							case ')':
								currentToken.setType(TokenType.RPAREN);
								break;
							case '[':
								currentToken.setType(TokenType.LSQU);
								break;
							case ']':
								currentToken.setType(TokenType.RSQU);
								break;
							case '{':
								currentToken.setType(TokenType.LBRACE);
								break;
							case '}':
								currentToken.setType(TokenType.RBRACE);
								break;
							default:
								currentToken.setType(TokenType.ERROR);
								break;
						}
		         }
		         break;
				case INASSIGN:
			         state = DfaState.DONE;
			         if (c == '='){
			        	 currentToken.setType(TokenType.EQ);
			         }else{ /* backup in the input */
			           ungetNextChar();
			           append = false;
			           currentToken.setType(TokenType.ASSIGN);
			         }
			         break;
		       case INCOMMENT:
		         append = false;
		         if (c == EOF){
		        	 state = DfaState.DONE;
		        	 currentToken.setType(TokenType.ENDFILE);
		         }else if (c=='*'){
		        	 state = DfaState.INUNCOMMENT;
		         }
		         break;
		       case INUNCOMMENT:
		    	   if (c=='/'){
		    		   state = DfaState.START;
		    		   currentToken = new Token("");
		    	   }else{
		    		   state = DfaState.INCOMMENT;
		    	   }
		    	   break;
		       case INNUM:
		         if (!Character.isDigit(c)){ /* backup in the input */
		           ungetNextChar();
		           append = false;
		           state = DfaState.DONE;
		           currentToken.setType(TokenType.NUM);
		         }
		         break;
		       case INID:
		         if (!Character.isAlphabetic(c))
		         { /* backup in the input */
		           ungetNextChar();
		           append = false;
		           state = DfaState.DONE;
		           currentToken.setType(TokenType.ID);
		         }
		         break;
		       case INOVER:
		    	   if (c=='*'){
		    		   state = DfaState.INCOMMENT;
			       }else{
			    	   ungetNextChar();
				       append = false;
				       state = DfaState.DONE;
				       currentToken.setType(TokenType.OVER);
			       }
			       break;
		       case INLT:
		    	   state = DfaState.DONE;
		    	   if (c=='='){
		    		   currentToken.setType(TokenType.LE);
		    	   }else{
		    		   ungetNextChar();
		    		   append = false;
		    		   currentToken.setType(TokenType.LT);
		    	   }
		    	   break;
		       case INGT:
		    	   state = DfaState.DONE;
		    	   if (c == '='){
		    		   currentToken.setType(TokenType.GE);
		    	   }else{
		    		   ungetNextChar();
		    		   append = false;
		    		   currentToken.setType(TokenType.GT);
		    	   }
		    	   break;
		       case INNE:
		    	   state = DfaState.DONE;
		    	   if (c!='='){
		    		   System.err.println("unexpected char"+c+" in NE");
		    		   System.exit(1);
		    	   }
		    	   currentToken.setType(TokenType.NE);
		    	   break;
		       case DONE:
		       default: /* should never happen */
		         System.err.println("ERROR:unexpected state"+state);
		         state = DfaState.DONE;
		         currentToken.setType(TokenType.ERROR);
		         break;
		     }
		     if (append){
		    	 currentToken.append((char) c);
		     }
		     if (state == DfaState.DONE&&currentToken.getType().equals(TokenType.ID)){
		    	 if (ReservedWords.get(currentToken.getToken())!=null){
		    		 currentToken.setType(ReservedWords.get(currentToken.getToken()));
		    	 }
		     }
		   }
		   if (traceScan) {
			   System.out.println("\t"+lineNo+":"+currentToken+"\n");
		   }
  		   currentToken.setLineNo(lineNo);
		   return currentToken;
	}
	
	private int getNextChar(){
		if (EOF_FLAG){
			return EOF;
		}
		if (linePos < line.length()){
			return line.charAt(linePos++);
		}
		try {
			linePos = 0;
			lineNo++;
			line = in.readLine();
			if (line == null){
				lineNo--;
				EOF_FLAG = true;
				return EOF;
			}else{
				line = line+'\n';
				return line.charAt(linePos++);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EOF;
	}
	private void ungetNextChar(){
		if (!EOF_FLAG){
			linePos--;
		}
	}
	public void setTraceScan(boolean traceScan){
		this.traceScan = traceScan;
	}
	public int getLineNo(){
		return lineNo;
	}
}