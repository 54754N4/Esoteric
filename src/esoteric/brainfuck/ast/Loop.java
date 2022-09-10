package esoteric.brainfuck.ast;

import model.AST;

public class Loop implements AST {
	private Block block;
	private int pointerOffset;  // condition offset
	
	public Loop(int pointerOffset, Block block) {
		this.pointerOffset = pointerOffset;
		this.block = block;
	}

	public Loop(Block block) {
		this(0, block);
	}
	
	public Loop() {
		this(new Block());
	}
	
	public Block getBlock() {
		return block;
	}
	
	public Loop setBlock(Block block) {
		this.block = block;
		return this;
	}
	
	public int getPointerOffset() {
		return pointerOffset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
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
		Loop other = (Loop) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (pointerOffset != other.pointerOffset)
			return false;
		return true;
	}
}