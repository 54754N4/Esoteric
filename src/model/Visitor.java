package model;

import error.InterpreterException;

public interface Visitor<T> {
	T visit(AST ast);
	
	default <S> S error(String message) {
		throw new InterpreterException(message);
	}
}
