package model;

import error.LexerException;

/* Lookahead lexer that implements the shift buffer using a string. 
 * Reads from a char stream and builds generic tokens progressively
 * based on representation defined by a specifc enum class.
 * 
 * Assumption(s):
 * 	- Every Enum class contains an EOF token who's value is '\0'
 * (e.g. null char) to represent the end of file and end of lexical analysis.
 */
public class Lexer<T extends Enum<T>> {
	private static final String EOF = "\0";
	
	private CharStream source;		// character source stream
	private Class<T> enumClass;		// class to allow retrieval of enum constants
	private T[] enums;				// list of enum constant tokens
	
	private int pos;				// keeps track of position inside the input
	private String buffer;			// simulates a lookahead buffer
	private final int bufferLength;	// lookahead buffer size is limited by biggest token size
	
	private T EOF_TOKEN;
	
	public Lexer(CharStream source, Class<T> enumClass) {
		this.source = source;
		this.enumClass = enumClass;
		enums = enumClass.getEnumConstants();
		pos = 0;
		buffer = "";
		// Determine biggest token to set buffer size
		int max = 0;
		for (T e : enums)
			if (max < e.toString().length())
				max = e.toString().length();
		bufferLength = max;
		fillBuffer();
		// Find EOF enum type
		for (T e : enums) {
			if (e.toString().equals("\0")) {
				EOF_TOKEN = e;
				break;
			}
		}
		if (EOF_TOKEN == null)
			throw new LexerException("Enum didn't define an EOF token type.");
	}

	/* Accessors */
	
	public final CharStream getCharStream() {
		return source;
	}

	public final Class<T> getEnumClass() {
		return enumClass;
	}
	
	public final String getLookaheadBuffer() {
		return buffer;
	}

	public final int getPosition() {
		return pos - bufferLength;
	}
	
	/* Lexer methods */
	
	protected final <S> S error(String message) {
		throw new LexerException(message, pos, buffer);
	}
	
	protected final boolean peek(String str) {
		return buffer.startsWith(str);
	}
	
	protected final void advance(int count) {
		while (count-->0)
			advance();
	}
	
	protected final void advance() {
		pos++;
		if (source.isAvailable())
			buffer += source.getNextChar();
		else 
			buffer += EOF;
		int overflow = buffer.length() - bufferLength;
		if (overflow > 0)							// If buffer has an excess
			buffer = buffer.substring(overflow);	// clear from the top
	}
	
	private void fillBuffer() {
		if (buffer.length() < bufferLength)
			advance(bufferLength - buffer.length());
	}
	
	protected void skipWhitespace() {
		while (Character.isWhitespace(buffer.charAt(0))) 
			advance();
	}
	
	protected final Token<T> createToken(T type) {
		// substract buffer and token length since pos includes buffer size
		int position = pos - type.toString().length() - bufferLength;
		return new Token<>(type, type.toString(), position);
	}
	
	public Token<T> getNextToken() {
		if (!buffer.startsWith(EOF)) {
			skipWhitespace();
			for (T e : enums) {
				String token = e.toString();
				if (peek(token)) {
					advance(token.length());
					return createToken(e);
				}
			}
			return error("Unrecognized input");
		}
		return createToken(EOF_TOKEN);
	}
}