package grammar;

import semantic.DataType;


public class TreeNode {
	public static int glogalIndex = 0;
	public int lineNo = 0;
	public int index = 0;
	public NodeType nodeType;
	public DataType dataType;
	/*for declaration:ID name
	 * */
	public String strValue;
	public Integer numValue;
	public TreeNode sibling;
	public TreeNode C0;
	public TreeNode C1;
	public TreeNode C2;
	public TreeNode(){
		index = glogalIndex++;
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
