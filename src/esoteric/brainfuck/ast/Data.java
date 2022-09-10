package esoteric.brainfuck.ast;

public class Data extends Mergeable {
	private int pointerOffset;
	private boolean set;
	
	public Data(int offset, int pointerOffset, boolean set) {
		super(offset);
		this.pointerOffset = pointerOffset;
		this.set = set; 	// to differentiate with: mem[ptr + pointerOffset] = offset
	}
	
	public Data(int offset, int pointerOffset) {	// mem[ptr + pointerOffset] += offset
		this(offset, pointerOffset, false);
	}
	
	public Data(int offset) {
		this(offset, 0);
	}
	
	public boolean isSet() {
		return set;
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
		int result = super.hashCode();
		result = prime * result + pointerOffset;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Data other = (Data) obj;
		if (pointerOffset != other.pointerOffset)
			return false;
		return true;
	}
}
