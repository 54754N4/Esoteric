package run;

import esoteric.brainfuck.BFCompiler;
import esoteric.brainfuck.BFOptimiser;
import esoteric.brainfuck.BFParser;
import esoteric.brainfuck.transpiler.CTranspiler;
import esoteric.brainfuck.transpiler.JavaTranspiler;
import esoteric.brainfuck.transpiler.OokTranspiler;
import esoteric.brainfuck.transpiler.PythonTranspiler;
import esoteric.jsfuck.JSFuckCompiler;
import esoteric.jsfuck.JSFuckOptimiser;
import esoteric.jsfuck.JSFuckParser;
import esoteric.jsfuck.transpiler.JSFuckDeobfuscator;
import esoteric.ook.OokCompiler;
import esoteric.ook.OokParser;
import esoteric.ook.transpiler.BFTranspiler;
import model.AST;
import model.CharStream;
import model.Lexer;
import model.StringFormatBuilder;
import model.Transpiler;
import model.Type.Brainfuck;
import model.Type.JSFuck;
import model.Type.Ook;

public interface Esoteric {
	public static enum Input { Brainfuck, Ook, JSFuck; }
	public static enum Output { Brainfuck, Ook, C, Java, Python, JS }
	
	/**
	 * Transpiles an AST from an input language to a specified output language target
	 * @param stream - stream of characters backed by string or file
	 * @param in - input language to interpret
	 * @param out - target language to transpile to
	 * @return transpiled code inside StringFormatBuilder
	 */
	static StringFormatBuilder transpile(CharStream stream, Input in, Output out) {
		AST ast = interpret(stream, in, out);
		Transpiler transpiler = getTranspiler(in, out);
		return transpiler.transpile(ast);
	}
	
	/**
	 * Converts a character stream into the appropriate AST
	 * @param stream - stream of characters backed by string or file
	 * @param in - input language to interpret
	 * @param out - target language to transpile to
	 * @return ast - built abstract syntax tree
	 */
	static AST interpret(CharStream stream, Input in, Output out) {
		switch (in) {
			case Brainfuck: {
				Lexer<Brainfuck> lexer = new Lexer<>(stream, Brainfuck.class);
				BFParser parser = new BFParser(lexer);
				if (out == Output.Ook)	// Ook doesn't support optimisations
					return parser.parse();
				BFOptimiser optimiser = new BFOptimiser();
				return optimiser.visit(parser.parse());
			} case Ook: {
				Lexer<Ook> lexer = new Lexer<>(stream, Ook.class);
				OokParser parser = new OokParser(lexer);
				if (out == Output.Brainfuck) // Brainfuck doesn't support optimisations
					return parser.parse();
				BFOptimiser optimiser = new BFOptimiser();
				return optimiser.visit(parser.parse());
			} case JSFuck: {
				Lexer<JSFuck> lexer = new Lexer<>(stream, JSFuck.class);
				JSFuckParser parser = new JSFuckParser(lexer);
				JSFuckOptimiser optimiser = new JSFuckOptimiser();
				return optimiser.visit(parser.parse());
			} default: // unreachable code
				return error("Invalid input language " + in);
		}
	}
	
	/**
	 * Converts text into target language specified
	 * @param input - text to convert
	 * @param out - target language to convert into
	 * @return converted text
	 */
	static String compile(String input, Input out) {
		switch (out) {
			case Brainfuck: return BFCompiler.encode(input);
			case Ook: return OokCompiler.encode(input);
			case JSFuck: return JSFuckCompiler.encode(input);
			default:
				return error("No compiler for language: " + out);
		}
	}
	
	/* Convenience methods */
	
	static Transpiler getTranspiler(Input in, Output out) {
		switch (out) {
			case Brainfuck:
				if (in == Input.Brainfuck)
					error("Why do you even want to transpile Brainfuck to itself..");
				if (in == Input.JSFuck)
					error("Cannot transpile JSFuck to Brainfuck");
				return new BFTranspiler();
			case C:
				if (in == Input.JSFuck)
					error("Cannot transpile JSFuck to C");
				return new CTranspiler();
			case Java:
				if (in == Input.JSFuck)
					error("Cannot transpile JSFuck to Java");
				return new JavaTranspiler();
			case Ook:
				if (in == Input.Ook)
					error("Why do you even want to transpile Ook! to itself..");
				if (in == Input.JSFuck)
					error("Cannot transpile JSFuck to Ook");
				return new OokTranspiler();
			case Python:
				if (in == Input.JSFuck)
					error("Cannot transpile JSFuck to Python");
				return new PythonTranspiler();
			case JS:
				if (in == Input.Brainfuck)
					error("Cannot transpile Brainfuck to JS");
				if (in == Input.Ook)
					error("Cannot transpile Ook to JS");
				return new JSFuckDeobfuscator();
			default: // unreachable code
				return error("Unrecognized output target " + out);
		}
	}
	
	static <T> T error(String message) {
		throw new IllegalArgumentException(message);
	}
}