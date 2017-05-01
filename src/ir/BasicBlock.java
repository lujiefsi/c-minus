package ir;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
	public int id = 0;
	int start;
	int end=-1;
	List<BasicBlock> predecessors;
	List<BasicBlock> successors;
	public BasicBlock(int id, int start){
		this.id = id;
		this.start = start;
		this.predecessors = new ArrayList<BasicBlock>();
		this.successors = new ArrayList<BasicBlock>();
	}
	public void setEnd(int end){
		if (this.end==-1){
			this.end = end;
		}
	}
	public String toString(){
		return "BB"+id+": "+start+"->"+end;
	}
	public List<BasicBlock> getSuccessors(){
		return successors;
	}
}
