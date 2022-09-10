package esoteric.brainfuck.ast;

import model.AST;

public abstract class IO implements AST {
	private int offset;	// of cell to output/input
	
	public IO(int offset) {
		setOffset(offset);
	}
	
	public IO() {
		this(-1);
	}
	
	public int getOffset() {
		return offset;
	}
	
	public IO setOffset(int offset) {
		this.offset = offset;
		return this;
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
		IO other = (IO) obj;
		if (offset != other.offset)
			return false;
		return true;
	}
}
