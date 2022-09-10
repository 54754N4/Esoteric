package esoteric.brainfuck.optimiser;

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
import model.Optimiser;

/* Converts recurrent loop patterns to equivalent constructs:
 * [-] == mem[ptr] = 0
 * [+] == mem[ptr] = 0
 * [->+++>+++++++<<] == mem[ptr+1] += mem[ptr] * 3; mem[ptr+2] += mem[ptr] * 7
 */
public class BFOptimiserLoopConverter implements BFVisitor<AST>, Optimiser {
	
	@Override
	public AST visit(Loop ast) {
		// Check for setter loops
		if (ast.getBlock().size() == 1) {
			AST node = ast.getBlock().get(0);
			if (Data.class.isInstance(node)) {
				Data data = Data.class.cast(node);
				if (data.getOffset() == 1 || data.getOffset() == -1)
					if (data.getPointerOffset() == ast.getPointerOffset())
						return new Data(0, ast.getPointerOffset(), true);
			}
		}
		return ast;
	}
	
	@Override
	public AST visit(Block ast) {
		Block block = new Block();
		AST visited;
		for (AST node : ast) {
			visited = visit(node);
			if (Loop.class.isInstance(visited)) {
				visited = checkMultiplicationLoop(Loop.class.cast(visited));
				// If found multiplication loop, flatten blocks together
				if (Block.class.isInstance(visited)) {
					Block child = Block.class.cast(visited);
					for (AST c : child)
						block.add(c);
					continue;
				}
			}
			block.add(visited);
		}
		return block;
	}
	
	// Check for multiplication loops
	private AST checkMultiplicationLoop(Loop ast) {
		Block block = ast.getBlock();
		int ptr = 0, nptr = -1;
		AST first = block.get(0), node;
		if (!Data.class.isInstance(first) || Data.class.cast(first).getOffset() != -1)
			return ast;	// has to start loop with a decrement instruction
		for (int i=1; i<block.size(); i++) {
			node = block.get(i);
			if (Pointer.class.isInstance(node)) {
				Pointer pointer = Pointer.class.cast(node);
				if (nptr == -1 && pointer.getOffset() < 0)
					nptr = i;
				if (nptr != -1 && pointer.getOffset() > 0)
					return ast;	// after ptrs become negative can't be positive again
				ptr += pointer.getOffset();
			} else if (!Data.class.isInstance(node))
				return ast;	// can only be Pointer or Data in mult loops
		}
		if (ptr != 0) // if pointers don't cancel out
			return ast;
		Block newBlock = new Block();
		ptr = 0;
		int factor = 0;
		for (int i=1; i<nptr; i++) {
			node = block.get(i);
			if (Pointer.class.isInstance(node)) {
				if (factor != 0)
					newBlock.add(new Multiply(ptr, factor, ast.getPointerOffset()));
				ptr += Pointer.class.cast(node).getOffset();
				factor = 0;
			} else if (Data.class.isInstance(node))
				factor += Data.class.cast(node).getOffset();
		}
		if (factor != 0) // add last multiply
			newBlock.add(new Multiply(ptr, factor, ast.getPointerOffset()));
		// finally add a set 0
		newBlock.add(new Data(0, ast.getPointerOffset(), true));
		return newBlock;
	}
	
	/* Leaf ASTs don't need optimisation */
	
	@Override
	public AST visit(Data ast) {
		return ast;
	}
	
	@Override
	public AST visit(Multiply ast) {
		return ast;
	}

	@Override
	public AST visit(Pointer ast) {
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
	public AST visit(FunctionCall ast) {
		return ast;
	}

	@Override
	public AST visit(FunctionDeclaration ast) {
		return ast;
	}
}
