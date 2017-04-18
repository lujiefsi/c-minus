package semantic;

import grammar.NodeType;

public class Symbol {
	public String ID; //The lexeme
	public NodeType entryType; //variable, array, etc.
	public DataType dataType; //INT or VOID
	public int blockLevel; //The nesting level: 0 is unnested, 1 is a single level deep, etc.
	public NodeType returnType; //For functions: INT or VOID
	public int arrayMax; //The size of an array
	public SymbolTable symbolTable;
	/*use in semantic*/
	public int offset = 0;
}
