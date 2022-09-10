package esoteric.brainfuck;

import esoteric.brainfuck.ast.Block;
import esoteric.brainfuck.ast.Data;
import esoteric.brainfuck.ast.Input;
import esoteric.brainfuck.ast.Loop;
import esoteric.brainfuck.ast.Output;
import esoteric.brainfuck.ast.Pointer;
import model.AST;
import model.Lexer;
import model.Parser;
import model.Type.Brainfuck;

/* > Brainfuck grammar
 * expr : atom+ | EOF ;
 * atom : ',' | '.' | '>' | '<' | '+' | '-' | loop ;
 * loop : '[' atom* ']' ;
 */
public class BFParser extends Parser<Brainfuck> {
	
	public BFParser(Lexer<Brainfuck> lexer) {
		super(lexer);
	}
	
	/* atom : ',' | '.' | '>' | '<' | '+' | '-' | loop ; */
	private AST atom() {
		switch (getCurrent().getType()) {
			case COMMA:
				consume(Brainfuck.COMMA);
				return new Input();
			case DOT:
				consume(Brainfuck.DOT);
				return new Output();
			case GREATER:
				consume(Brainfuck.GREATER);
				return new Pointer(1);
			case LOWER:
				consume(Brainfuck.LOWER);
				return new Pointer(-1);
			case PLUS:
				consume(Brainfuck.PLUS);
				return new Data(1);
			case MINUS:
				consume(Brainfuck.MINUS);
				return new Data(-1);
			case LBRACKET:
				return loop();
			default: 
				return error("Invalid token "+getCurrent());
		}
	}
	
	/* loop : '[' atom* ']' ; */
	private AST loop() {
		Loop loop = new Loop();
		consume(Brainfuck.LBRACKET);
		while (!is(Brainfuck.RBRACKET))
			loop.getBlock().add(atom());
		consume(Brainfuck.RBRACKET);
		return loop;
	}
	
	/* expr : atom+ | EOF ; */
	private AST expr() {
		Block block = new Block();
		while (!is(Brainfuck.EOF))
			block.add(atom());
		return block;
	}
	
	@Override
	public AST parse() {
		return expr();
	}
}