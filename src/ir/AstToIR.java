package ir;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import codegen.Util;
import grammar.NodeType;
import grammar.TreeNode;
import semantic.Symbol;
import semantic.SymbolTable;
import util.Process;

public class AstToIR {
	TreeNode root;
	List<Method> methods = null;
	SymbolTable globalSymbolTable = null;
	private Method currentMethod = null;
	int reg = 0;
	private SymbolTable currentSymbolTable;
	int insID = 0;
	public AstToIR(TreeNode root, SymbolTable globalSymbolTable) {
		this.root = root;
		this.globalSymbolTable = globalSymbolTable;
		methods = new ArrayList<Method>();
	}

	public void genIR(TreeNode node) {
		if (node == null) return;
		TreeNode t = null;
		Operator operator = null;
		Operand operand1 = null;
		Operand operand2 = null;
		Operand operand3 = null;
		Operand operand4 = null;
		switch (node.nodeType) {
		case PROGRAM:
			t = node.C0;
			while (t != null) {
				genIR(t);
				t = t.sibling;
			}
			break;
		case FUNDECL:
			insID = 0;
			reg = 0;
			currentSymbolTable = globalSymbolTable.lookUp(node.strValue).symbolTable;
			currentMethod = new Method(node.strValue,currentSymbolTable);
			methods.add(currentMethod);
			genIR(node.C1);
			genIR(node.C2);
			currentMethod.addIns(new Quad(insID++,new ExitOperator()));
			break;
		case ARRAYDECL:
		case VARDECL:
			if (currentMethod!=null){
				currentMethod.addLocalVar(node.strValue);
			}
			break;
		case VARPARM:
			currentMethod.addParm(node.strValue);
			genIR(node.sibling);
			break;
		case FUNCOMPUND:
			genIR(node.C0);
			genIR(node.C1);
			break;
		case IFSTMT:
			if (node.C0.nodeType.equals(NodeType.EQ)){
				operator = new IfCmpEQFalse();
			}
			operand1 = genExp(node.C0.C0);
			operand2 = genExp(node.C0.C1);
			operand3 = new TargetOperand();
			operand4 = new TargetOperand();
			currentMethod.addIns(new Quad(insID++,operator,operand1,operand2,operand3));
			genIR(node.C1);
			((TargetOperand)operand3).setId(insID);
			if (node.C2!=null){
				currentMethod.addIns(new Quad(insID++,new GoToOperator(),operand4));
				((TargetOperand)operand3).setId(insID);
				genIR(node.C2);
				((TargetOperand)operand4).setId(insID);
			}
			genIR(node.sibling);
			break;
		case RETURNSTMT:
			if (node.C0!=null){
				operator = new ReturnIOperator();
				operand1 = genExp(node.C0);
			}else{
				operator = new ReturnVOperator();
			}
			currentMethod.addIns(new Quad(insID++,operator,operand1,operand2,operand3));
			genIR(node.sibling);
			break;
		case EXPRESSIONSTMT:
			genIR(node.C0);
			genIR(node.sibling);
			break;
		case ASSIGN:
			operator = new AssignOperator();
			operand1 = genExp(node.C0);
			operand2 = genExp(node.C1);
			currentMethod.addIns(new Quad(insID++,operator,operand1,operand2,operand3));
			break;
		default:
			break;
		}
	}

	private Operand genExp(TreeNode node) {
		Operator operator = null;
		Operand operand1 = null;
		Operand operand2 = null;
		Operand operand3 = null;
		if (node.nodeType.equals(NodeType.VAR)){
			return new VarOperand(node.strValue);
		}else if (node.nodeType.equals(NodeType.CONST)){
			return new ConstOperand(node.numValue);
		}else if (node.nodeType.equals(NodeType.CALL)){
			operator = new CallOperator();
			operand1 = new RegisterOperand(reg++);
			operand2 = new ParmListOperand();
			TreeNode t = node.C0;
			while (t!=null){
				((ParmListOperand) operand2).addParm(genExp(t));
				t = t.sibling;
			}
			currentMethod.addIns(new Quad(insID++,operator,operand1,operand2,operand3));
			return operand1;
		}else if (node.nodeType.equals(NodeType.MINUS)){
			operator = new MinusOperator();
			operand1 = new RegisterOperand(reg++);
			operand2 = genExp(node.C0);
			operand3 = genExp(node.C1);
			currentMethod.addIns(new Quad(insID++,operator,operand1,operand2,operand3));
			return operand1;
		}else if (node.nodeType.equals(NodeType.MULT)){
			operator = new MultOperator();
			operand1 = new RegisterOperand(reg++);
			operand2 = genExp(node.C0);
			operand3 = genExp(node.C1);
			currentMethod.addIns(new Quad(insID++,operator,operand1,operand2,operand3));
			return operand1;
		}else if (node.nodeType.equals(NodeType.OVER)){
			operator = new OverOperator();
			operand1 = new RegisterOperand(reg++);
			operand2 = genExp(node.C0);
			operand3 = genExp(node.C1);
			currentMethod.addIns(new Quad(insID++,operator,operand1,operand2,operand3));
			return operand1;
		}else{
			return null;
		}
	}

	public void dumpIR(String file) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			for (Method method : methods) {
				for (Quad quad : method.getInstructions()) {
					fileWriter.write(quad.toString()+'\n');
				}
				method.getCFG();
			}
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<Method> getAllMethods(){
		return methods;
	}

	public void dumpCFG(String prefix) {
		for (Method method:methods){
			String fileName = prefix+"."+method.getName()+".dot";
			try {
				FileWriter fileWriter = new FileWriter(fileName);
				fileWriter.write("digraph AST{\n");
				for (BasicBlock bb:method.getCFG().getAllBB()){
					fileWriter.write(""+bb.id+"[label=\""+bb.toString()+"\"]\n");
				}
				for (BasicBlock bb:method.getCFG().getAllBB()){
					for (BasicBlock bb2:bb.getSuccessors()){
						fileWriter.write(""+bb.id+"->"+bb2.id+"\n");
					}
					
				}
				fileWriter.write("}\n");
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
