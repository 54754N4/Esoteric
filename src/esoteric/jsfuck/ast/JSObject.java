package esoteric.jsfuck.ast;

import esoteric.jsfuck.TypeConversion;
import model.AST;

public interface JSObject extends AST, TypeConversion {
	default public boolean hasOwnProperty(Str string) {
		try {
			getClass().getDeclaredField(string.getValue());
			return true;
		} catch (NoSuchFieldException | SecurityException e) {
			return false;
		}
	}
}
