package error;

public class InterpreterException extends RuntimeException {
	private static final long serialVersionUID = 7082652841642199148L;

	public InterpreterException(String message) {
		super("INTERPRETER: "+message);
	}
}
