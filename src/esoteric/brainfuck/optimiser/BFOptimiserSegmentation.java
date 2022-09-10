package esoteric.brainfuck.optimiser;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

/* Converts every while loop to a function and replaces them
 * with a FunctionCall AST. Every while loop is compared to 
 * the already created functions to avoid duplications.
 * 
 * Note: FunctionCall::hashCode == FunctionDeclaration::getBody::hashCode
 */
public class BFOptimiserSegmentation implements BFVisitor<AST>, Optimiser {
	private Set<FunctionCall> functions;
	
	public BFOptimiserSegmentation() {
		functions = new HashSet<>();
	}
	
	public BFOptimiserSegmentation reset() {
		functions.clear();
		return this;
	}
	
	@Override
	public AST visit(Loop ast) {
		AST loop = new Loop(ast.getPointerOffset(), (Block) visit(ast.getBlock()));
		int hashCode = loop.hashCode();
		Optional<FunctionCall> optional = functions.stream()
			.filter(call -> call.hashCode() == hashCode)
			.findFirst();
		if (optional.isEmpty()) {
			FunctionDeclaration declaration = new FunctionDeclaration(functions.size(), loop);
			FunctionCall call = new FunctionCall(declaration);
			functions.add(call);
			return call;
		}
		return optional.get();
	}
	
	@Override
	public AST visit(Block ast) {
		Block block = new Block();
		for (AST node : ast)
			block.add(visit(node));
		return block;
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
	public AST visit(FunctionDeclaration ast) {
		return ast;
	}
	
	@Override
	public AST visit(FunctionCall ast) {
		return ast;
	}
}