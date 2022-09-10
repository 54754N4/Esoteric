package esoteric.jsfuck.ast;

public class Infinity extends Num {
	private boolean negative;
	
	public Infinity(boolean negative) {
		super(negative ? "-Infinity" : "Infinity", false);
		this.negative = negative;
	}
	
	public boolean isNegative() {
		return negative;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (negative ? 1231 : 1237);
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
		Infinity other = (Infinity) obj;
		if (negative != other.negative)
			return false;
		return true;
	}
}
