package esoteric.brainfuck.ast;

import model.AST;

public class FunctionCall implements AST {
	private FunctionDeclaration declaration;
	
	public FunctionCall(FunctionDeclaration declaration) {
		this.declaration = declaration;
	}

	public FunctionDeclaration getDeclaration() {
		return declaration;
	}
	
	public FunctionCall setDeclaration(FunctionDeclaration declaration) {
		this.declaration = declaration;
		return this;
	}

	@Override
	public int hashCode() {
		return declaration.getBody().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionCall other = (FunctionCall) obj;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		return true;
	}
}
