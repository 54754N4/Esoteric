package esoteric.jsfuck.optimiser;

import java.lang.reflect.Method;

import esoteric.jsfuck.JSFuckVisitor;
import esoteric.jsfuck.JSMethods;
import esoteric.jsfuck.TypeConversion;
import esoteric.jsfuck.ast.Addition;
import esoteric.jsfuck.ast.Array;
import esoteric.jsfuck.ast.Array.EntryIterator;
import esoteric.jsfuck.ast.Array.KeysIterator;
import esoteric.jsfuck.ast.Array.ValuesIterator;
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

/* - Merges primitives using binary addition and converts primitives
 * using unary plus
 * - Replaces array accesses with the correct index element or with a function
 * definition if the method exists for target object
 * - Executes nested function calls if possible
 */
public class JSPrimitivesMerger implements JSFuckVisitor<AST>, TypeConversion, Optimiser {
 	
	@Override
	public AST visit(Addition ast) {
		Addition addition = new Addition();
		// Addition always has a minimum of 2 elements
		AST left = visit(ast.get(0)), right;
		boolean added = false;
		for (int i=1; i < ast.size(); i++) {
			right = visit(ast.get(i));
			if (isAddable(left) && isAddable(right)) {
				left = additionMerge(left, right);
				added = false;
			} else {	// can't be merged
				addition.add(left);
				left = right;
				if (i+1 < ast.size())	// if it's last element it wouldn't have been added
					added = true;
			}
		}
		if (!added) {
			if (addition.size() == 0)
				return left;
			addition.add(left);
		}
		// if everything merged to a single element, remove useless addition node
		if (addition.size() == 1)
			return addition.get(0);
		return addition;
	}

	@Override
	public AST visit(Unary ast) {
		AST value = visit(ast.getOperand());
		if (is(value, Bool.class, Num.class, Str.class, Array.class))
			return ast.isNot() ? toBool(value).flip() : toNumber(value);
		else
			return ast;
	}
	
	@Override
	public AST visit(ArrayAccess ast) {
		AST left = visit(ast.getArray());
		AST index = visit(ast.getIndex());
		if (Array.class.isInstance(left)) {
			Array array = Array.class.cast(left);
			if (Str.class.isInstance(index)) {
				Str i = Str.class.cast(index);
				if (JSMethods.ARRAYS.contains(i.getValue())) {
					String name = i.getValue();
					if (name.equals("constructor"))
						name = "Array";
					return new FunctionDef(name, array, i.getValue());
				}
			}
			Num i = toNumber(index);
			if (i.isIndexable() && i.isInteger() && i.getIntValue() < array.size())
				return array.get((int) i.getDoubleValue());
			else
				return new Undefined();
		} else if (Str.class.isInstance(left)) {
			Str str = Str.class.cast(left);
			if (Str.class.isInstance(index)) {
				Str i = Str.class.cast(index);
				if (JSMethods.STRINGS.contains(i.getValue())) {
					String name = i.getValue();
					if (name.equals("constructor")) {
						if (str.getValue().startsWith("/") && str.getValue().endsWith("/"))
							name = "RegExp";
						else 
							name = "String";
					}
					return new FunctionDef(name, str, i.getValue());
				}
			}
			Num i = toNumber(index);
			if (i.isIndexable() && i.isInteger())
				return new Str("" + str.getValue().charAt((int) i.getDoubleValue()));
		} else if (Num.class.isInstance(left)) {
			Num num = Num.class.cast(left);
			if (Str.class.isInstance(index)) {
				Str i = Str.class.cast(index);
				if (JSMethods.NUMBERS.contains(i.getValue())) {
					String name = i.getValue();
					if (name.equals("constructor"))
						name = "Number";
					return new FunctionDef(name, num, i.getValue());
				}
			}
		} else if (Bool.class.isInstance(left)) {
			Bool bool = Bool.class.cast(left);
			if (Str.class.isInstance(index)) {
				Str i = Str.class.cast(index);
				if (JSMethods.BOOLEANS.contains(i.getValue())) {
					String name = i.getValue();
					if (name.equals("constructor"))
						name = "Boolean";
					return new FunctionDef(name, bool, i.getValue());
				}
			}
		} else if (FunctionDef.class.isInstance(left)) {
			FunctionDef def = FunctionDef.class.cast(left);
			if (Str.class.isInstance(index)) {
				Str str = Str.class.cast(index);
				String name = str.getValue();
				if (name.equals("constructor")) 
					name = "Function";
				else if (name.equals("name"))
					return new Str(def.getName());
				return new FunctionDef(name, def, str.getValue());
			} 
		} else if (Array.EntryIterator.class.isInstance(left) 
				|| Array.KeysIterator.class.isInstance(left)
				|| Array.ValuesIterator.class.isInstance(left)) {
			if (Str.class.isInstance(index)) {
				Str str = Str.class.cast(index);
				String name = str.getValue();
				if (name.equals("constructor")) {
					FunctionDef parent = new FunctionDef("entries", new Array(), "entries");
					return new FunctionDef("Object", parent, str.getValue());
				}
			}
			return new Undefined();
		}
		return new ArrayAccess(left, index);
	}
	
	@Override
	public AST visit(FunctionCall ast) {
		AST funcName = visit(ast.getName());
		Array params = ast.getParams()
				.stream()
				.map(this::visit)
				.collect(Array::new, Array::add, Array::addAll);
		FunctionCall call = new FunctionCall(funcName, params);
		if (FunctionDef.class.isInstance(funcName)) {
			FunctionDef def = FunctionDef.class.cast(funcName);	
			Class<? extends AST> target = def.getParent().getClass();
			String function = def.getName();
			/* Set parameter class types to AST explicitly (since Class::getDeclaredMethod 
			 * doesn't handle covarient parameter types, e.g. throws NoSuchMethodException) */
			@SuppressWarnings("unchecked")
			Class<AST>[] paramsClass = new Class[params.size()];
			for (int i=0; i<params.size(); i++)
				paramsClass[i] = AST.class;
			if (function.equals("toString"))	// since java prevents overriding return type of toString
				function = "toStringJS";
			String param = "";
			try {
				// Assumption: parameter for function/regexp constructors should almost always be a string
				if ((function.equals("Function") || function.equals("RegExp")) && params.size() == 1)
					param = Str.class.cast(params.get(0)).getValue();
				if (function.equals("Function"))
					return JSMethods.executeFunctionConstructor(param, call);
				else if (function.equals("RegExp"))
					return JSMethods.executeRegexpConstructor(param, call);
				else {
					Method method = target.getDeclaredMethod(function, paramsClass);
					return (AST) method.invoke(def.getParent(), params.toArray());
				}
			} catch (Exception e) {
				// If it can't be executed then return FunctionCall as is
				return call;
			}
		} else if (!FunctionCall.class.isInstance(funcName) &&
				!ArrayAccess.class.isInstance(funcName) &&
				params.size() == 0)
			return funcName;
		return call;
	}
	
	@Override
	public AST visit(Array ast) {
		return ast.stream()
				.map(this::visit)
				.collect(Array::new, Array::add, Array::addAll);
	}
	
	/* Helper methods */
	
	private AST additionMerge(AST left, AST right) {
		// Coerce into string addition if any parameter is a string
		if (Str.class.isInstance(left) || Str.class.isInstance(right))
			return new Str(toString(left).getValue() + toString(right).getValue());
		// arrays and functions force string type coercion
		else if ((Bool.class.isInstance(left) && Bool.class.isInstance(right)) || 
				isAndIsNot(left, right, Num.class, Array.class, FunctionDef.class, Array.EntryIterator.class, Array.ValuesIterator.class, Array.KeysIterator.class) ||
				isAndIsNot(right, left, Num.class, Array.class, FunctionDef.class, Array.EntryIterator.class, Array.ValuesIterator.class, Array.KeysIterator.class))
			return new Num(toNumber(left).getDoubleValue() + toNumber(right).getDoubleValue());
		else // not numbers/strings/bool, so just coerce both into string + concatenate
			return new Str(toString(left).getValue() + toString(right).getValue());
	}
	
	@SafeVarargs
	private boolean isAndIsNot(AST left, AST right, Class<? extends AST> leftCls, Class<? extends AST>...notRightCls) {
		boolean test = leftCls.isInstance(left);
		if (!test)
			return test;
		for (Class<? extends AST> rightCls : notRightCls) // right shouldn't be those classes
			test = test && !rightCls.isInstance(right);
		return test;
	} 
	
	private boolean isAddable(AST ast) {
		return is(ast, 
				Undefined.class,
				Bool.class, 
				Num.class, 
				Str.class, 
				Array.class, 
				FunctionDef.class,
				EntryIterator.class,
				ValuesIterator.class,
				KeysIterator.class);
	}
	
	@SafeVarargs
	private boolean is(AST ast, Class<? extends AST>...classes) {
		for (Class<? extends AST> cls : classes)
			if (cls.isInstance(ast))
				return true;
		return false;
	}
	
	/* Child AST nodes don't need to be optimised */

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