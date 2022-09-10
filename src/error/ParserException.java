package error;

public class ParserException extends RuntimeException {
	private static final long serialVersionUID = 4940927480860260247L;

	public ParserException(String message, int pos, String buffer) {
		super(String.format("PARSER: Error at pos %d with current "
				+ "lookahead buffer '%s' and reason: %s", pos, buffer, message));
	}
	
	public ParserException(String message) {
		super("PARSER: "+message);
	}
}
