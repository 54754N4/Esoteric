package model;

import java.util.function.Function;

public interface Optimiser extends Function<AST, AST>, Visitor<AST> {
	@Override
	default AST apply(AST ast) {
		return visit(ast);
	}
}
