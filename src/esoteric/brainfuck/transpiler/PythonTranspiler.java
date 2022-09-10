package esoteric.brainfuck.transpiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import esoteric.brainfuck.BFInterpreter;
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

public class PythonTranspiler implements BFVisitor<StringFormatBuilder>, Transpiler {
	private String ptr = "ptr", mem = "mem";
	private int cells, nested;
	private Map<Integer, FunctionCall> declared;
	
	public PythonTranspiler(int cells) {
		this.cells = cells;
		declared = new HashMap<>();
	}
	
	public PythonTranspiler() {
		this(BFInterpreter.DEFAULT_MEMORY_CELLS);
	}
	
	public PythonTranspiler reset() {
		nested = 0;
		declared.clear();
		return this;
	}
	
	@Override
	public StringFormatBuilder transpile(AST ast) {
		reset();
		StringFormatBuilder sb = new StringFormatBuilder();
		sb.append(visit(ast));
		postVisit(sb);
		return sb;
	}
	
	/* Helper methods */
	
	private void append(StringFormatBuilder sb, String format, Object...args) {
		sb.append(tabs(nested) + format, args);
	}
	
	private void appendln(StringFormatBuilder sb, String format, Object...args) {
		append(sb, format + "%n", args);
	}
	
	private String tabs(int count) {
		StringBuilder sb = new StringBuilder();
		while (count-->0)
			sb.append("\t");
		return sb.toString();
	}
	
	/* Transpiling methods */
	
	private void postVisit(StringFormatBuilder sb) {
		StringFormatBuilder declarations = new StringFormatBuilder();
		Set<FunctionDeclaration> sorted = new TreeSet<>((f1, f2) -> f1.getFid() - f2.getFid());
		List<FunctionDeclaration> calls = declared.values().stream()
				.map(FunctionCall::getDeclaration)
				.collect(Collectors.toList());
		sorted.addAll(calls);
		for (FunctionDeclaration declaration : sorted) {
			nested = 0;
			appendln(declarations, "def %s():", declaration.getName());
			nested = 1;
			appendln(declarations, "global %s, %s", ptr, mem);
			appendln(declarations, "%s", visit(declaration.getBody()).toString().trim());
		}
		sb.prepend(System.lineSeparator())
			.prepend(declarations)
			.prependln("%s = 0%n", ptr)
			.prependln("%s = [0] * %d", mem, cells);
	}
	
	@Override
	public StringFormatBuilder visit(Data ast) {
		StringFormatBuilder format = new StringFormatBuilder();
		if (ast.getPointerOffset() == 0)
			format.append("%s[%s]", mem, ptr);
		else
			format.append("%s[%s + %d]", mem, ptr, ast.getPointerOffset());
		if (ast.isSet())
			format.append(" = %d", ast.getOffset());
		else
			format.append(" += %d", ast.getOffset());
		StringFormatBuilder sb = new StringFormatBuilder();
		appendln(sb, format.toString());
		return sb;
	}
	
	@Override
	public StringFormatBuilder visit(Multiply ast) {
		StringFormatBuilder format = new StringFormatBuilder();
		if (ast.getPointerOffset() == 0)
			format.append("%s[%s + %d] += %s[%s]", mem, ptr, ast.getOffset(), mem, ptr);
		else
			format.append("%s[%s + %d + %d] += %s[%s + %d]", mem, ptr, ast.getPointerOffset(), ast.getOffset(), mem, ptr, ast.getPointerOffset());
		if (ast.getFactor() != 1)
			format.append(" * %d", ast.getFactor());
		StringFormatBuilder sb = new StringFormatBuilder();
		appendln(sb, format.toString());
		return sb;
	}
	
	@Override
	public StringFormatBuilder visit(Pointer ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		appendln(sb, "%s += %d", ptr, ast.getOffset());
		return sb;
	}

	@Override
	public StringFormatBuilder visit(Input ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		if (ast.getOffset() == -1)
			appendln(sb, "%s[%s] = ord(input()[0])", mem, ptr);
		else
			appendln(sb, "%s[%s + %d] = ord(input()[0])", mem, ptr, ast.getOffset());
		return sb;
	}

	@Override
	public StringFormatBuilder visit(Output ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		if (ast.getOffset() == -1)
			appendln(sb, "print(chr(%s[%s]), end='')", mem, ptr);
		else
			appendln(sb, "print(chr(%s[%s + %d]), end='')", mem, ptr, ast.getOffset());
		return sb;
	}

	@Override
	public StringFormatBuilder visit(Block ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		for (AST node : ast)
			sb.append(visit(node));
		return sb;
	}

	@Override
	public StringFormatBuilder visit(Loop ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		if (ast.getPointerOffset() == 0)
			appendln(sb, "while %s[%s] != 0:", mem, ptr);
		else
			appendln(sb, "while %s[%s + %d] != 0:", mem, ptr, ast.getPointerOffset());
		nested++;
		for (AST node : ast.getBlock())
			sb.append(visit(node));
		nested--;
		return sb;
	}

	@Override
	public StringFormatBuilder visit(FunctionCall ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		appendln(sb, "%s()", ast.getDeclaration().getName());
		int hashCode = ast.hashCode();
		if (!declared.containsKey(hashCode))
			declared.put(hashCode, ast);
		visit(ast.getDeclaration());	// visit but ignore output, postVisit will take care of it
		return sb;
	}
	
	@Override
	public StringFormatBuilder visit(FunctionDeclaration ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		visit(ast.getBody());	// visit but ignore output, to make sure to find other FunctionCalls
		return sb;
	}
}