package ir;

import java.util.ArrayList;
import java.util.List;

import semantic.SymbolTable;

public class Method {
	List<Quad> instructions = null;
	private String name = null;
	private List<String> parms;
	private List<String> localVars;
	private SymbolTable symbolTable = null;
	private ControlFlowGraph cfg = null;
	public Method(String name,SymbolTable symbolTable){
		this.name = name;
		this.symbolTable = symbolTable;
		instructions = new ArrayList<Quad>();
		parms = new ArrayList<String>();
		localVars = new ArrayList<String>();
	}
	public void addParm(String strValue) {
		parms.add(strValue);
	}
	public void addLocalVar(String strValue) {
		localVars.add(strValue);
	}
	public List<Quad> getInstructions(){
		return instructions;
	}
	public void addIns(Quad quad) {
		instructions.add(quad);
	}
	public ControlFlowGraph getCFG(){
		if (cfg == null){
			cfg = new ControlFlowGraph(this);
		}
		return cfg;
	}
	public String getName(){
		return name;
	}
}
