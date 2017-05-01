package ir;

public class ConstOperand extends Operand{
	Integer value = null;
	public ConstOperand(Integer numValue) {
		this.value = numValue;
	}
	@Override
	public String toString(){
		return value.toString();
	}
}
