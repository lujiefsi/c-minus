package ir;

import java.util.ArrayList;

public class ParmListOperand extends Operand{
	ArrayList<Operand> parms  = null;
	public ParmListOperand() {
		parms = new ArrayList<Operand>();
	}
	public void addParm(Operand parm){
		parms.add(parm);
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append('(');
		for (Operand parm:parms){
			sb.append(parm+",");
		}
		sb.append(')');
		return sb.toString();
	}
}
