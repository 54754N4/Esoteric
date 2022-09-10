package esoteric.jsfuck;

import esoteric.jsfuck.ast.Array;
import esoteric.jsfuck.ast.Bool;
import esoteric.jsfuck.ast.FunctionDef;
import esoteric.jsfuck.ast.Infinity;
import esoteric.jsfuck.ast.NaN;
import esoteric.jsfuck.ast.Null;
import esoteric.jsfuck.ast.Num;
import esoteric.jsfuck.ast.Str;
import esoteric.jsfuck.ast.Undefined;
import model.AST;

/* - JS conversion table:	https://www.w3schools.com/js/js_type_conversion.asp
 * - Coercions grid:		https://getify.github.io/coercions-grid/
 * - Equality table:		https://www.oreilly.com/library/view/you-dont-know/9781491905159/assets/tygr_0401.png
 */
public interface TypeConversion {
	static final String FUNCTION_STRING_COERCE_FORMAT = "function %s() {\n    [native code]\n}";
	
	default <T> T error(String format, Object...args) {
		throw new IllegalStateException(String.format(format, args));
	}
	
	// https://262.ecma-international.org/13.0/#table-toboolean-conversions
	default Bool toBool(AST ast) {
		if (Bool.class.isInstance(ast))
			return Bool.class.cast(ast);
		else if (Num.class.isInstance(ast))
			return Num.class.cast(ast).getDoubleValue() == 0 ? Bool.FALSE : Bool.TRUE; 
		else if (Str.class.isInstance(ast))
			return Str.class.cast(ast).getValue().equals("") ? Bool.FALSE : Bool.TRUE;
		else if (NaN.class.isInstance(ast))
			return Bool.FALSE;
		else if (Infinity.class.isInstance(ast))
			return Bool.TRUE;
		else if (Array.class.isInstance(ast))
			return Bool.TRUE;
		else if (Null.class.isInstance(ast))
			return Bool.FALSE;
		else if (Undefined.class.isInstance(ast))
			return Bool.FALSE;
		else	// any other object returns true
			return Bool.TRUE;
	}
	
	// https://262.ecma-international.org/13.0/#table-tonumber-conversions
	default Num toNumber(AST ast) {
		if (Num.class.isInstance(ast))
			return Num.class.cast(ast);
		else if (Undefined.class.isInstance(ast))
			return new NaN();
		else if (Infinity.class.isInstance(ast))
			return Infinity.class.cast(ast);
		else if (Bool.class.isInstance(ast))
			return new Num(Bool.class.cast(ast).getValue() ? 1 : 0);
		else if (Str.class.isInstance(ast)) {
			String str = Str.class.cast(ast).getValue();
			if (str.equals(""))
				return new Num(0);
			try {
				return new Num(Integer.parseInt(str));
			} catch (Exception e) {
				return new NaN();
			}
		} else if (Array.class.isInstance(ast)) {
			Array array = Array.class.cast(ast);
			if (array.size() == 0)
				return new Num(0);
			else if (array.size() == 1) {
				AST node = array.get(0);
				if (Str.class.isInstance(node)) {
					Str str = Str.class.cast(node);
					boolean isInteger = Num.isInteger(str.getValue());
					if (Num.isDouble(str.getValue()) || isInteger)
						return new Num(str.getValue(), isInteger);
				} else if (Num.class.isInstance(node)) {
					Num n = Num.class.cast(node);
					if (n.isDouble() || n.isInteger())
						return new Num(n.getValue(), n.isInteger());
				}
			}
			return new NaN();
		} else if (Null.class.isInstance(ast))
			return new Num(0);
		else
			return new NaN();
	}
	
	// https://262.ecma-international.org/13.0/#table-tostring-conversions
	default Str toString(AST ast) {
		if (Str.class.isInstance(ast))
			return Str.class.cast(ast);
		else if (Bool.class.isInstance(ast))
			return new Str(Bool.class.cast(ast).getValue() ? "true" : "false");
		else if (Num.class.isInstance(ast))
			return new Str(Num.class.cast(ast).getValue());
		else if (NaN.class.isInstance(ast))
			return new Str(NaN.class.cast(ast).getValue());
		else if (Infinity.class.isInstance(ast))
			return new Str(Infinity.class.cast(ast).getValue());
		else if (Array.class.isInstance(ast)) {
			Array array = Array.class.cast(ast);
			StringBuilder sb = new StringBuilder();
			for (AST element : array) 
				sb.append(toString(element).getValue());
			return new Str(sb.toString());
		} else if (Null.class.isInstance(ast))
			return new Str("null");
		else if (Undefined.class.isInstance(ast))
			return new Str("undefined");
		else if (FunctionDef.class.isInstance(ast)) {
			String name = FunctionDef.class.cast(ast).getName();
			return new Str(String.format(FUNCTION_STRING_COERCE_FORMAT, name));
		} else if (Array.EntryIterator.class.isInstance(ast)
				|| Array.KeysIterator.class.isInstance(ast)
				|| Array.ValuesIterator.class.isInstance(ast))
			return new Str("[object Array Iterator]");
		else if (Array.Entry.class.isInstance(ast))
			return new Str("[object Object]");
		else
			return error("Cannot convert to string type: %s", ast);
	}
}
