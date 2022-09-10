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

public class BFInterpreter implements BFVisitor<Void> {
	public static final int DEFAULT_MEMORY_CELLS = 2048, 
			MAX_CELL_VALUE = 255, MIN_CELL_VALUE = 0;
	
	private int ptr;
	private int[] cells;
	
	public BFInterpreter(int memorySize) {
		ptr = 0;
		cells = new int[memorySize];
	}
	
	public BFInterpreter() {
		this(DEFAULT_MEMORY_CELLS);
	}
	
	public int getPtr() {
		return ptr;
	}
	
	public int[] getMemory() {
		return cells;
	}
	
	private void handleOverflows(int pointerOffset) {
		// Wrap around on cell overflow/underflow
		if (cells[ptr + pointerOffset] > MAX_CELL_VALUE)
			cells[ptr + pointerOffset] = MIN_CELL_VALUE;
		if (cells[ptr + pointerOffset] < MIN_CELL_VALUE)
			cells[ptr + pointerOffset] = MAX_CELL_VALUE;
	}
	
	@Override
	public Void visit(Data ast) {
		if (ast.isSet())
			cells[ptr + ast.getPointerOffset()] = ast.getOffset();
		else
			cells[ptr + ast.getPointerOffset()] += ast.getOffset();
		handleOverflows(ast.getPointerOffset());
		return null;
	}
	
	@Override
	public Void visit(Multiply ast) {
		cells[ptr + ast.getPointerOffset() + ast.getOffset()] += cells[ptr] * ast.getFactor();
		handleOverflows(ast.getPointerOffset());
		return null;
	}

	@Override
	public Void visit(Pointer ast) {
		ptr += ast.getOffset();
		// Wrap around on ptr overflow and underflow
		if (ptr > cells.length)
			ptr = 0;
		if (ptr < 0)
			ptr = cells.length - 1;
		return null;
	}
	
	@Override
	public Void visit(Input ast) {
		// block until 1 char of user input given
		try {
			int offset = 0;
			if (ast.getOffset() != -1)
				offset = ast.getOffset();	
			cells[ptr + offset] = System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Void visit(Output ast) {
		int offset = 0;
		if (ast.getOffset() != -1)
			offset = ast.getOffset();
		System.out.print((char) cells[ptr + offset]);
		return null;
	}
	
	@Override
	public Void visit(Block ast) {
		for (AST node : ast)
			visit(node);
		return null;
	}

	@Override
	public Void visit(Loop ast) {
		while (cells[ptr + ast.getPointerOffset()] != 0)
			for (AST node : ast.getBlock())
				visit(node);
		return null;
	}
	
	@Override
	public Void visit(FunctionCall ast) {
		return visit(ast.getDeclaration());
	}
	
	@Override
	public Void visit(FunctionDeclaration ast) {
		return visit(ast.getBody());
	}
}