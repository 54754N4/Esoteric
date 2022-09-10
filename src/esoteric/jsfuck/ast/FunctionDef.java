package esoteric.jsfuck.ast;

import model.AST;

public class FunctionDef implements AST {
	
	private AST parent;	// to simulate chained calls like [].flat.constructor
	private String name, accessedFrom;
	
	public FunctionDef(String name, AST parent, String accessedFrom) {
		this.name = name;
		this.parent = parent;
		this.accessedFrom = accessedFrom;
	}
	
	public String getName() {
		return name;
	}
	
	public AST getParent() {
		return parent;
	}
	
	public String getAccessedFrom() {
		return accessedFrom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
		FunctionDef other = (FunctionDef) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
}