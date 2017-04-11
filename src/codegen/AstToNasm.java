package codegen;

import semantic.SymbolTable;
import grammar.NodeType;
import grammar.TreeNode;

public class AstToNasm {
	NasmCode nc = null;
	private SymbolTable globals; 
	private long currentFun=0;
	public AstToNasm(String file) {
		nc = new NasmCode(file);
		globals = new SymbolTable();
	}
	public void genCode(TreeNode node) {
		if (node == null){
			return;
		}
		TreeNode t = null;
		switch( node.nodeType ) {
			case PROGRAM:
				//TODO:rename the var
				globalVars(node);
				t = node.sibling;
				while( t != null ) {
					genCode( t );
					t = t.sibling;
				}
				break;
			case FUNDECL:
				/*if (!nc.code_data_section()){
					nc.code_start_text();
					nc.code_start_func(Util.ELFHash("output"));
					nc.code_func_output();
					nc.code_end_func(Util.ELFHash("output"));
					
					nc.code_start_func(Util.ELFHash("input"));
					nc.code_func_input();
					nc.code_end_func(Util.ELFHash("input"));
				}*/
				currentFun = Util.ELFHash(node.strValue);
				nc.code_start_func(currentFun);
				genCode(node.C2);
				break;
			case FUNCOMPUND:
				genCode(node.sibling);
				nc.code_end_func(currentFun);
		default:
			break;
		}
	}
	private void globalVars(TreeNode node) {
		while (true){
			if (node.nodeType==NodeType.VARDECL){
				nc.code_start_bss();
				nc.code_declare_global_var(node.strValue,1);
			}else if (node.nodeType==NodeType.ARRAYDECL){
				nc.code_start_bss();
				nc.code_declare_global_var(node.strValue,node.numValue);
			}else{
				break;
			}
			node = node.sibling;
		}
	}

	public void close(){
		if (nc!=null){
			nc.close();
		}
	}
}
