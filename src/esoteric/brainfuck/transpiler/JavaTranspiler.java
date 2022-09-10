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

public class JavaTranspiler implements BFVisitor<StringFormatBuilder>, Transpiler {
	private String ptr = "ptr", mem = "mem";
	private int cells, nested;
	private Map<Integer, FunctionCall> declared;
	
	public JavaTranspiler(int cells) {
		this.cells = cells;
		declared = new HashMap<>();
	}
	
	public JavaTranspiler() {
		this(BFInterpreter.DEFAULT_MEMORY_CELLS);
	}
	
	public JavaTranspiler reset() {
		nested = 1;
		declared.clear();
		return this;
	}
	
	@Override
	public StringFormatBuilder transpile(AST ast) {
		reset();
		StringFormatBuilder sb = new StringFormatBuilder();
		preVisit(sb);
		sb.append(visit(ast));
		postVisit(sb);
		return sb;
	}
	
	/* Helper Methods */
	
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
	
	private void preVisit(StringFormatBuilder sb) {
		sb.appendln("public static void main(String[] args) {");
	}
	
	private void postVisit(StringFormatBuilder sb) {
		StringFormatBuilder declarations = new StringFormatBuilder();
		Set<FunctionDeclaration> sorted = new TreeSet<>((f1, f2) -> f1.getFid() - f2.getFid());
		List<FunctionDeclaration> calls = declared.values().stream()
				.map(FunctionCall::getDeclaration)
				.collect(Collectors.toList());
		sorted.addAll(calls);
		for (FunctionDeclaration declaration : sorted) {
			nested = 0;
			appendln(declarations, "public static void %s() {", declaration.getName());
			nested = 1;
			appendln(declarations, "%s", visit(declaration.getBody()).toString().trim());
			nested = 0;
			appendln(declarations, "}");	// end of function
		}
		sb.appendln("}")	// end of main
			.prepend(declarations)
			.prependln("public static int %s = 0;%n", ptr)
			.prependln("public static int[] %s = new int[%d];", mem, cells);
	}
	
	@Override
	public StringFormatBuilder visit(Data ast) {
		StringFormatBuilder format = new StringFormatBuilder();
		if (ast.getPointerOffset() == 0)
			format.append("%s[%s]", mem, ptr);
		else
			format.append("%s[%s + %d]", mem, ptr, ast.getPointerOffset());
		if (ast.isSet())
			format.append(" = %d;", ast.getOffset());
		else {
			if (ast.getOffset() == 1)
				format.append("++;");
			else if (ast.getOffset() == -1)
				format.append("--;");
			else
				format.append(" += %d;", ast.getOffset());
		}
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
		format.append(";");
		StringFormatBuilder sb = new StringFormatBuilder();
		appendln(sb, format.toString());
		return sb;
	}
	
	@Override
	public StringFormatBuilder visit(Pointer ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		if (ast.getOffset() == 1)
			appendln(sb, "%s++;", ptr);
		else if (ast.getOffset() == -1)
			appendln(sb, "%s--;", ptr);
		else
			appendln(sb, "%s += %d;", ptr, ast.getOffset());
		return sb;
	}

	@Override
	public StringFormatBuilder visit(Input ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		if (ast.getOffset() == -1)
			appendln(sb, "%s[%s] = System.in.read();", mem, ptr);
		else
			appendln(sb, "%s[%s + %d] = System.in.read();", mem, ptr, ast.getOffset());
		return sb;
	}

	@Override
	public StringFormatBuilder visit(Output ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		if (ast.getOffset() == -1)
			appendln(sb, "System.out.print((char) %s[%s]);", mem, ptr);
		else
			appendln(sb, "System.out.print((char) %s[%s + %d]);", mem, ptr, ast.getOffset());
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
			appendln(sb, "while (%s[%s] != 0) {", mem, ptr);
		else
			appendln(sb, "while (%s[%s + %d] != 0) {", mem, ptr, ast.getPointerOffset());
		nested++;
		for (AST node : ast.getBlock())
			sb.append(visit(node));
		nested--;
		appendln(sb, "}");
		return sb;
	}

	@Override
	public StringFormatBuilder visit(FunctionCall ast) {
		StringFormatBuilder sb = new StringFormatBuilder();
		appendln(sb, "%s();", ast.getDeclaration().getName());
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