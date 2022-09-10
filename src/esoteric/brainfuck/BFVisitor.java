package esoteric.brainfuck;

import esoteric.brainfuck.ast.Block;
import esoteric.brainfuck.ast.Data;
import esoteric.brainfuck.ast.FunctionCall;
import esoteric.brainfuck.ast.FunctionDeclaration;
import esoteric.brainfuck.ast.Input;
import esoteric.brainfuck.ast.Loop;
import esoteric.brainfuck.ast.Multiply;
import esoteric.brainfuck.ast.Output;
import esoteric.brainfuck.ast.Pointer;
import model.AST;
import model.Visitor;

public interface BFVisitor<T> extends Visitor<T> {
	T visit(Data ast);
	T visit(Multiply ast);
	T visit(Pointer ast);
	T visit(Input ast);
	T visit(Output ast);
	T visit(Block ast);
	T visit(Loop ast);
	T visit(FunctionCall ast);
	T visit(FunctionDeclaration ast);
	
	@Override
	default T visit(AST ast) {	// double-dispatch
		if (Data.class.isInstance(ast))
			return visit(Data.class.cast(ast));
		if (Multiply.class.isInstance(ast))
			return visit(Multiply.class.cast(ast));
		if (Pointer.class.isInstance(ast))
			return visit(Pointer.class.cast(ast));
		if (Input.class.isInstance(ast))
			return visit(Input.class.cast(ast));
		if (Output.class.isInstance(ast))
			return visit(Output.class.cast(ast));
		if (Block.class.isInstance(ast))
			return visit(Block.class.cast(ast));
		if (Loop.class.isInstance(ast))
			return visit(Loop.class.cast(ast));
		if (FunctionCall.class.isInstance(ast))
			return visit(FunctionCall.class.cast(ast));
		if (FunctionDeclaration.class.isInstance(ast))
			return visit(FunctionDeclaration.class.cast(ast));
		return error("Unrecognized AST node type: "+ast.getClass());
	}
}