package esoteric.jsfuck;

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
import model.Visitor;

public interface JSFuckVisitor<T> extends Visitor<T> {
	T visit(Addition ast);
	T visit(Unary ast);
	T visit(ArrayAccess ast);
	T visit(Array ast);
	T visit(FunctionDef ast);
	T visit(FunctionCall ast);
	T visit(Bool ast);
	T visit(Num ast);
	T visit(Str ast);
	T visit(Null ast);
	T visit(Undefined ast);
	
	@Override
	default T visit(AST ast) {	// double-dispatch
		if (Addition.class.isInstance(ast))
			return visit(Addition.class.cast(ast));
		if (Unary.class.isInstance(ast))
			return visit(Unary.class.cast(ast));
		if (Array.class.isInstance(ast))
			return visit(Array.class.cast(ast));
		if (ArrayAccess.class.isInstance(ast))
			return visit(ArrayAccess.class.cast(ast));
		if (FunctionDef.class.isInstance(ast))
			return visit(FunctionDef.class.cast(ast));
		if (FunctionCall.class.isInstance(ast))
			return visit(FunctionCall.class.cast(ast));
		if (Bool.class.isInstance(ast))
			return visit(Bool.class.cast(ast));
		if (Num.class.isInstance(ast))
			return visit(Num.class.cast(ast));
		if (Str.class.isInstance(ast))
			return visit(Str.class.cast(ast));
		if (Null.class.isInstance(ast))
			return visit(Null.class.cast(ast));
		if (Undefined.class.isInstance(ast))
			return visit(Undefined.class.cast(ast));
		return error("Unrecognized AST node type: "+ast.getClass());
	}
}
