package esoteric.jsfuck.ast;

import model.AST;

public class FunctionCall implements AST {
	private AST name;
	private Array params;
	
	public FunctionCall(AST name, Array params) {
		this.name = name;
		this.params = params;
	}
	
	public FunctionCall(AST name) {
		this(name, new Array());
	}
	
	public AST getName() {
		return name;
	}
	
	public Array getParams() {
		return params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
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
		FunctionCall other = (FunctionCall) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		return true;
	}
}
