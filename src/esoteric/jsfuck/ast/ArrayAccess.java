package esoteric.jsfuck.ast;

import model.AST;

public class ArrayAccess implements AST {
	private AST array; // can be a string or array
	private AST index;
	
	public ArrayAccess(AST array, AST index) {
		this.array = array;
		this.index = index;
	}
	
	public AST getArray() {
		return array;
	}
	
	public AST getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((array == null) ? 0 : array.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
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
		ArrayAccess other = (ArrayAccess) obj;
		if (array == null) {
			if (other.array != null)
				return false;
		} else if (!array.equals(other.array))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		return true;
	}
}
