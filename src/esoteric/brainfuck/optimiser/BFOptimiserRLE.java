package esoteric.brainfuck.optimiser;

import esoteric.brainfuck.BFVisitor;
import esoteric.brainfuck.ast.Block;
import esoteric.brainfuck.ast.Data;
import esoteric.brainfuck.ast.FunctionCall;
import esoteric.brainfuck.ast.FunctionDeclaration;
import esoteric.brainfuck.ast.Input;
import esoteric.brainfuck.ast.Loop;
import esoteric.brainfuck.ast.Mergeable;
import esoteric.brainfuck.ast.Multiply;
import esoteric.brainfuck.ast.Output;
import esoteric.brainfuck.ast.Pointer;
import model.AST;
import model.Optimiser;

/* Follows the run-length encoded (RLE) approach. Adjacent
 * mergeable instructions are merged together through the 
 * additive combination of both of their respective offsets.
 * Examples:
 * ---------->> == -(10)>(2)
 * ++++--..>><< == +(2)..
 * >>><<+-.+--- == >.-(2)
 */
public class BFOptimiserRLE implements BFVisitor<AST>, Optimiser {

	@Override
	public AST visit(Block ast) {
		if (ast.size() == 0)
			return ast;
		Block block = new Block();
		int i = 0;
		AST current = ast.get(0), next;
		while (current != null) {
			next = i+1 < ast.size() ? ast.get(++i) : null;
			if (Mergeable.class.isInstance(current) && next != null && current.getClass().equals(next.getClass()))
				Mergeable.class.cast(current).increment(Mergeable.class.cast(next).getOffset());
			else {
				if (!Mergeable.class.isInstance(current) || Mergeable.class.cast(current).getOffset() != 0)
					block.add(visit(current));
				current = next;
			}
		}
		return block;
	}

	@Override
	public AST visit(Loop ast) {
		return new Loop(ast.getPointerOffset(), (Block) visit(ast.getBlock()));
	}
	
	@Override
	public AST visit(FunctionCall ast) {
		AST body = visit(ast.getDeclaration().getBody());
		FunctionDeclaration declaration = new FunctionDeclaration(ast.getDeclaration().getFid(), body);
		return new FunctionCall(declaration);
	}
	
	/* Leaf ASTs don't need optimisation */
	
	@Override
	public AST visit(FunctionDeclaration ast) {
		return ast;
	}
	
	@Override
	public AST visit(Data ast) {
		return ast;
	}

	@Override
	public AST visit(Multiply ast) {
		return ast;
	}
	
	@Override
	public AST visit(Input ast) {
		return ast;
	}

	@Override
	public AST visit(Output ast) {
		return ast;
	}

	@Override
	public AST visit(Pointer ast) {
		return ast;
	}
}