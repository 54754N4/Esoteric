package esoteric;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import esoteric.brainfuck.BFOptimiser;
import esoteric.brainfuck.BFParser;
import esoteric.brainfuck.BFPrinter;
import esoteric.brainfuck.ast.Block;
import esoteric.brainfuck.ast.Data;
import esoteric.brainfuck.ast.Loop;
import esoteric.brainfuck.ast.Multiply;
import esoteric.brainfuck.ast.Pointer;
import esoteric.brainfuck.transpiler.CTranspiler;
import esoteric.brainfuck.transpiler.JavaTranspiler;
import esoteric.brainfuck.transpiler.OokTranspiler;
import esoteric.brainfuck.transpiler.PythonTranspiler;
import esoteric.ook.OokParser;
import esoteric.ook.transpiler.BFTranspiler;
import model.AST;
import model.CharStream;
import model.Lexer;
import model.Token;
import model.Type.Brainfuck;
import model.Type.Ook;

class BrainfuckTests {

	@Test
	void lexical_analysis_on_bf_string() {
		String code = "[->+<]";
		CharStream stream = CharStream.of(code);
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		List<Token<Brainfuck>> tokens = new ArrayList<>();
		Token<Brainfuck> token;
		while ((token = lexer.getNextToken()).getType() != Brainfuck.EOF)
			tokens.add(token);
		
		Brainfuck[] types = {
				Brainfuck.LBRACKET, Brainfuck.MINUS, Brainfuck.GREATER,
				Brainfuck.PLUS, Brainfuck.LOWER, Brainfuck.RBRACKET
		};
		for (int i=0; i<tokens.size(); i++) {
			token = tokens.get(i);
			assertEquals(types[i], token.getType());
			assertEquals(i, token.getPosition());
		}
	}
	
	@Test
	void lexical_analysis_on_bf_file() {
		int HANOI_BRAINFUCK_TOKENS = 53884;
		File file = new File("resources/towerofhanoi.bf");
		CharStream stream = assertDoesNotThrow(() -> CharStream.of(file));
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		
		int count = 0;
		while (lexer.getNextToken().getType() != Brainfuck.EOF)
			count++;
		
		assertEquals(HANOI_BRAINFUCK_TOKENS, count);
	}
	
	@Test
	void parsing_bf_string_to_AST() {
		CharStream stream = CharStream.of("[->+<[->+<]]");
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		BFParser parser = new BFParser(lexer);

		AST ast = parser.parse();
		assertEquals(Loop.class, ((Block) ast).get(0).getClass());
		Block block = ((Loop) ((Block) ast).get(0)).getBlock();
		assertEquals(Data.class, block.get(0).getClass());
		assertEquals(Pointer.class, block.get(1).getClass());
		assertEquals(Data.class, block.get(2).getClass());
		assertEquals(Pointer.class, block.get(3).getClass());
		assertEquals(Loop.class, block.get(4).getClass());
	}
	
	@Test
	void multiplication_loop_should_be_optimised() {
		String code = "[->+++>+++++++<<]";
		CharStream stream = CharStream.of(code);
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		BFParser parser = new BFParser(lexer);
		int optimisations = BFOptimiser.RLE | BFOptimiser.LC | BFOptimiser.CS;
		BFOptimiser optimiser = new BFOptimiser(optimisations);
		AST optimised = optimiser.visit(parser.parse());
		Block block = (Block) optimised;
		assertEquals(3, block.size());
		assertEquals(Multiply.class, block.get(0).getClass());
		Multiply mult1 = (Multiply) block.get(0);
		assertEquals(1, mult1.getOffset());
		assertEquals(3, mult1.getFactor());
		assertEquals(Multiply.class, block.get(1).getClass());
		Multiply mult2 = (Multiply) block.get(1);
		assertEquals(2, mult2.getOffset());
		assertEquals(7, mult2.getFactor());
		assertEquals(Data.class, block.get(2).getClass());
		Data data = (Data) block.get(2);
		assertTrue(data.isSet());
		assertEquals(0, data.getOffset());
	}
	
	@Test
	void printing_bf_from_AST() {
		String code = "++++++++++[>+>+++>+++++++>++++++++++<<<<-]>>>>.<---.>+++++++++++.-----------.+.<<++.>-.>+++++++++++++.-----------------.++++++++.+++++.--------.+++++++++++++++.------------------.++++++++.";
		CharStream stream = CharStream.of(code);
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		BFParser parser = new BFParser(lexer);
		AST ast = parser.parse();
		BFPrinter printer = new BFPrinter();
		String result = printer.visit(ast).toString();
		assertEquals(code, result);
	}
	
	@Test
	void transpiling_bf_to_ook_and_back() {
		String code = "++++++++++[>+>+++>+++++++>++++++++++<<<<-]>>>>.<---.>+++++++++++.-----------.+.<<++.>-.>+++++++++++++.-----------------.++++++++.+++++.--------.+++++++++++++++.------------------.++++++++.";
		CharStream stream = CharStream.of(code);
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		BFParser parser = new BFParser(lexer);
		AST ast = parser.parse();
		int tokensBF = parser.getTokensCount();
		OokTranspiler transpiler = new OokTranspiler();
		String result = transpiler.transpile(ast).toString();
		assertEquals(ExpectedOutputs.OOK_TRANSPILED_BF, result);
		// Go back full circle
		CharStream ostream = CharStream.of(result);
		Lexer<Ook> olexer = new Lexer<>(ostream, Ook.class);
		OokParser oparser = new OokParser(olexer);
		AST oast = oparser.parse();
		int tokensOok = oparser.getTokensCount();
		BFTranspiler otranspiler = new BFTranspiler();
		result = otranspiler.transpile(oast).toString();
		assertEquals(tokensBF, tokensOok);
		assertEquals(ast.hashCode(), oast.hashCode());
		assertEquals(code, result);
	}
	
	@Test
	void transpiling_bf_to_java() {
		String code = "++++++++++[>+>+++>+++++++>++++++++++<<<<-]>>>>.<---.>+++++++++++.-----------.+.<<++.>-.>+++++++++++++.-----------------.++++++++.+++++.--------.+++++++++++++++.------------------.++++++++.";
		CharStream stream = CharStream.of(code);
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		BFParser parser = new BFParser(lexer);
		int optimisations = BFOptimiser.RLE | BFOptimiser.LC | BFOptimiser.CS; // BFOptimiser.OF
		BFOptimiser optimiser = new BFOptimiser(optimisations);
		AST optimised = optimiser.visit(parser.parse());
		JavaTranspiler transpiler = new JavaTranspiler();
		assertEquals(ExpectedOutputs.JAVA_TRANSPILED_BF, transpiler.transpile(optimised).toString());
	}
	
	@Test
	void transpiling_bf_to_python() {
		String code = "++++++++++[>+>+++>+++++++>++++++++++<<<<-]>>>>.<---.>+++++++++++.-----------.+.<<++.>-.>+++++++++++++.-----------------.++++++++.+++++.--------.+++++++++++++++.------------------.++++++++.";
		CharStream stream = CharStream.of(code);
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		BFParser parser = new BFParser(lexer);
		int optimisations = BFOptimiser.RLE | BFOptimiser.LC | BFOptimiser.CS; // BFOptimiser.OF
		BFOptimiser optimiser = new BFOptimiser(optimisations);
		AST optimised = optimiser.visit(parser.parse());
		PythonTranspiler transpiler = new PythonTranspiler();
		assertEquals(ExpectedOutputs.PYTHON_TRANSPILED_BF, transpiler.transpile(optimised).toString());
	}
	
	@Test
	void transpiling_bf_to_c() {
		String code = "++++++++++[>+>+++>+++++++>++++++++++<<<<-]>>>>.<---.>+++++++++++.-----------.+.<<++.>-.>+++++++++++++.-----------------.++++++++.+++++.--------.+++++++++++++++.------------------.++++++++.";
		CharStream stream = CharStream.of(code);
		Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
		BFParser parser = new BFParser(lexer);
		int optimisations = BFOptimiser.RLE | BFOptimiser.LC | BFOptimiser.CS; // BFOptimiser.OF
		BFOptimiser optimiser = new BFOptimiser(optimisations);
		AST optimised = optimiser.visit(parser.parse());
		CTranspiler transpiler = new CTranspiler();
		assertEquals(ExpectedOutputs.C_TRANSPILED_BF, transpiler.transpile(optimised).toString());
	}
	
	private final class ExpectedOutputs {
		public static final String JAVA_TRANSPILED_BF = "public static int[] mem = new int[2048];\r\n"
				+ "public static int ptr = 0;\r\n"
				+ "\r\n"
				+ "public static void func0() {\r\n"
				+ "	while (mem[ptr] != 0) {\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]++;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr] += 3;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr] += 7;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr] += 10;\r\n"
				+ "		ptr += -4;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "public static void main(String[] args) {\r\n"
				+ "	mem[ptr] += 10;\r\n"
				+ "	func0();\r\n"
				+ "	ptr += 4;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr] += -3;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 11;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += -11;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr += -2;\r\n"
				+ "	mem[ptr] += 2;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr]--;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 13;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += -17;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += 8;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += 5;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += -8;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += 15;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += -18;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += 8;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "}\r\n"
				+ "";
		public static final String OOK_TRANSPILED_BF = "Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook. Ook? Ook. Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook! Ook! Ook? Ook! Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook? Ook! Ook. Ook? Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook. Ook! Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook. Ook? Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. ";
		public static final String PYTHON_TRANSPILED_BF = "mem = [0] * 2048\r\n"
				+ "ptr = 0\r\n"
				+ "\r\n"
				+ "def func0():\r\n"
				+ "	global ptr, mem\r\n"
				+ "	while mem[ptr] != 0:\r\n"
				+ "		ptr += 1\r\n"
				+ "		mem[ptr] += 1\r\n"
				+ "		ptr += 1\r\n"
				+ "		mem[ptr] += 3\r\n"
				+ "		ptr += 1\r\n"
				+ "		mem[ptr] += 7\r\n"
				+ "		ptr += 1\r\n"
				+ "		mem[ptr] += 10\r\n"
				+ "		ptr += -4\r\n"
				+ "		mem[ptr] += -1\r\n"
				+ "\r\n"
				+ "mem[ptr] += 10\r\n"
				+ "func0()\r\n"
				+ "ptr += 4\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += -1\r\n"
				+ "mem[ptr] += -3\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += 1\r\n"
				+ "mem[ptr] += 11\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += -11\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 1\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += -2\r\n"
				+ "mem[ptr] += 2\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += 1\r\n"
				+ "mem[ptr] += -1\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += 1\r\n"
				+ "mem[ptr] += 13\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += -17\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 8\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 5\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += -8\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 15\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += -18\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 8\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n";
		public static final String C_TRANSPILED_BF = "int mem[2048];\r\n"
				+ "int ptr = 0;\r\n"
				+ "\r\n"
				+ "void func0() {\r\n"
				+ "	while (mem[ptr]) {\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]++;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr] += 3;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr] += 7;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr] += 10;\r\n"
				+ "		ptr += -4;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "int main(int argc, char** argv) {\r\n"
				+ "	// Initialise memory cells\r\n"
				+ "	for (int i=0; i<2048; i++)\r\n"
				+ "		mem[i] = 0;\r\n"
				+ "\r\n"
				+ "	// Driver code\r\n"
				+ "	mem[ptr] += 10;\r\n"
				+ "	func0();\r\n"
				+ "	ptr += 4;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr] += -3;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 11;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += -11;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr += -2;\r\n"
				+ "	mem[ptr] += 2;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr]--;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 13;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += -17;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += 8;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += 5;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += -8;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += 15;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += -18;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += 8;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	return 0;\r\n"
				+ "}\r\n";
	}
}