package model;

import error.ParserException;

public abstract class Parser<T extends Enum<T>> {
	private int tokens;
	private Lexer<T> lexer;
	private Token<T> current;
	
	public Parser(Lexer<T> lexer) {
		this.lexer = lexer;
		current = lexer.getNextToken();
	}
	
	/* Accessors */
	
	protected Token<T> getCurrent() {
		return current;
	}
	
	protected Lexer<T> getLexer() {
		return lexer;
	}
	
	public int getTokensCount() {
		return tokens;
	}
	
	/* Parser methods */
	
	protected final <S> S error(String message) {
		throw new ParserException(message, lexer.getPosition(), lexer.getLookaheadBuffer());
	}
	
	@SafeVarargs
	protected final boolean is(T...types) {
		for (T type : types)
			if (type == current.getType())
				return true;
		return false;
	}
	
	protected final void consume(T type) {
		if (is(type))
			current = lexer.getNextToken();
		else
			error("Unexpected token "+current.getType()+", expected type: "+type);
		tokens++;
	}
	
	public abstract AST parse();
}
