package esoteric.jsfuck.transpiler;

import esoteric.jsfuck.JSFuckVisitor;
import esoteric.jsfuck.ast.Addition;
import esoteric.jsfuck.ast.Array;
import esoteric.jsfuck.ast.ArrayAccess;
import esoteric.jsfuck.ast.Bool;
import esoteric.jsfuck.ast.FunctionCall;
import esoteric.jsfuck.ast.FunctionDef;
import esoteric.jsfuck.ast.Null;
import esoteric.jsfuck.ast.Num;
import esoteric.jsfuck.ast.Str;
import esoteric.jsfuck.ast.Unary;
import esoteric.jsfuck.ast.Undefined;
import model.AST;
import model.StringFormatBuilder;
import model.Transpiler;

public class JSFuckDeobfuscator implements JSFuckVisitor<StringFormatBuilder>, Transpiler {
	
	@Override
	public StringFormatBuilder transpile(AST ast) {
		return visit(ast);
	}

	@Override
	public StringFormatBuilder visit(Addition ast) {
		StringFormatBuilder format = new StringFormatBuilder();
		for (AST node : ast)
			format.append("%s+", visit(node));
		if (format.lastChar() == '+')
			format.deleteLastChar();
		return format;
	}

	@Override
	public StringFormatBuilder visit(Unary ast) {		
		return new StringFormatBuilder()
				.append("%s%s", ast.getOperator(), visit(ast.getOperand()));
	}

	@Override
	public StringFormatBuilder visit(ArrayAccess ast) {
		return new StringFormatBuilder()
				.append("%s[%s]", visit(ast.getArray()), visit(ast.getIndex()));
	}

	@Override
	public StringFormatBuilder visit(Array ast) {
		StringFormatBuilder format = new StringFormatBuilder("[");
		for (AST node : ast)
			format.append("%s,", visit(node));
		if (format.lastChar() == ',')
			format.deleteLastChar();
		format.append("]");
		return format;
	}

	@Override
	public StringFormatBuilder visit(FunctionDef ast) {
		StringFormatBuilder format = new StringFormatBuilder();
		format.append(ast.getAccessedFrom());
		format.prepend("%s.", visit(ast.getParent()));
		return format;
	}

	@Override
	public StringFormatBuilder visit(FunctionCall ast) {
		StringFormatBuilder format = new StringFormatBuilder();
		format.append(visit(ast.getName()))
			.append("(");
		for (AST node : ast.getParams())
			format.append("%s,", visit(node));
		if (format.lastChar() == ',')
			format.deleteLastChar();
		return format.append(")");
	}

	@Override
	public StringFormatBuilder visit(Bool ast) {
		return new StringFormatBuilder()
				.append(ast.getValue());
	}

	@Override
	public StringFormatBuilder visit(Num ast) {
		return new StringFormatBuilder()
				.append(ast.getValue());
	}

	@Override
	public StringFormatBuilder visit(Str ast) {
		if (ast.getValue().startsWith("/") && ast.getValue().endsWith("/") 
				&& ast.getValue().length() != 1)
			return new StringFormatBuilder("%s", ast.getValue());	// since it's regexp not strings
		return new StringFormatBuilder("\"%s\"", ast.getValue().replace("\"", "\\\""));
	}

	@Override
	public StringFormatBuilder visit(Null ast) {
		return new StringFormatBuilder("null");
	}

	@Override
	public StringFormatBuilder visit(Undefined ast) {
		return new StringFormatBuilder("undefined");
	}
}