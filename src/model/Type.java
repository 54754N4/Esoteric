package model;

public interface Type {
	public static enum Brainfuck {
		GREATER(">"), LOWER("<"),
		PLUS("+"), MINUS("-"),
		DOT("."), COMMA(","),
		LBRACKET("["), RBRACKET("]"),
		EOF("\0");
		
		private String representation;
		
		private Brainfuck(String representation) {
			this.representation = representation;
		}
		
		public String toString() {
			return representation;
		}
	}
	
	public static enum Ook {
		GREATER("Ook. Ook?"), LOWER("Ook? Ook."),	
		PLUS("Ook. Ook."), MINUS("Ook! Ook!"),
		DOT("Ook! Ook."), COMMA("Ook. Ook!"),
		LBRACKET("Ook! Ook?"), RBRACKET("Ook? Ook!"),
		EOF("\0");
		
		private String representation;
		
		private Ook(String representation) {
			this.representation = representation;
		}
		
		public String toString() {
			return representation;
		}
	}
	
	public static enum JSFuck {
		LBRACKET("["), RBRACKET("]"),
		LPAREN("("), RPAREN(")"),
		EXCLAMATION("!"), PLUS("+"),
		EOF("\0");
		
		private String representation;
		
		private JSFuck(String representation) {
			this.representation = representation;
		}
		
		public String toString() {
			return representation;
		}
	}
}
