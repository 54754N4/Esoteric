package error;

public class LexerException extends RuntimeException {
	private static final long serialVersionUID = 7086247355681809170L;

	public LexerException(String message, int pos, String buffer) {
		super(String.format("LEXER: Error at pos %d with current "
				+ "lookahead buffer '%s' and reason: %s", pos, buffer, message));
	}
	
	public LexerException(String message) {
		super("LEXER: "+message);
	}
}
