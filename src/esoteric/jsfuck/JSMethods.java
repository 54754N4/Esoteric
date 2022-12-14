package esoteric.jsfuck;

import java.util.Arrays;
import java.util.List;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetLogger;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.engine.IJavetEngine;
import com.caoccao.javet.interop.engine.JavetEngineConfig;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import com.caoccao.javet.interop.options.NodeRuntimeOptions;
import com.caoccao.javet.interop.options.V8Flags;
import com.caoccao.javet.interop.options.V8RuntimeOptions;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueBoolean;
import com.caoccao.javet.values.primitive.V8ValueDouble;
import com.caoccao.javet.values.primitive.V8ValueInteger;
import com.caoccao.javet.values.primitive.V8ValueNull;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.primitive.V8ValueUndefined;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.caoccao.javet.values.reference.V8ValueRegExp;

import esoteric.jsfuck.ast.Array;
import esoteric.jsfuck.ast.Bool;
import esoteric.jsfuck.ast.FunctionCall;
import esoteric.jsfuck.ast.Null;
import esoteric.jsfuck.ast.Num;
import esoteric.jsfuck.ast.Str;
import esoteric.jsfuck.ast.Undefined;
import model.AST;

/* We can list all of a JS array's methods using the following code:
 *	function getAllMethods(obj) {
 *		let properties = new Set()
 *		let currentObj = obj
 *		do {
 *			Object.getOwnPropertyNames(currentObj).map(item => properties.add(item))
 *		} while (currentObj = Object.getPrototypeOf(currentObj))
 *		return [...properties.keys()]
 *	}
 *
 * Refs: https://flaviocopes.com/how-to-list-object-methods-javascript
 * TODO Note: At this point with the way this class is used this could probably 
 * be simply refactored into a singleton class
 */
public interface JSMethods {
	// console.log(...getAllMethods([]).sort())
	static final List<String> ARRAYS = Arrays.asList(("__defineGetter__ __defineSetter__ __lookupGetter__ __lookupSetter__ "
			+ "at concat constructor copyWithin entries every fill filter find "
			+ "findIndex findLast findLastIndex flat flatMap forEach hasOwnProperty "
			+ "includes indexOf isPrototypeOf join keys lastIndexOf map pop "
			+ "propertyIsEnumerable push reduce reduceRight reverse shift "
			+ "slice some sort splice toLocaleString toString unshift valueOf values").split(" "));
	// console.log(...getAllMethods(Function[]).sort())
	static final List<String> FUNCTIONS = Arrays.asList(("__defineGetter__ __defineSetter__ __lookupGetter__ "
			+ "__lookupSetter__ __proto__ apply arguments bind call constructor hasOwnProperty isPrototypeOf "
			+ "length name propertyIsEnumerable toLocaleString toString valueOf").split(" "));
	// console.log(...getAllMethods("").sort())
	static final List<String> STRINGS = Arrays.asList(("__defineGetter__ __defineSetter__ __lookupGetter__ "
			+ "__lookupSetter__ __proto__ anchor at big blink bold charAt charCodeAt codePointAt concat constructor "
			+ "endsWith fixed fontcolor fontsize hasOwnProperty includes indexOf isPrototypeOf italics lastIndexOf "
			+ "length link localeCompare match matchAll normalize padEnd padStart propertyIsEnumerable repeat replace "
			+ "replaceAll search slice small split startsWith strike sub substr substring sup toLocaleLowerCase "
			+ "toLocaleString toLocaleUpperCase toLowerCase toString toUpperCase trim trimEnd trimLeft trimRight "
			+ "trimStart valueOf").split(" "));
	// console.log(...getAllMethods(0).sort())
	static final List<String> NUMBERS = Arrays.asList(("__defineGetter__ __defineSetter__ __lookupGetter__ "
			+ "__lookupSetter__ __proto__ constructor hasOwnProperty isPrototypeOf propertyIsEnumerable "
			+ "toExponential toFixed toLocaleString toPrecision toString valueOf").split(" "));
	// console.log(...getAllMethods(true).sort())
	static final List<String> BOOLEANS = Arrays.asList(("__defineGetter__ __defineSetter__ __lookupGetter__ "
			+ "__lookupSetter__ __proto__ constructor hasOwnProperty isPrototypeOf propertyIsEnumerable "
			+ "toLocaleString toString valueOf").split(" "));
	
	static final String ANONYMOUS_FUNC_FORMAT = "(function() { %s; })()";
	static final String REGEXP_CONSTRUCTOR = "/a/.constructor(\"%s\")";
	
	static JavetEnginePool<V8Runtime> JAVET_ENGINE_POOL = createEnginePool();
	
	static AST execute(String code, FunctionCall call) {
		try (IJavetEngine<V8Runtime> engine = JAVET_ENGINE_POOL.getEngine()) {
			V8Runtime v8 = engine.getV8Runtime();
			return convert(v8.getExecutor(code).execute(), call);
		} catch (JavetException e) {
			throw new RuntimeException("Error running js code : "+code);
		}
	}
	
	static AST executeFunctionConstructor(String body, FunctionCall call) {
		return execute(String.format(ANONYMOUS_FUNC_FORMAT, body), call);
	}
	
	static AST executeRegexpConstructor(String param, FunctionCall call) {
		return execute(String.format(REGEXP_CONSTRUCTOR, param), call);
	}
	
	/* Convert from Javet data type to actual AST type */
	static AST convert(V8Value value, FunctionCall call) {
		if (V8ValueNull.class.isInstance(value))
			return new Null();
		if (V8ValueUndefined.class.isInstance(value))
			return new Undefined();
		if (V8ValueBoolean.class.isInstance(value))
			return new Bool(V8ValueBoolean.class.cast(value).getValue());
		if (V8ValueDouble.class.isInstance(value))
			return new Num(V8ValueDouble.class.cast(value).getValue());
		if (V8ValueInteger.class.isInstance(value))
			return new Num(V8ValueInteger.class.cast(value).getValue());
		if (V8ValueString.class.isInstance(value) || V8ValueRegExp.class.isInstance(value))
			return new Str(value.toString());
		if (V8ValueArray.class.isInstance(value)) {
			V8ValueArray array = V8ValueArray.class.cast(value);
			Array result = new Array();
			try {
				for (int i=0; i<array.getLength(); i++)
					result.add(convert(array.get(i), call));
				return result;
			} catch (JavetException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (V8ValueFunction.class.isInstance(value))
			return call;
		throw new IllegalArgumentException("Did not implement conversion from Javet "+value+"("+value.getClass()+")");
	}
	
	// Default logger ignores everything
	static IJavetLogger defaultLogger() {
		return new IJavetLogger() {
			@Override public void debug(String message) {}
			@Override public void error(String message) {}
			@Override public void error(String message, Throwable cause) {}
			@Override public void info(String message) {}
			@Override public void warn(String message) {}
		};
	}
	
	// V8Flags settings reference: https://github.com/caoccao/Javet/discussions/187
	static JavetEnginePool<V8Runtime> createEnginePool() {
		JavetEngineConfig config = new JavetEngineConfig();
		config.setJavetLogger(defaultLogger());
		for (V8Flags v8Flags : new V8Flags[]{NodeRuntimeOptions.V8_FLAGS, V8RuntimeOptions.V8_FLAGS}) {
            if (!v8Flags.isSealed()) {
            	v8Flags.setUseStrict(false); // off to allow usage of deprecated octal numbers
                v8Flags.setAllowNativesSyntax(true);
                v8Flags.setExposeGC(false);
                v8Flags.setExposeInspectorScripts(true);
                v8Flags.setMaxHeapSize(768);
                v8Flags.setMaxOldSpaceSize(512);
                v8Flags.setTrackRetainingPath(true);
            }
        }
		return new JavetEnginePool<>(config);
	}
}