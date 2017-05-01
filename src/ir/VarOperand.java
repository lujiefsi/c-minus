package ir;

public class VarOperand extends Operand{
	String name;
	public VarOperand(String name){
		this.name = name;
	}
	@Override
	public String toString(){
		return name;
	}
}
