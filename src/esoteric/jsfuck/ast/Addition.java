package esoteric.jsfuck.ast;

import java.util.ArrayList;

import model.AST;

public class Addition extends ArrayList<AST> implements AST {
	private static final long serialVersionUID = -3690016786780185940L;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
}
