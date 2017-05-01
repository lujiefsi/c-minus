package ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlFlowGraph {
	int id = 0;
	Method method = null;
	List<BasicBlock> bbs = null;
	private BasicBlock startBB = null;
	private BasicBlock endBB = null;

	public ControlFlowGraph(Method method) {
		this.method = method;
		this.bbs = new ArrayList<BasicBlock>();
		computeCFG();
	}

	private void computeCFG() {
		startBB = new BasicBlock(id++, -1);
		bbs.add(startBB);
		Map<Integer, Quad> map = new HashMap<Integer, Quad>();
		List<Integer> entry = new ArrayList<Integer>();
		List<Integer> branch = new ArrayList<Integer>();
		List<Integer> end = new ArrayList<Integer>();
		entry.add(0);
		for (Quad ins : method.getInstructions()) {
			if (ins.operator instanceof GoToOperator) {
				entry.add(((TargetOperand) (ins.operand1)).id);
				entry.add(ins.id_number + 1);
				branch.add(ins.id_number);
			} else if (ins.operator instanceof IfCmpEQFalse) {
				entry.add(((TargetOperand) (ins.operand3)).id);
				entry.add(ins.id_number + 1);
				branch.add(ins.id_number);
			} else if (ins.operator instanceof CallOperator) {
				entry.add(ins.id_number);
				entry.add(ins.id_number + 1);
			} else if (ins.operator instanceof ReturnIOperator) {
				end.add(ins.id_number);
			} else if (ins.operator instanceof ReturnVOperator) {
				end.add(ins.id_number);
			} else if (ins.operator instanceof ExitOperator) {
				entry.add(ins.id_number);
			}
			map.put(ins.id_number, ins);
		}
		BasicBlock currentBB = startBB;
		for (Quad ins : method.getInstructions()) {
			if (entry.contains(ins.id_number)) {
				if (currentBB != null) {
					currentBB.setEnd(ins.id_number);
				}
				currentBB = new BasicBlock(id++, ins.id_number);
				bbs.add(currentBB);
				if (ins.operator instanceof ExitOperator) {
					currentBB.setEnd(ins.id_number + 1);
					endBB = currentBB;
				}
			}
			if (branch.contains(ins.id_number)) {
				if (currentBB == null) continue;
				currentBB.setEnd(ins.id_number + 1);
				currentBB = null;
			}
			if (end.contains(ins.id_number)) {
				currentBB.setEnd(ins.id_number + 1);
				currentBB = null;
			}
		}
		createEdge(map);
	}

	private void createEdge(Map<Integer, Quad> map) {
		for (BasicBlock bb : bbs) {
			Quad lastIns = map.get(bb.end - 1);
			if (lastIns == null)
				continue;
			if (lastIns.operator instanceof IfCmpEQFalse) {
				int target = ((TargetOperand) (lastIns.operand3)).id;
				for (BasicBlock bb2 : bbs) {
					Quad firstIns = map.get(bb2.start);
					if (firstIns == null)
						continue;
					if (firstIns.id_number == target) {
						bb.successors.add(bb2);
					}
				}
			} else if (lastIns.operator instanceof GoToOperator) {
				int target = ((TargetOperand) (lastIns.operand1)).id;
				for (BasicBlock bb2 : bbs) {
					Quad firstIns = map.get(bb.end - 1);
					if (firstIns == null)
						continue;
					if (firstIns.id_number == target) {
						bb.successors.add(bb2);
					}
				}
			} else if (lastIns.operator instanceof ReturnIOperator) {
				bb.successors.add(endBB);
			} else if (lastIns.operator instanceof ReturnVOperator) {
				bb.successors.add(endBB);
			}
		}
		for (int i = 0; i < bbs.size() - 1; i++) {
			BasicBlock bb = bbs.get(i);
			BasicBlock bb2 = bbs.get(i + 1);
			Quad lastIns = map.get(bb.end - 1);
			if (lastIns != null) {

				if (lastIns.operator instanceof GoToOperator) {
					continue;
				}
				if (lastIns.operator instanceof ReturnIOperator) {
					continue;
				}
				if (lastIns.operator instanceof ReturnVOperator) {
					continue;
				}
			}
			bb.successors.add(bb2);
		}
	}

	public List<BasicBlock> getAllBB() {
		return bbs;
	}
}
