package esoteric.brainfuck.ast;

public class Pointer extends Mergeable  {
	public Pointer(int offset) {	// ptr += offset
		super(offset);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
}
