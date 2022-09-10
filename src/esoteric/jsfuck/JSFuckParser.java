package esoteric.jsfuck;

import esoteric.jsfuck.ast.FunctionCall;
import esoteric.jsfuck.ast.Addition;
import esoteric.jsfuck.ast.Array;
import esoteric.jsfuck.ast.ArrayAccess;
import esoteric.jsfuck.ast.Unary;
import model.AST;
import model.Lexer;
import model.Parser;
import model.Type.JSFuck;

/* >> JSFuck grammar (extracted from real JS grammar referenced below
 * and after removing left recursions):
 * 
 * Expression: AdditiveExpression ;
 * AdditiveExpression: MemberExpression ('+' MemberExpression)* ;
 * MemberExpression: ('+'|'!') MemberExpression
 * 	| Atom ('[' Expression ']')* 
 * 	| Atom ('(' (Expression (',' Expression)*)? ')')?
 * 	; 
 * Atom: '[' Expression? ']' 
 * 	| '(' Expression ')'
 * 	;
 * 
 * Referencess: JS grammar - https://tc39.es/ecma262
 */
public class JSFuckParser extends Parser<JSFuck> {

	public JSFuckParser(Lexer<JSFuck> lexer) {
		super(lexer);
	}
	
	/* Atom: '[' Expression ']' 
	 * 	| '(' Expression ')'
	 * 	;
	 */
	private AST atom() {
		if (is(JSFuck.LBRACKET)) {
			Array array = new Array();
			consume(JSFuck.LBRACKET);
			if (!is(JSFuck.RBRACKET))
				array.add(expression());
			consume(JSFuck.RBRACKET);
			return array;
		} 
		consume(JSFuck.LPAREN);
		AST expression = expression();
		consume(JSFuck.RPAREN);
		return expression;
	}
	
	/* MemberExpression: ('+'|'!') MemberExpression
	 * 	| Atom (('[' Expression ']') | ('(' (Expression (',' Expression)*)? ')'))*
	 * 	; 													*/
	private AST member_expression() {
		if (is(JSFuck.PLUS, JSFuck.EXCLAMATION)) {
			String operator;
			if (is(JSFuck.PLUS)) {
				consume(JSFuck.PLUS);
				operator = "+";
			} else {
				consume(JSFuck.EXCLAMATION);
				operator = "!";
			}
			return new Unary(operator, member_expression());
		}
		AST atom = atom();
		while (is(JSFuck.LBRACKET, JSFuck.LPAREN)) {
			if (is(JSFuck.LBRACKET)) {	
				consume(JSFuck.LBRACKET);
				AST expression = expression();
				consume(JSFuck.RBRACKET);
				atom = new ArrayAccess(atom, expression);
			} else if (is(JSFuck.LPAREN)) {
				Array params = new Array();
				consume(JSFuck.LPAREN);
				while (!is(JSFuck.RPAREN))
					params.add(expression());
				consume(JSFuck.RPAREN);
				atom = new FunctionCall(atom, params);
			}
		}
		return atom;
	}

	/* AdditiveExpression: MemberExpression ('+' MemberExpression)* ; */
	private AST additive_expression() {
		AST lhs = member_expression();
		if (is(JSFuck.PLUS)) {
			Addition addition = new Addition();
			addition.add(lhs);
			while (is(JSFuck.PLUS)) {
				consume(JSFuck.PLUS);
				addition.add(member_expression());
			}
			lhs = addition;
		}
		return lhs;
	}
	
	/* Expression: AdditiveExpression */
	private AST expression() {
		return additive_expression();
	}
	
	@Override
	public AST parse() {
		return expression();
	}
}
