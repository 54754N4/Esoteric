package esoteric.brainfuck.ast;

import model.AST;

public class FunctionDeclaration implements AST {
	public static final String NAME_PREFIX = "func";
	private int fid;
	private AST body;
	
	public FunctionDeclaration(int fid, AST body) {
		this.fid = fid;
		this.body = body;
	}
	
	public FunctionDeclaration(int fid) {
		this(fid, null);
	}
	
	public int getFid() {
		return fid;
	}
	
	public String getName() {
		return NAME_PREFIX + fid;
	}
	
	public FunctionDeclaration setBody(AST body) {
		this.body = body;
		return this;
	}
	
	public AST getBody() {
		return body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + fid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionDeclaration other = (FunctionDeclaration) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (fid != other.fid)
			return false;
		return true;
	}
}
