package codegen;

import semantic.Symbol;
import semantic.SymbolTable;
import grammar.NodeType;
import grammar.TreeNode;

public class AstToNasm {
	NasmCode nc = null;
	int labelno = 0;
	private SymbolTable globalSymbolTable;
	private SymbolTable currentSymbolTable;
	private long currentFun = 0;

	public AstToNasm(String file, SymbolTable globalSymbolTable) {
		this.globalSymbolTable = globalSymbolTable;
		nc = new NasmCode(file);
	}

	public void genCode(TreeNode node, boolean signal) {
		if (node == null) {
			return;
		}
		int label0 = 0;
		int label1 = 0;
		TreeNode t = null;
		Symbol symbol = null;
		switch (node.nodeType) {
		case PROGRAM:
			// TODO:rename the var
			globalVars(node);
			t = node.C0;
			while (t != null) {
				genCode(t, signal);
				t = t.sibling;
			}
			break;
		case FUNDECL:
			if (!nc.code_data_section()) {
				nc.code_start_text();
				nc.code_start_func(Util.ELFHash("output"));
				nc.code_func_output();
				nc.code_end_func(Util.ELFHash("output"));

				nc.code_start_func(Util.ELFHash("input"));
				nc.code_func_input();
				nc.code_end_func(Util.ELFHash("input"));
			}
			currentFun = Util.ELFHash(node.strValue);
			currentSymbolTable = globalSymbolTable.lookUp(node.strValue).symbolTable;
			nc.code_start_func(currentFun);
			genCode(node.C2, signal);
			break;
		case ARRAYDECL:
			symbol = globalSymbolTable.lookUp(node.strValue);
			if (symbol != null) {
				nc.code_start_bss();
				nc.code_declare_global_var(symbol.ID, symbol.arrayMax);
			} else {
				symbol = currentSymbolTable.lookUp(node.strValue);
				nc.code_sub_esp(4 * symbol.arrayMax);
			}
			break;
		case VARDECL:
			if (globalSymbolTable.lookUp(node.strValue) != null) {
				nc.code_start_bss();
				nc.code_declare_global_var(node.strValue, 1);
			} else {
				nc.code_sub_esp(4);
				genCode(node.sibling, false);
			}
			break;
		case FUNCOMPUND:
			genCode(node.C0, signal);
			genCode(node.C1, signal);
			nc.code_end_func(currentFun);
			break;
		case COMPUND:
			genCode(node.C0, signal);
			genCode(node.C1, signal);
			break;
		case EXPRESSIONSTMT:
			genCode(node.C0, signal);
			genCode(node.sibling, signal);
			break;
		case WHILESTMT:
			label0 = creat_label();
			label1 = creat_label();
			nc.code_jmp(label0);
			nc.code_label(label1);
			genCode(node.C1, signal);

			nc.code_label(label0);
			genCode(node.C0, true);
			nc.code_pop(1);
			nc.code_test_condition(1, 1, label1);
			genCode(node.sibling, signal);
			break;
		case IFSTMT:
			label0 = creat_label();
			genCode(node.C0, true);
			nc.code_pop(1);
			nc.code_test_condition(1, 0, label0);
			genCode(node.C1, true);
			if (node.C2 != null) {
				label1 = creat_label();
				nc.code_jmp(label1);
			}
			nc.code_label(label0);
			if (node.C2 != null) {
				genCode(node.C2, true);
				nc.code_label(label1);
			}
			genCode(node.sibling, signal);
			break;
		case EQ:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.EQ, true);
			break;
		case LT:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.LT, true);
			break;
		case LE:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.LE, true);
			break;
		case NE:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.NE, true);
			break;
		case GT:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.GT, true);
			break;
		case GE:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.GE, true);
			break;
		case MINUS:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.MINUS, true);
			break;
		case MULT:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.MULT, true);
			break;
		case OVER:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.OVER, true);
			break;
		case PLUS:
			genCode(node.C0, true);
			genCode(node.C1, true);
			binaryCode(1, 2, NodeType.PLUS, true);
			break;
		case ASSIGN:
			genCode(node.C1, true);
			genCode(node.C0, false);
			nc.code_pop(1);
			nc.code_op_assign(2, 1);
			break;
		case VAR:
			symbol = currentSymbolTable.lookUp(node.strValue);
			if (symbol == null) {
				symbol = globalSymbolTable.lookUp(node.strValue);
				if (signal){
				if (symbol.entryType.equals(NodeType.ARRAYDECL)){
					nc.code_push_global_array(Util.ELFHash(symbol.ID));
				}else{
					nc.code_push_global_var(Util.ELFHash(symbol.ID));
				}
				}else{
					nc.code_lea_global(2, Util.ELFHash(symbol.ID), 0);
				}
			} else {
				if (signal) {
					nc.code_push_ind(symbol.offset);
				} else {
					nc.code_lea_local(2, symbol.offset);
				}
			}
			break;
		case ARRAYVAR:
			genCode(node.C0, true);
			nc.code_pop(1);
			symbol = currentSymbolTable.lookUp(node.strValue);
			if (symbol == null) {
				symbol = globalSymbolTable.lookUp(node.strValue);
				nc.code_get_array_offset(0, 1, 4, 1);
				if (signal) {
					nc.code_push_mem(Util.ELFHash(symbol.ID), 2);
				} else {
					nc.code_lea_global(2, Util.ELFHash(symbol.ID), 2);
				}
			} else {
				if (symbol.offset > 0) {
					nc.code_get_array_offset(symbol.offset, 1, 4, -1);
				} else {
					nc.code_get_array_offset(symbol.offset, 1, 4, 0);
				}
				if (signal) {
					nc.code_push_reg(2, true);
				} else {
					nc.code_move_reg(2, 2);
				}
			}
			break;
		case CONST:
			nc.code_push_cons(node.numValue);
			break;
		case RETURNSTMT:
			genCode(node.C0, true);
			nc.code_pop(1);
			nc.code_end_func(currentFun);
			break;
		case CALL:
			t = node.C0;
			int cnt = 0;
			while (t != null) {
				cnt++;
				genCode(t, true);
				t = t.sibling;
			}
			nc.code_call_func(Util.ELFHash(node.strValue));
			nc.code_clean_stack(cnt * 4);
			if (signal) {
				nc.code_push_reg(1, false);
			}
			break;
		default:
			break;
		}
	}

	private void binaryCode(int reg1, int reg2, NodeType op, boolean signal) {
		nc.code_pop(reg2);
		nc.code_pop(reg1);
		nc.code_op_binary(reg1, reg2, op);
		if (signal) {
			nc.code_push_reg(1, false);
		}
	}

	private void globalVars(TreeNode node) {
		while (true) {
			if (node.nodeType == NodeType.VARDECL) {
				nc.code_start_bss();
				nc.code_declare_global_var(node.strValue, 1);
			} else if (node.nodeType == NodeType.ARRAYDECL) {
				nc.code_start_bss();
				nc.code_declare_global_var(node.strValue, node.numValue);
			} else {
				break;
			}
			node = node.sibling;
		}
	}

	public void close() {
		if (nc != null) {
			nc.close();
		}
	}

	int creat_label() {
		return ++labelno;
	}
}
