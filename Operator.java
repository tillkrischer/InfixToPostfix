public class Operator extends Term {

	private char type;
	private int precedence;
	
	public Operator(char op) {
		setType(op);
		switch(op) {
		case '(':
			precedence = 0;
			break;
		case '+': case '-':
			precedence = 1;
			break;
		case '*': case '/':
			precedence = 2;
			break;
		default:
			precedence = -1;
		}
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}
	
	public String toString() {
		return "" + type;
	}
	
	public int getPrecedence() {
		return precedence;
	}
	
	public int comparePrecedence(Operator o) {
		return precedence - o.getPrecedence();
	}
}
