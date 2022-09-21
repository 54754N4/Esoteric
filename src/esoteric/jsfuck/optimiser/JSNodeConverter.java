package esoteric.jsfuck.optimiser;

import esoteric.jsfuck.JSFuckVisitor;
import esoteric.jsfuck.JSMethods;
import esoteric.jsfuck.ast.Addition;
import esoteric.jsfuck.ast.Array;
import esoteric.jsfuck.ast.ArrayAccess;
import esoteric.jsfuck.ast.Bool;
import esoteric.jsfuck.ast.FunctionCall;
import esoteric.jsfuck.ast.FunctionDef;
import esoteric.jsfuck.ast.Null;
import esoteric.jsfuck.ast.Num;
import esoteric.jsfuck.ast.Str;
import esoteric.jsfuck.ast.Unary;
import esoteric.jsfuck.ast.Undefined;
import model.AST;
import model.Optimiser;

/* To avoid re-implementing all of javascript, the specific AST
 * nodes used in JSFuck can be directly converted to correct 
 * ASTs.
 */
public class JSNodeConverter implements JSFuckVisitor<AST>, Optimiser {
	private static final int percentage, comma, c, d, dateResult, undefinedString;
	
	static {
		/* Stores hashcodes of recurrent AST nodes that we cannot evaluate
		 * without a full blown JS interpreter. Then replaces them with the
		 * appropriate result/AST
		 */
		percentage = new ArrayAccess(
				new FunctionCall(
						new FunctionCall(
								new FunctionCall(
										new FunctionDef("Function",
												new FunctionDef("flat", new Array(), "flat"),
												"constructor"),
										new Array(new Str("return escape"))
								)
						),
						new Array(new FunctionDef("flat", new Array(), "flat"))
				),
				new Str("21")).hashCode();
		comma = new FunctionCall(
				new FunctionDef("concat", new Array(new Array()), "concat"),
				new Array(new Array(new Array()))
		).hashCode();
		c = new ArrayAccess(
				new FunctionCall(
						new FunctionCall(
								new FunctionCall(
										new FunctionDef(
												"Function", 
												new FunctionDef("flat", new Array(), "flat"),
												"constructor"),
										new Array(new Str("return escape"))
								)
						),
						new Array(new Str("<i></i>"))
				),
				new Num(2)
		).hashCode();
		d = new FunctionCall(
				new ArrayAccess(
						new FunctionCall(
								new FunctionCall(
										new FunctionCall(
												new FunctionDef(
														"Function", 
														new FunctionDef("flat", new Array(), "flat"), 
														"constructor"),
												new Array(new Str("return escape"))
										)
								),
								new Array(new FunctionDef("flat", new Array(), "flat"))
						),
						new Str("slice")
				),
				new Array(new Str("-1"))
		).hashCode();
		dateResult = new FunctionCall(
				new FunctionDef(
						"Function",
						new FunctionDef("flat", new Array(), "flat"),
						"constructor"),
				new Array(new Str("return Date"))
		).hashCode();
		undefinedString = new FunctionCall(
				new ArrayAccess(
						new ArrayAccess(
								new FunctionCall(new FunctionDef(
										"Object",
										new FunctionDef("entries", new Array(), "entries"),
										"constructor"
								)),
								new Str("toString")
						),
						new Str("call")
				)
		).hashCode();
	}
	
	@Override
	public AST visit(ArrayAccess ast) {
		int hc = ast.hashCode();
		if (hc == percentage)
			return new Str("%");
		else if (hc == c)
			return new Str("C");
		return new ArrayAccess(visit(ast.getArray()), visit(ast.getIndex()));
	}
	
	@Override
	public AST visit(FunctionCall ast) {
		int hc = ast.hashCode();
		if (hc == comma)
			return new Str(",");
		else if (hc == d)
			return new Str("D");
		else if (hc == dateResult)
			return JSMethods.execute("new Date().toString()", ast);
		else if (hc == undefinedString)
			return new Str("[object Undefined]");
		Array array = ast.getParams().stream()
				.map(this::visit)
				.collect(Array::new, Array::add, Array::addAll);
		return new FunctionCall(visit(ast.getName()), array);
	}
	
	@Override
	public AST visit(Addition ast) {
		return ast.stream()
				.map(this::visit)
				.collect(Addition::new, Addition::add, Addition::addAll);
	}

	@Override
	public AST visit(Unary ast) {
		return new Unary(ast.getOperator(), visit(ast.getOperand()));
	}

	@Override
	public AST visit(Array ast) {
		return ast.stream()
				.map(this::visit)
				.collect(Array::new, Array::add, Array::addAll);
	}

	/* Leaf node visitors don't need to be optimised */
	
	@Override
	public AST visit(FunctionDef ast) {
		return ast;
	}

	@Override
	public AST visit(Bool ast) {
		return ast;
	}

	@Override
	public AST visit(Num ast) {
		return ast;
	}

	@Override
	public AST visit(Str ast) {
		return ast;
	}

	@Override
	public AST visit(Null ast) {
		return ast;
	}

	@Override
	public AST visit(Undefined ast) {
		return ast;
	}
}