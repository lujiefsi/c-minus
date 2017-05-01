package ir;

public class Quad {
	public Operator operator;
	public Operand operand1, operand2, operand3, operand4;
	public int id_number;

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
	 public String toString() {
	        StringBuffer s = new StringBuffer();
	        s.append(id_number+"\t");
	        s.append(operator+"\t");
	        if (operand1 == null) {
	            if (operand2 == null) return s.toString();
	            s.append("    \t");
	        } else {
	            s.append(operand1.toString());
	            if (operand2 == null) return s.toString();
	            s.append(",\t");
	        }
	        s.append(operand2.toString());
	        if (operand3 == null) return s.toString();
	        s.append(",\t");
	        s.append(operand3.toString());
	        if (operand4 == null) return s.toString();
	        s.append(",\t");
	        s.append(operand4.toString());
	        return s.toString();
	    }
}
