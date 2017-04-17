package semantic;

import util.IO;
import grammar.NodeType;
import grammar.TreeNode;

public class Analysis {
	private SymbolTable globalSymbolTable = null;
	private TreeNode root = null;
	public Analysis(TreeNode root){
		this.root = root;
	}
	public void analysis(){
		buildSymbolTabel();
	}
	private void buildSymbolTabel() {
		new Process(){
			SymbolTable currentSymbolTable = null;
			@Override
			public void postProc(TreeNode node) {
			}

			@Override
			public void preProc(TreeNode node) {
				if (node == null){
					return;
				}
				Symbol symbol = null;
				switch( node.nodeType ) {
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
						if (symbol == null){
							symbol = new Symbol();
						}else{
							IO.err(node.strValue+" aleardy del\n");
						}
						symbol.entryType = NodeType.FUNDECL;
						symbol.ID = node.strValue;
						symbol.symbolTable = new SymbolTable();
						currentSymbolTable = symbol.symbolTable;
						globalSymbolTable.insert(node.strValue, symbol);
						break;
					case VARPARM:
						symbol = currentSymbolTable.lookUp(node.strValue);
						if (symbol == null){
							symbol = new Symbol();
						}else{
							IO.err(node.strValue+" aleardy del\n");
						}
						symbol.entryType = NodeType.VARPARM;
						symbol.ID = node.strValue;
						symbol.symbolTable = new SymbolTable();
						currentSymbolTable.insert(node.strValue, symbol);
						break;
					case ARRAYPARM:
						symbol = currentSymbolTable.lookUp(node.strValue);
						if (symbol == null){
							symbol = new Symbol();
						}else{
							IO.err(node.strValue+" aleardy del\n");
						}
						symbol.entryType = NodeType.ARRAYPARM;
						symbol.ID = node.strValue;
						symbol.symbolTable = new SymbolTable();
						currentSymbolTable.insert(node.strValue, symbol);
						break;
					case VARDECL:
						symbol = currentSymbolTable.lookUp(node.strValue);
						if (symbol == null){
							symbol = new Symbol();
						}else{
							IO.err(node.strValue+" aleardy del\n");
						}
						symbol.entryType = NodeType.VARDECL;
						symbol.dataType = node.C0.nodeType;
						symbol.ID = node.strValue;
						currentSymbolTable.insert(node.strValue, symbol);
						break;
					case ARRAYDECL:
						symbol = currentSymbolTable.lookUp(node.strValue);
						if (symbol == null){
							symbol = new Symbol();
						}else{
							IO.err(node.strValue+" aleardy del\n");
						}
						symbol.entryType = NodeType.ARRAYDECL;
						symbol.dataType = node.C0.nodeType;
						symbol.arrayMax = node.numValue;
						symbol.ID = node.strValue;
						currentSymbolTable.insert(node.strValue, symbol);
						break;
					case VAR:
					case CALL:
					case ARRAYVAR:
						symbol = currentSymbolTable.lookUp(node.strValue);
						if (symbol == null){
							symbol = globalSymbolTable.lookUp(node.strValue);
							if (symbol == null){
								IO.err(node.strValue+" must del before use\n");
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
	public SymbolTable getGlobalSymbolTable(){
		return globalSymbolTable;
	}
}
