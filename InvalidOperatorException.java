public class InvalidOperatorException extends Exception {
	
	private char operator;

	public InvalidOperatorException(char c) {
		super();
		operator = c;
	}

	public char getOperator() {
		return operator;
	}
}
