package semantic;

import util.IO;
import grammar.NodeType;
import grammar.TreeNode;

public class Analysis {
	private SymbolTable globalSymbolTable = null;
	private TreeNode root = null;

	public Analysis(TreeNode root) {
		this.root = root;
	}

	public void analysis() {
		buildSymbolTabel();
		typeChecker();
	}
	/*
	 * now we check the type is correct;
	 *
	 * */
	private void typeChecker() {
		new Process() {
			SymbolTable currentSymbolTable = null;
			Symbol currentFun = null;
			Symbol symbol = null;

			@Override
			public void postProc(TreeNode node) {
				switch (node.nodeType) {
				case CONST:
					node.dataType = DataType.INTEGER;
					break;
				case VARDECL:
				case VARPARM:
				case ARRAYDECL:
				case ARRAYPARM:
					symbol = currentFun.symbolTable.lookUp(node.strValue);
					if (symbol.dataType.equals(DataType.VOID)) {
						util.IO.err(node.lineNo
								+ ":void is an invalid type for the variable "
								+ symbol.ID);
					}
					node.dataType = symbol.dataType;
					break;
				case VAR:
				case ARRAYVAR:
					symbol = currentFun.symbolTable.lookUp(node.strValue);
					node.dataType = symbol.dataType;
					break;
				case ASSIGN:
					if (!node.C0.dataType.equals(DataType.INTEGER)){
						util.IO.err(node.lineNo
								+ ":void is an invalid type for left");
					}
					break;
				case LT:
				case LE:
				case GT:
				case GE: 
				case EQ: 
				case NE:
					node.dataType = DataType.BOOLEAN;
					break;
				case OVER:
				case MULT:
				case PLUS:
				case MINUS:
					if (!node.C0.dataType.equals(DataType.INTEGER)){
						util.IO.err(node.lineNo+":operation"+node.nodeType+" doest not  support "+node.C0.dataType);
					}else if (!node.C1.dataType.equals(DataType.INTEGER)){
						util.IO.err(node.lineNo+":operation"+node.nodeType+" doest not  support "+node.C1.dataType);
					}
					node.dataType = DataType.INTEGER;
					break;
				case CALL:
					symbol = globalSymbolTable.lookUp(node.strValue);
					node.dataType = symbol.dataType;
					break;
				case IFSTMT:
				case WHILESTMT:
					if (!node.C0.dataType.equals(DataType.BOOLEAN)){
						util.IO.err(node.lineNo+": if or while test is not boolean");
					}
					break;
				default:
					break;
				}
			}

			@Override
			public void preProc(TreeNode node) {
				if (node == null) {
					return;
				}
				switch (node.nodeType) {
				case PROGRAM:
					currentSymbolTable = globalSymbolTable;
					break;
				case FUNDECL:
					currentFun = globalSymbolTable.lookUp(node.strValue);
					currentSymbolTable = currentFun.symbolTable;
					break;
				default:
					break;
				}
			}

			@Override
			public void finProc(TreeNode t) {
				// TODO Auto-generated method stub

			}

		}.traverse(root);
	}

	private void buildSymbolTabel() {
		new Process() {
			SymbolTable currentSymbolTable = null;

			@Override
			public void postProc(TreeNode node) {
			}

			@Override
			public void preProc(TreeNode node) {
				if (node == null) {
					return;
				}
				Symbol symbol = null;
				switch (node.nodeType) {
				case PROGRAM:
					globalSymbolTable = new SymbolTable();
					currentSymbolTable = globalSymbolTable;
					symbol = new Symbol();
					symbol.entryType = NodeType.FUNDECL;
					symbol.ID = "input";
					globalSymbolTable.insert("input", symbol);
					symbol.entryType = NodeType.FUNDECL;
					symbol.ID = "output";
					globalSymbolTable.insert("output", symbol);
					break;
				case FUNDECL:
					symbol = globalSymbolTable.lookUp(node.strValue);
					if (symbol == null) {
						symbol = new Symbol();
					} else {
						IO.err(node.strValue + " aleardy del\n");
					}
					symbol.entryType = NodeType.FUNDECL;
					if (node.C0.nodeType.equals(NodeType.VOIDTYPESPEC)) {
						symbol.dataType = DataType.VOID;
					} else if (node.C0.nodeType.equals(NodeType.INTTYPESPEC)) {
						symbol.dataType = DataType.INTEGER;
					}
					symbol.ID = node.strValue;
					symbol.symbolTable = new SymbolTable();
					currentSymbolTable = symbol.symbolTable;
					globalSymbolTable.insert(node.strValue, symbol);
					break;
				case VARPARM:
					symbol = currentSymbolTable.lookUp(node.strValue);
					if (symbol == null) {
						symbol = new Symbol();
					} else {
						IO.err(node.strValue + " aleardy del\n");
					}
					symbol.entryType = NodeType.VARPARM;
					if (node.C0.nodeType.equals(NodeType.VOIDTYPESPEC)) {
						symbol.dataType = DataType.VOID;
					} else if (node.C0.nodeType.equals(NodeType.INTTYPESPEC)) {
						symbol.dataType = DataType.INTEGER;
					}
					symbol.ID = node.strValue;
					symbol.symbolTable = new SymbolTable();
					currentSymbolTable.insert(node.strValue, symbol);
					break;
				case ARRAYPARM:
					symbol = currentSymbolTable.lookUp(node.strValue);
					if (symbol == null) {
						symbol = new Symbol();
					} else {
						IO.err(node.strValue + " aleardy del\n");
					}
					symbol.entryType = NodeType.ARRAYPARM;
					if (node.C0.nodeType.equals(NodeType.VOIDTYPESPEC)) {
						symbol.dataType = DataType.VOID;
					} else if (node.C0.nodeType.equals(NodeType.INTTYPESPEC)) {
						symbol.dataType = DataType.INTEGER;
					}
					symbol.ID = node.strValue;
					symbol.symbolTable = new SymbolTable();
					currentSymbolTable.insert(node.strValue, symbol);
					break;
				case VARDECL:
					symbol = currentSymbolTable.lookUp(node.strValue);
					if (symbol == null) {
						symbol = new Symbol();
					} else {
						IO.err(node.strValue + " aleardy del\n");
					}
					symbol.entryType = NodeType.VARDECL;
					if (node.C0.nodeType.equals(NodeType.VOIDTYPESPEC)) {
						symbol.dataType = DataType.VOID;
					} else if (node.C0.nodeType.equals(NodeType.INTTYPESPEC)) {
						symbol.dataType = DataType.INTEGER;
					}
					symbol.ID = node.strValue;
					currentSymbolTable.insert(node.strValue, symbol);
					break;
				case ARRAYDECL:
					symbol = currentSymbolTable.lookUp(node.strValue);
					if (symbol == null) {
						symbol = new Symbol();
					} else {
						IO.err(node.strValue + " aleardy del\n");
					}
					symbol.entryType = NodeType.ARRAYDECL;
					if (node.C0.nodeType.equals(NodeType.VOIDTYPESPEC)) {
						symbol.dataType = DataType.VOID;
					} else if (node.C0.nodeType.equals(NodeType.INTTYPESPEC)) {
						symbol.dataType = DataType.INTEGER;
					}
					symbol.arrayMax = node.numValue;
					symbol.ID = node.strValue;
					currentSymbolTable.insert(node.strValue, symbol);
					break;
				case VAR:
				case CALL:
				case ARRAYVAR:
					symbol = currentSymbolTable.lookUp(node.strValue);
					if (symbol == null) {
						symbol = globalSymbolTable.lookUp(node.strValue);
						if (symbol == null) {
							IO.err(node.strValue + " must del before use\n");
						}
					}
					break;
				default:
					break;
				}
			}

			@Override
			public void finProc(TreeNode t) {
				// TODO Auto-generated method stub

			}

		}.traverse(root);
	}

	public SymbolTable getGlobalSymbolTable() {
		return globalSymbolTable;
	}
}
