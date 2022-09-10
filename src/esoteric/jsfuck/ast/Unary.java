package esoteric.jsfuck.ast;

import model.AST;

public class Unary implements AST {
	private String operator;
	private AST operand;
	
	public Unary(String operator, AST operand) {
		this.operator = operator;
		this.operand = operand;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public AST getOperand() {
		return operand;
	}
	
	public boolean isNot() {
		return operator.equals("!");
	}
	
	public boolean isPlus() {
		return operator.equals("+");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operand == null) ? 0 : operand.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unary other = (Unary) obj;
		if (operand == null) {
			if (other.operand != null)
				return false;
		} else if (!operand.equals(other.operand))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		return true;
	}
}
