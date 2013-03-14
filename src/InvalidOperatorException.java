/**
 * Thrown to indicate than an invalid operator is specified in cell expression.
 */

public class InvalidOperatorException extends Exception {

	private static final long serialVersionUID = 1L;
	private String operator;

	public InvalidOperatorException(String operator) {
		this.operator = operator;
	}

	public String toString() {
		return "Invalid operator " + operator;
	}

}