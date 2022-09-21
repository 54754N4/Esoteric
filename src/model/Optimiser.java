package model;

import java.util.function.Function;

@FunctionalInterface
public interface Optimiser extends Function<AST, AST>, Visitor<AST> {
	@Override
	default AST apply(AST ast) {
		return visit(ast);
	}
}
