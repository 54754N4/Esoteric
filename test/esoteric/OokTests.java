package esoteric;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import esoteric.brainfuck.BFOptimiser;
import esoteric.brainfuck.ast.Block;
import esoteric.brainfuck.ast.Data;
import esoteric.brainfuck.ast.Loop;
import esoteric.brainfuck.ast.Pointer;
import esoteric.brainfuck.transpiler.CTranspiler;
import esoteric.brainfuck.transpiler.JavaTranspiler;
import esoteric.brainfuck.transpiler.PythonTranspiler;
import esoteric.ook.OokParser;
import esoteric.ook.transpiler.BFTranspiler;
import model.AST;
import model.CharStream;
import model.Lexer;
import model.Token;
import model.Type.Ook;

class OokTests {

	@Test
	void lexical_analysis_on_ook_string() {
		CharStream stream = CharStream.of("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
				+ "Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook.");
		Lexer<Ook> lexer = new Lexer<>(stream, Ook.class);
		List<Token<Ook>> tokens = new ArrayList<>();
		Token<Ook> token;
		while ((token = lexer.getNextToken()).getType() != Ook.EOF)
			tokens.add(token);
		
		Ook[] types = {
				Ook.GREATER, Ook.PLUS, Ook.PLUS, Ook.PLUS, Ook.PLUS,
				Ook.PLUS, Ook.PLUS, Ook.PLUS, Ook.PLUS, Ook.PLUS,
				Ook.LBRACKET, Ook.LOWER, Ook.PLUS, Ook.PLUS,
				Ook.PLUS, Ook.PLUS
		};
		int pos = 0;
		for (int i=0; i<tokens.size(); i++) {
			token = tokens.get(i);
			assertEquals(types[i], token.getType());
			assertEquals(pos, token.getPosition());
			pos += 10;
		}
	}
	
	@Test
	void parsing_ook_string_to_AST() {
		CharStream stream = CharStream.of("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
				+ "Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook!");
		Lexer<Ook> lexer = new Lexer<>(stream, Ook.class);
		OokParser parser = new OokParser(lexer);
		Block block = (Block) parser.parse();	// first AST element is always a block
		
		List<Class<? extends AST>> classes = Arrays.asList(
				Pointer.class, Data.class, Data.class, 
				Data.class, Data.class, Data.class, 
				Data.class, Data.class, Data.class, 
				Data.class, Loop.class
		);
		assertEquals(classes.size(), block.size());
		for (int i=0; i<block.size(); i++)
			assertEquals(classes.get(i), block.get(i).getClass());
	}

	@Test
	void transpiling_ook_to_bf() {
		CharStream stream = CharStream.of("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
				+ "Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook!");
		Lexer<Ook> lexer = new Lexer<>(stream, Ook.class);
		OokParser parser = new OokParser(lexer);
		AST ast = parser.parse();
		BFTranspiler transpiler = new BFTranspiler();
		String BF_CODE = ">+++++++++[<++++]";
		assertEquals(BF_CODE, transpiler.transpile(ast).toString());
	}
	
	@Test
	void transpiling_ook_to_java() {
		CharStream stream = CharStream.of("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
				+ "Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook!");
		Lexer<Ook> lexer = new Lexer<>(stream, Ook.class);
		OokParser parser = new OokParser(lexer);
		AST ast = parser.parse();
		int optimisations = BFOptimiser.RLE | BFOptimiser.LC | BFOptimiser.CS; // BFOptimiser.OF
		BFOptimiser optimiser = new BFOptimiser(optimisations);
		AST optimised = optimiser.visit(ast);
		JavaTranspiler transpiler = new JavaTranspiler();
		assertEquals(ExpectedOutputs.JAVA_TRANSPILED_OOK, transpiler.transpile(optimised).toString());
	}
	
	@Test
	void transpiling_ook_to_python() {
		CharStream stream = CharStream.of("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
				+ "Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook!");
		Lexer<Ook> lexer = new Lexer<>(stream, Ook.class);
		OokParser parser = new OokParser(lexer);
		AST ast = parser.parse();
		int optimisations = BFOptimiser.RLE | BFOptimiser.LC | BFOptimiser.CS; // BFOptimiser.OF
		BFOptimiser optimiser = new BFOptimiser(optimisations);
		AST optimised = optimiser.visit(ast);
		PythonTranspiler transpiler = new PythonTranspiler();
		assertEquals(ExpectedOutputs.PYTHON_TRANSPILED_OOK, transpiler.transpile(optimised).toString());
	}
	
	@Test
	void transpiling_ook_to_c() {
		CharStream stream = CharStream.of("Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
				+ "Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook!");
		Lexer<Ook> lexer = new Lexer<>(stream, Ook.class);
		OokParser parser = new OokParser(lexer);
		AST ast = parser.parse();
		int optimisations = BFOptimiser.RLE | BFOptimiser.LC | BFOptimiser.CS; // BFOptimiser.OF
		BFOptimiser optimiser = new BFOptimiser(optimisations);
		AST optimised = optimiser.visit(ast);
		CTranspiler transpiler = new CTranspiler();
		assertEquals(ExpectedOutputs.C_TRANSPILED_OOK, transpiler.transpile(optimised).toString());
	}
	
	private final class ExpectedOutputs {
		public static final String JAVA_TRANSPILED_OOK = "public static int[] mem = new int[2048];\r\n"
				+ "public static int ptr = 0;\r\n"
				+ "\r\n"
				+ "public static void func0() {\r\n"
				+ "	while (mem[ptr] != 0) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 4;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "public static void main(String[] args) {\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 9;\r\n"
				+ "	func0();\r\n"
				+ "}\r\n"
				+ "";
		public static final String PYTHON_TRANSPILED_OOK = "mem = [0] * 2048\r\n"
				+ "ptr = 0\r\n"
				+ "\r\n"
				+ "def func0():\r\n"
				+ "	global ptr, mem\r\n"
				+ "	while mem[ptr] != 0:\r\n"
				+ "		ptr += -1\r\n"
				+ "		mem[ptr] += 4\r\n"
				+ "\r\n"
				+ "ptr += 1\r\n"
				+ "mem[ptr] += 9\r\n"
				+ "func0()\r\n";
		public static final String C_TRANSPILED_OOK = "int mem[2048];\r\n"
				+ "int ptr = 0;\r\n"
				+ "\r\n"
				+ "void func0() {\r\n"
				+ "	while (mem[ptr]) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 4;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "int main(int argc, char** argv) {\r\n"
				+ "	// Initialise memory cells\r\n"
				+ "	for (int i=0; i<2048; i++)\r\n"
				+ "		mem[i] = 0;\r\n"
				+ "\r\n"
				+ "	// Driver code\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 9;\r\n"
				+ "	func0();\r\n"
				+ "	return 0;\r\n"
				+ "}\r\n";
	}
}
