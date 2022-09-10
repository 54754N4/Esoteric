package esoteric.jsfuck.ast;

import model.AST;

public class Bool implements AST {
	public static final Bool TRUE = new Bool(true), FALSE = new Bool(false);
	
	private boolean value;
	
	public Bool(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}

	public Bool flip() {
		return new Bool(!value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value ? 1231 : 1237);
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
		Bool other = (Bool) obj;
		if (value != other.value)
			return false;
		return true;
	}
	
	public Str toStringJS() {
		return new Str(value ? "true" : "false");
	}
}
