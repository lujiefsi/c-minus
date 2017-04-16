package grammar;

import util.IO;
import lexic.Scan;
import lexic.Token;
import lexic.TokenType;

public class Parser {
	private Token token;
	private Scan scan;
	public Parser(Scan scan) {
		this.scan = scan;
	}
	public TreeNode parse() {
		token = scan.getToken();
		TreeNode t = program();
		if (!token.isType(TokenType.ENDFILE)) {
			System.out.println("ERROR:Code ends before file\n");
		}
		return t;
	}
	private TreeNode program() {
		TreeNode t = new TreeNode();
		t.setType(NodeType.PROGRAM);
		t.C0 = declarationList();
		TreeNode p = t.C0;
		while (!token.isType(TokenType.ENDFILE)){
			TreeNode q = declaration();
			if (q!=null){
				p.sibling = q;
				p = q;
			}
		}
		return t;
	}
	private TreeNode declarationList(){
		TreeNode t = declaration();
		TreeNode p  = t,q;
		while (p!= null){
			q = declaration();
			p.sibling = q;
			p = q;
		}
		return t;
	}
	private TreeNode declaration() {
		if (!token.isType(TokenType.INT)) return null;
		TreeNode t = new TreeNode();
		t.C0=typeSpec();
		t.strValue = token.getToken().toString();
		match(TokenType.ID);
		if (token.isType(TokenType.SEMI)){
			t.setType(NodeType.VARDECL);
			match(TokenType.SEMI);
		}else if (token.isType(TokenType.LSQU)){
			t.setType(NodeType.ARRAYDECL);
			match(TokenType.LSQU);
			t.numValue = Integer.valueOf(token.getToken());
			match(TokenType.NUM);
			match(TokenType.RSQU);
			match(TokenType.SEMI);
		}else if (token.isType(TokenType.LPAREN)){
			t.setType(NodeType.FUNDECL);
			match(TokenType.LPAREN);
			t.C1 = paramlist();
			match(TokenType.RPAREN);
			t.C2 = compound();
			t.C2.setType(NodeType.FUNCOMPUND);
		}
		return t;
	}
	private TreeNode compound() {
		TreeNode t = new TreeNode();
		t.setType(NodeType.COMPUND);
		match(TokenType.LBRACE);
		t.C0 = declarationList();
		t.C1 = statementlist();
		match(TokenType.RBRACE);
		return t;
	}
	private TreeNode statementlist() {
		TreeNode t = statement();
		TreeNode p  = t,q;
		while (p!= null){
			q = statement();
			p.sibling = q;
			p = q;
		}
		return t;
	}
	private TreeNode statement() {
		TreeNode t = null;
		switch (token.getType()){
			case IF:
				t = selectionstmt();
				break;
			case RETURN:
				t = returnstmt();
				break;
			case WHILE:
				t = iterationstmt();
				break;
			case LBRACE:
				t = compound();
				break;
			case ID:
			case SEMI:
			case LPAREN:
				t = expressionstmt();
				break;
			default:
				return null;
			
		}
		return t;
	}
	private TreeNode expressionstmt() {
		TreeNode t = new TreeNode();
		t.setType(NodeType.EXPRESSIONSTMT);
		if (!token.isType(TokenType.SEMI)){
			t.C0 = expression();
		}
		match(TokenType.SEMI);
		return t;
	}
	private TreeNode expression() {
		TreeNode t = simple_exp();
		if (token.isType(TokenType.ASSIGN)){
			TreeNode p = new TreeNode();
			p.setChild(0, t);
			p.setType(NodeType.ASSIGN);
			match(token.getType());
			t = p;
			p.setChild(1, simple_exp());
		}
		return t;
	}
	private TreeNode simple_exp() {//LT,LE,GT,GE,EQ,NE,ASSIGN
		TreeNode t = additive_exp();
		if (token.isType(TokenType.LT)) {
			TreeNode p = new TreeNode();
			p.setChild(0, t);
			p.setType(NodeType.LT);
			match(token.getType());
			p.setChild(1, additive_exp());
			t = p;
		}else if (token.isType(TokenType.LE)){
			TreeNode p = new TreeNode();
			p.setChild(0, t);
			p.setType(NodeType.LE);
			match(token.getType());
			p.setChild(1, additive_exp());
			t = p;
		}else if (token.isType(TokenType.GT)){
			TreeNode p = new TreeNode();
			p.setChild(0, t);
			p.setType(NodeType.GT);
			match(token.getType());
			p.setChild(1, additive_exp());
			t = p;
		}else if (token.isType(TokenType.GE)){
			TreeNode p = new TreeNode();
			p.setChild(0, t);
			p.setType(NodeType.GE);
			match(token.getType());
			p.setChild(1, additive_exp());
			t = p;
		}else if (token.isType(TokenType.EQ)){
			TreeNode p = new TreeNode();
			p.setChild(0, t);
			p.setType(NodeType.EQ);
			match(token.getType());
			p.setChild(1, additive_exp());
			t = p;
		}else if (token.isType(TokenType.NE)){
			TreeNode p = new TreeNode();
			p.setChild(0, t);
			p.setType(NodeType.NE);
			match(token.getType());
			p.setChild(1, additive_exp());
			t = p;
		}
		return t;
	}
	private TreeNode additive_exp() {
		TreeNode t = term();
		while (token.isType(TokenType.PLUS) || token.isType(TokenType.MINUS)) {
			TreeNode p = new TreeNode();
			if (token.isType(TokenType.PLUS)){
				p.setType(NodeType.PLUS);
			}else{
				p.setType(NodeType.MINUS);
			}
			p.setChild(0, t);
			t = p;
			match(token.getType());
			p.setChild(1, term());
		}
		return t;
	}
	private TreeNode term() {
		TreeNode t = factor();
		while (token.isType(TokenType.MULT)||token.isType(TokenType.OVER)){
			TreeNode p = new TreeNode();
			if (token.isType(TokenType.MULT)){
				p.setType(NodeType.MULT);
			}else{
				p.setType(NodeType.OVER);
			}
			p.setChild(0,t);
			t = p;
			match(token.getType());
			p.setChild(1, factor());
		}
		return t;
	}
	private TreeNode factor() {
		TreeNode t = null;
		switch (token.getType()) {
			case NUM:
				t= new TreeNode();
				t.setType(NodeType.CONST);
				t.numValue = Integer.valueOf(token.getToken());
				match(TokenType.NUM);
				break;
			case ID:
				t= new TreeNode();
				t.strValue = token.getToken().toString();
				match(TokenType.ID);
				if (token.isType(TokenType.LPAREN)){
					t.setType(NodeType.CALL);
					t.C0 = args();
				}else if (token.isType(TokenType.LSQU)){
					t.setType(NodeType.ARRAYVAR);
					match(TokenType.LSQU);
					t.C0 = expression();
					match(TokenType.RSQU);
				}else{
					t.setType(NodeType.VAR);
				}
				break;
			case LPAREN:
				match(TokenType.LPAREN);
				t = expression();
				match(TokenType.RPAREN);
				break;
		default:
			break;
		}
		return t;
	}
	private TreeNode args() {
		match(TokenType.LPAREN);
		TreeNode root = null,p=null,q=null;
		while (!token.isType(TokenType.RPAREN)){
			p = expression();
			if (root == null){
				root = p;
			}
			if (q!=null){
				q.sibling = p;
			}
			q = p;
			if (token.isType(TokenType.COMMA)){
				match(TokenType.COMMA);
			}
		}
		match(TokenType.RPAREN);
		return root;
	}
	private TreeNode iterationstmt() {
		TreeNode t = new TreeNode();
		t.setType(NodeType.WHILESTMT);
		match(TokenType.WHILE);
		match(TokenType.LPAREN);
		t.C0 = expression();
		match(TokenType.RPAREN);
		t.C1 = statement();
		return t;
	}
	private TreeNode returnstmt() {
		TreeNode t = new TreeNode();
		t.setType(NodeType.RETURNSTMT);
		match(TokenType.RETURN);
		if (!token.isType(TokenType.SEMI)){
			t.C0 = expression();
		}
		match(TokenType.SEMI);
		return t;
	}

	private TreeNode selectionstmt() {
		TreeNode t = new TreeNode();
		t.setType(NodeType.IFSTMT);
		match(TokenType.IF);
		match(TokenType.LPAREN);
		t.C0 = expression();
		match(TokenType.RPAREN);
		t.C1 = statement();
		if (token.isType(TokenType.ELSE)) {
			match(TokenType.ELSE);
			t.setChild(2, statement());
		}
		return t;
		
	}
	private TreeNode paramlist() {
		if (token.getType().equals(TokenType.VOID)){
			match(TokenType.VOID);
			return null;
		}
		TreeNode t = param(),p,q;
		p = t;
		while (p!=null){
			q = param();
			p.sibling = q;
			p = q;
		}
		return t;
	}
	private TreeNode param() {
		if (token.isType(TokenType.RPAREN)){
			return null;
		}
		TreeNode t = new TreeNode();
		t.C0 = typeSpec();
		t.strValue=token.getToken().toString();
		match(TokenType.ID);
		if (token.isType(TokenType.LSQU)){
			match(TokenType.LSQU);
			match(TokenType.RSQU);
			t.setType(NodeType.ARRAYPARM);
		}else{
			t.setType(NodeType.VARPARM);
			if (token.isType(TokenType.COMMA)){
				match(TokenType.COMMA);
			}
		}
		return t;
	}
	private TreeNode typeSpec() {
		TreeNode t = new TreeNode();
		if (token.isType(TokenType.VOID)){
			match(TokenType.VOID);
			t.setType(NodeType.VOIDTYPESPEC);
		}else if (token.isType(TokenType.INT)){
			match(TokenType.INT);
			t.setType(NodeType.INTTYPESPEC);
		}else{
			IO.err("declaration must have typeSpecifiler");
		}
		return t;
	}
	private void match(TokenType expected) {
		if (token.getType().equals(expected)) {
			token = scan.getToken();
		} else {
			IO.err("ERROR:expect");
		}
	}

}
