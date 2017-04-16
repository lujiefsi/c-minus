package codegen;

import semantic.Symbol;
import semantic.SymbolTable;
import grammar.NodeType;
import grammar.TreeNode;

public class AstToNasm {
	NasmCode nc = null;
	int labelno = 0;
	int label0;
	int label1;
	private SymbolTable globalSymbolTable; 
	private SymbolTable currentSymbolTable;
	private long currentFun=0;
	public AstToNasm(String file,SymbolTable globalSymbolTable) {
		this.globalSymbolTable = globalSymbolTable;
		nc = new NasmCode(file);
	}
	public void genCode(TreeNode node,boolean signal) {
		if (node == null){
			return;
		}
		TreeNode t = null;
		switch( node.nodeType ) {
			case PROGRAM:
				//TODO:rename the var
				globalVars(node);
				t = node.C0;
				while( t != null ) {
					genCode(t,signal);
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
				currentSymbolTable = globalSymbolTable.lookUp(node.strValue).symbolTable;
				nc.code_start_func(currentFun);
				genCode(node.C2,signal);
				break;
			case FUNCOMPUND:
				genCode(node.C0,signal);
				genCode(node.C1,signal);
				nc.code_end_func(currentFun);
				break;
			case IFSTMT:
				label0 = creat_label();
				genCode(node.C0,true);
				nc.code_pop(1);
				nc.code_test_condition(1,0,label0);
				break;
			case EQ:
				genCode(node.C0,true);
				genCode(node.C1,true);
				binaryCode(1,2,NodeType.EQ,signal);
				break;
			case VAR:
				Symbol symbol = currentSymbolTable.lookUp(node.strValue);
				if (symbol == null){
					symbol = globalSymbolTable.lookUp(node.strValue);
				}
				if (signal){
					nc.code_push_ind(symbol.offset);
				}
				break;
			case CONST:
				nc.code_push_cons(node.numValue);
		default:
			break;
		}
	}
	private void binaryCode(int reg1,int reg2, NodeType eq,boolean signal) {
		nc.code_pop(reg2);
		nc.code_pop(reg1);
		nc.code_op_binary(reg1,reg2,NodeType.EQ);
		if (signal){
			nc.code_push_reg(1,false);
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
	int creat_label(){
		return ++labelno;
	}
}
