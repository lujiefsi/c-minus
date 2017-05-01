package ir;
/*temp var*/
public class RegisterOperand extends Operand{
	public int id = 0;
	public RegisterOperand(int id ){
		this.id = id;
	}
	@Override
	public String toString(){
		return "R"+id;
	}
}
