public class Operand extends Term {
	private double value;
	
	public Operand(double value) {
		this.setValue(value);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString() {
		return "" + value;
	}
}
