package model;

/* Uses a generic type to allow re-use using different
 * language enum constants.
 */
public class Token<T extends Enum<T>> {
	private T type;
	private String value;
	private int pos;
	
	public Token(T type, String value, int pos) {
		this.type = type;
		this.value = value;
		this.pos = pos;
	}
	
	public Token(T type, int pos) {
		this(type, type.toString(), pos);
	}
	
	public T getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getPosition() {
		return pos;
	}
	
	@Override
	public String toString() {
		return String.format("Token[type=%s, value=%s, pos=%d]", type.name(), value, pos);
	}
}
