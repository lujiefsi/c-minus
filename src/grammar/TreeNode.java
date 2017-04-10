package grammar;


public class TreeNode {
	public static int globalIndex = 0;
	public int localIndex = 0;
	private NodeType nodeType;
	/*for declaration:ID name
	 * */
	public String strValue;
	public Integer numValue;
	public TreeNode sibling;
	public TreeNode C0;
	public TreeNode C1;
	public TreeNode C2;
	public TreeNode(){
		localIndex = globalIndex++;
	}
	public NodeType getType(){
		return nodeType;
	}
	public void setType(NodeType nodeType){
		this.nodeType = nodeType;
	}
	public void setChild(int index,TreeNode node){
		if (index>2){
			System.err.println("node's child must be less then 2");
		}
		if (index==0){
			C0 = node;
		}else if (index==1){
			C1 = node;
		}else{
			C2 = node;
		}
	}
	public String toString(){
		String str = "";
		if (getType()!=null){
			str+=getType()+"\\n";
		}
		if (strValue!=null){
			str+=strValue+"\\n";
		}
		if (numValue!=null){
			str+=numValue+"\\n";
		}
		return str;
	}
}
