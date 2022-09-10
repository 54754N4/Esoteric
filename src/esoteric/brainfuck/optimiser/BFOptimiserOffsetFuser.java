package esoteric.brainfuck.optimiser;

import java.util.HashMap;
import java.util.Map;

import esoteric.brainfuck.BFVisitor;
import esoteric.brainfuck.ast.Block;
import esoteric.brainfuck.ast.Data;
import esoteric.brainfuck.ast.FunctionCall;
import esoteric.brainfuck.ast.FunctionDeclaration;
import esoteric.brainfuck.ast.IO;
import esoteric.brainfuck.ast.Input;
import esoteric.brainfuck.ast.Loop;
import esoteric.brainfuck.ast.Multiply;
import esoteric.brainfuck.ast.Output;
import esoteric.brainfuck.ast.Pointer;
import model.AST;
import model.Optimiser;

/* Fuses offsets from sequential pointer and data
 * instructions. Hence the following code:
 * ptr += 10; 
 * mem[ptr] += 2
 * ptr -= 7
 * mem[ptr] += 15 
 * Would become : 
 * mem[ptr+10] += 2
 * mem[ptr+3] += 15
 * 
 * NOTE: DOESN'T WORK YET. NOT TO USE
 */
public class BFOptimiserOffsetFuser implements BFVisitor<AST>, Optimiser {
//	private Deque<Integer> offsets;
	private int pointerOffset;
	private Map<Integer, FunctionDeclaration> declarations;
	
	public BFOptimiserOffsetFuser() {
//		offsets = new LinkedList<>();
//		offsets.push(0);
		pointerOffset = 0;
		declarations = new HashMap<>();
	}

	@Override
	public AST visit(Block ast) {
		Block block = new Block();
		int startOffset = pointerOffset;
//		int startOffset = offsets.pop(),
//			pointerOffset = startOffset;
		for (AST node : ast) {
			if (Pointer.class.isInstance(node)) {
				pointerOffset += Pointer.class.cast(node).getOffset();
			} else if (Data.class.isInstance(node)) {
				Data.class.cast(node).setPointerOffset(pointerOffset);
				block.add(node);
			} else if (IO.class.isInstance(node)) {
				IO.class.cast(node).setOffset(pointerOffset);
				block.add(node);
//			} else if (Loop.class.isInstance(node)) {
//				offsets.push(pointerOffset);
//				block.add(visit(node));
//				pointerOffset = offsets.pop();
			} else
				block.add(visit(node));
		}
		// Fix pointer at end of block or loop if it differs from start
		int delta = pointerOffset - startOffset;
		if (delta != 0)
			block.add(new Pointer(delta));
		pointerOffset += delta;
//		offsets.push(pointerOffset);
		return block;
	}

	@Override
	public AST visit(Loop ast) {
		return new Loop(pointerOffset, (Block) visit(ast.getBlock()));
//		return new Loop(offsets.peek(), (Block) visit(ast.getBlock()));
	}
	
	@Override
	public AST visit(FunctionCall ast) {
		int fid = ast.getDeclaration().getFid();
		if (!declarations.containsKey(fid))
			declarations.put(fid, (FunctionDeclaration) visit(ast.getDeclaration()));
		return ast.setDeclaration(declarations.get(fid));
	}
	
	@Override
	public AST visit(FunctionDeclaration ast) {
		return ast.setBody(visit(ast.getBody()));
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
}