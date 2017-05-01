package util;

import grammar.TreeNode;

public abstract class Process {
	public void traverse(TreeNode t){
		if (t == null) return;
		preProc(t);
		traverse(t.C0);
		traverse(t.C1);
		traverse(t.C2);
		postProc(t);
		traverse(t.sibling);
		finProc(t);
	}
	public abstract void preProc(TreeNode node);
	
	public abstract void postProc(TreeNode node);
	
	public abstract void finProc(TreeNode node);
	
}
