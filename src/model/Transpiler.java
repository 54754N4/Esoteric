package model;

@FunctionalInterface
public interface Transpiler {
	StringFormatBuilder transpile(AST ast);
}
