package esoteric.brainfuck.ast;

import model.AST;

public class Multiply implements AST {
	private int pointerOffset, offset, factor;
	
	public Multiply(int offset, int factor, int pointerOffset) {
		this.offset = offset;
		this.factor = factor;
		this.pointerOffset = pointerOffset;
	}
	
	public Multiply(int offset, int factor) {
		this(offset, factor, 0);
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getFactor() {
		return factor;
	}
	
	public int getPointerOffset() {
		return pointerOffset;
	}
	
	public void setPointerOffset(int offset) {
		pointerOffset = offset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + factor;
		result = prime * result + offset;
		result = prime * result + pointerOffset;
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
		Multiply other = (Multiply) obj;
		if (factor != other.factor)
			return false;
		if (offset != other.offset)
			return false;
		if (pointerOffset != other.pointerOffset)
			return false;
		return true;
	}
}
