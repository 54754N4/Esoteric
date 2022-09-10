package esoteric.brainfuck.transpiler;

import esoteric.brainfuck.BFVisitor;
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
import model.StringFormatBuilder;
import model.Transpiler;

public class OokTranspiler implements BFVisitor<StringFormatBuilder>, Transpiler {
	public StringFormatBuilder output;
	
	public OokTranspiler() {
		output = new StringFormatBuilder();
	}
	
	public OokTranspiler reset() {
		output.delete(0, output.length());
		return this;
	}
	
	@Override
	public StringFormatBuilder transpile(AST ast) {
		reset();
		return visit(ast);
	}
	
	@Override
	public StringFormatBuilder visit(Data ast) {
		int offset = ast.getOffset();
		if (ast.getPointerOffset() == 0) {
			if (offset == 1)
				output.append("Ook. Ook. ");
			else if (offset == -1)
				output.append("Ook! Ook! ");
			else if (offset > 0 )
				output.append("Ook. Ook.(" + offset+") ");
			else if (offset < 0)
				output.append("Ook! Ook!(" + offset+") ");
		} else if (offset > 0)
			output.append("Ook. Ook.(" + offset + ")(" + ast.getPointerOffset() + ") ");
		else if (offset > 0)
			output.append("Ook. Ook.(" + offset + ")(" + ast.getPointerOffset() + ") ");
		return output;
	}
	
	@Override
	public StringFormatBuilder visit(Pointer ast) {
		int offset = ast.getOffset();
		if (offset == 1)
			output.append("Ook. Ook? ");
		else if (offset == -1)
			output.append("Ook? Ook. ");
		else if (offset > 0)
			output.append("Ook. Ook?(" + offset + ") ");
		else 
			output.append("Ook? Ook.(" + offset + ") ");
		return output;
	}

	@Override
	public StringFormatBuilder visit(Input ast) {
		if (ast.getOffset() == -1)
			output.append("Ook. Ook! ");
		else
			output.append("Ook. Ook!(" + ast.getOffset() + ") ");
		return output;
	}

	@Override
	public StringFormatBuilder visit(Output ast) {
		if (ast.getOffset() == -1)
			output.append("Ook! Ook. ");
		else
			output.append("Ook! Ook.(" + ast.getOffset() + ") ");
		return output;
	}

	@Override
	public StringFormatBuilder visit(Block ast) {
		for (AST node : ast)
			visit(node);
		return output;
	}

	@Override
	public StringFormatBuilder visit(Loop ast) {
		output.append("Ook! Ook? ");
		visit(ast.getBlock());
		output.append("Ook? Ook! ");
		return output;
	}
	
	@Override
	public StringFormatBuilder visit(Multiply ast) {
		return error("Multiplies don't exist in ook");	// should not have optimised loops if we want to transpile to ook
	}
	
	@Override
	public StringFormatBuilder visit(FunctionCall ast) {
		return error("Function calls don't exist in ook"); // Only used by high-level transpilers
	}
	
	@Override
	public StringFormatBuilder visit(FunctionDeclaration ast) {
		return error("Functions declarations don't exist in ook"); // Only used by high-level transpilers
	}
}