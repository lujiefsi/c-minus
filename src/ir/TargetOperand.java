package ir;

public class TargetOperand extends Operand{
	public TargetOperand() {
	}
	public int id;
	public void setId(int id){
		this.id  = id;
	}
	public String toString(){
		return ""+id;
	}
}
