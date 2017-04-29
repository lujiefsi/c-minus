package ir;

public class Quad {
	private Operator operator;
	private Operand operand1, operand2, operand3, operand4;
	private int id_number;

	Quad(int id, Operator operator) {
		this.id_number = id;
		this.operator = operator;
	}

	Quad(int id, Operator operator, Operand operand1) {
		this.id_number = id;
		this.operator = operator;
		this.operand1 = operand1;
	}

	Quad(int id, Operator operator, Operand operand1, Operand operand2) {
		this.id_number = id;
		this.operator = operator;
		this.operand1 = operand1;
		this.operand2 = operand2;
	}

	Quad(int id, Operator operator, Operand operand1, Operand operand2,
			Operand operand3) {
		this.id_number = id;
		this.operator = operator;
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operand3 = operand3;
	}

	Quad(int id, Operator operator, Operand operand1, Operand operand2,
			Operand operand3, Operand operand4) {
		this.id_number = id;
		this.operator = operator;
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operand3 = operand3;
		this.operand4 = operand4;
	}
}
