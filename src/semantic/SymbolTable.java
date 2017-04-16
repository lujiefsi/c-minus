package semantic;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable{
	Map<String,Symbol> table = null;
	public SymbolTable(){
		table = new HashMap<String,Symbol>();
	}
	public void insert(String name,Symbol symbol) {
		table.put(name, symbol);
	}
	public Symbol lookUp(String name){
		Symbol ret = table.get(name);
		return ret;
	}
}
