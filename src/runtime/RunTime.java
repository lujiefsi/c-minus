package runtime;
import grammar.NodeType;
import grammar.TreeNode;
import semantic.Symbol;
import semantic.SymbolTable;
import util.IO;
import util.Process;
public class RunTime {
	private final int VARSIZE = 4;
	TreeNode root;
	SymbolTable globalSymbolTable;
	SymbolTable currentSymbolTable = null;
	int offset = 8;
	int localOffset = -4;
	public RunTime(TreeNode root,SymbolTable symbolTable){
		this.root = root;
		this.globalSymbolTable = symbolTable;
	}
	public void run() {
		new Process(){
			@Override
			public void postProc(TreeNode t) {
				
			}

			@Override
			public void preProc(TreeNode t) {
				if (t == null) return;
				if (t.nodeType.equals(NodeType.PROGRAM)){
					currentSymbolTable = globalSymbolTable;
				}else if (t.nodeType.equals(NodeType.FUNDECL)){
					currentSymbolTable = globalSymbolTable.lookUp(t.strValue).symbolTable;
					offset = 8;
					localOffset = -4;
				}else if (t.nodeType.equals(NodeType.VARDECL)){
					Symbol symbol = globalSymbolTable.lookUp(t.strValue);
					if (symbol==null){
						symbol = currentSymbolTable.lookUp(t.strValue);
						symbol.offset = localOffset;
						localOffset-=VARSIZE;
					}
				}else if (t.nodeType.equals(NodeType.ARRAYDECL)){
					Symbol symbol = currentSymbolTable.lookUp(t.strValue);
					symbol.offset = offset;
					offset+=VARSIZE*symbol.arrayMax;
				}
				
			}

			@Override
			public void finProc(TreeNode t) {
				if (t.nodeType.equals(NodeType.ARRAYPARM)||t.nodeType.equals(NodeType.VARPARM)){
					Symbol symbol = currentSymbolTable.lookUp(t.strValue);
					symbol.offset = offset;
					offset+=VARSIZE;
				}
			}
		}.traverse(root);
	}
}
