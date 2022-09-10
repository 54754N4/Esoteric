package esoteric.brainfuck.ast;

import java.util.ArrayList;

import model.AST;

public class Block extends ArrayList<AST> implements AST {
	private static final long serialVersionUID = 7600001434852073208L;

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
