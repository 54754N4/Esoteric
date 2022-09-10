package esoteric.brainfuck.ast;

import model.AST;

public abstract class Mergeable implements AST {
	private int offset;
	
	public Mergeable(int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return offset;
	}
	
	/* Since some ASTs can be merged, check the sign 
	 * and increment accordingly (hence ++ == +2 and --- == -3 etc) */
	public void increment(int offset) {
		this.offset += offset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + offset;
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
		Mergeable other = (Mergeable) obj;
		if (offset != other.offset)
			return false;
		return true;
	}
}
