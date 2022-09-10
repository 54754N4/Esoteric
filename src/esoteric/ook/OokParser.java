package esoteric.ook;

import esoteric.brainfuck.ast.Block;
import esoteric.brainfuck.ast.Data;
import esoteric.brainfuck.ast.Input;
import esoteric.brainfuck.ast.Loop;
import esoteric.brainfuck.ast.Output;
import esoteric.brainfuck.ast.Pointer;
import model.AST;
import model.Lexer;
import model.Parser;
import model.Type.Ook;

/* > Ook grammar
 * expr : atom+ | EOF ;
 * atom : 'Ook. Ook!' 
 * 		| 'Ook! Ook.' 
 * 		| 'Ook. Ook?' 
 * 		| 'Ook? Ook.' 
 * 		| 'Ook. Ook.' 
 * 		| 'Ook! Ook!' 
 * 		| loop
 * 		;
 * loop : 'Ook! Ook?' atom* 'Ook? Ook!' ;
 */
public class OokParser extends Parser<Ook> {
	
	public OokParser(Lexer<Ook> lexer) {
		super(lexer);
	}
	
	/* atom : 'Ook. Ook!' 
	 * 		| 'Ook! Ook.' 
	 * 		| 'Ook. Ook?' 
	 * 		| 'Ook? Ook.' 
	 * 		| 'Ook. Ook.' 
	 * 		| 'Ook! Ook!' 
	 * 		| loop
	 */
	private AST atom() {
		switch (getCurrent().getType()) {
			case COMMA:
				consume(Ook.COMMA);
				return new Input();
			case DOT:
				consume(Ook.DOT);
				return new Output();
			case GREATER:
				consume(Ook.GREATER);
				return new Pointer(1);
			case LOWER:
				consume(Ook.LOWER);
				return new Pointer(-1);
			case PLUS:
				consume(Ook.PLUS);
				return new Data(1);
			case MINUS:
				consume(Ook.MINUS);
				return new Data(-1);
			case LBRACKET:
				return loop();
			default: 
				return error("Invalid token "+getCurrent());
		}
	}
	
	/* loop : 'Ook! Ook?' atom* 'Ook? Ook!' ; */
	private AST loop() {
		Loop loop = new Loop();
		consume(Ook.LBRACKET);
		while (!is(Ook.RBRACKET))
			loop.getBlock().add(atom());
		consume(Ook.RBRACKET);
		return loop;
	}
	
	/* expr : atom+ | EOF */
	private AST expr() {
		Block block = new Block();
		while (!is(Ook.EOF))
			block.add(atom());
		return block;
	}
	
	@Override
	public AST parse() {
		return expr();
	}
}