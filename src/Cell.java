import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * This class encapsulates a single cell within a spreadsheet.
 */
public class Cell {

	private Spreadsheet spreadsheet;
	private String expressionInReversePolishNotation;
	// numerical value of evaluating expression
	private double value;
	private boolean valueValid;

	/**
	 * Constructor for a cell belonging to a particular spreadsheet.
	 * 
	 * @param spreadsheet
	 *            SpreadSheet to which Cell belongs.
	 * @param expression
	 *            expression within cell.
	 */
	public Cell(Spreadsheet spreadsheet,
			String expressionInReversePolishNotation) {
		this.spreadsheet = spreadsheet;
		this.expressionInReversePolishNotation = expressionInReversePolishNotation;
	}

	/**
	 * Evaluates expression within a cell. Expression must be in reverse Polish
	 * notation.
	 * 
	 * @throws InvalidOperatorException
	 *             if cell expression contains an invalid operator.
	 */
	public void evaluate() throws InvalidOperatorException {
		if (!valueValid) {
			try {
				// create stack containing tokens in expression
				Deque<String> expressionStack = new ArrayDeque<String>(
						Arrays.asList(expressionInReversePolishNotation
								.split("\\s")));
				value = evaluateRPN(expressionStack);
				valueValid = true;
			} catch (UnresolvedReferenceException e) {
			}
		}
	}

	public double getValue() throws InvalidValueException {
		if (isValueValid()) {
			return value;
		} else {
			throw new InvalidValueException();
		}
	}

	public boolean isValueValid() {
		return valueValid;
	}

	/**
	 * Evaluate an expression contained within an ExpressionStack.
	 * 
	 * @param expressionStack
	 *            an expression represented as a stack of individual terms.
	 * @return evaluation of expression
	 * @throws InvalidOperatorException
	 * @throws UnresolvedReferenceException
	 */
	private double evaluateRPN(Deque<String> expressionStack)
			throws InvalidOperatorException, UnresolvedReferenceException {
		String token = expressionStack.removeLast();
		if (isCellReference(token)) {
			// if last token in expression is a cell reference then resolve it
			return resolveCellReference(token);
		} else {
			double x, y;
			try {
				// if last token in expression is double then return it
				x = Double.parseDouble(token);
			} catch (NumberFormatException e) {
				// otherwise last token is an operator, evaluate operands and
				// apply operator
				y = evaluateRPN(expressionStack);
				x = evaluateRPN(expressionStack);
				x = applyOperator(x, y, token);
			}
			return x;
		}
	}

	/**
	 * Apply operator to operands x and y.
	 * 
	 * @param x
	 *            first operand.
	 * @param y
	 *            second operand.
	 * @param operator
	 * @return result of operation
	 * @throws InvalidOperatorException
	 */
	private double applyOperator(double x, double y, String operator)
			throws InvalidOperatorException {
		if (operator.equals("+"))
			return x + y;
		else if (operator.equals("-"))
			return x - y;
		else if (operator.equals("*"))
			return x *= y;
		else if (operator.equals("/"))
			return x / y;
		else {
			System.out.println("It is an invalid operator: " + operator);
			throw new InvalidOperatorException(operator);
		}

	}

	private double resolveCellReference(String reference)
			throws UnresolvedReferenceException {
		int row = reference.charAt(0) - 'A';
		int col = Integer.parseInt(reference.substring(1)) - 1;
		Cell referencedCell = spreadsheet.getCell(row, col);
		try {
			return referencedCell.getValue();
		} catch (InvalidValueException e) {
			throw new UnresolvedReferenceException();
		}
	}

	private boolean isCellReference(String term) {
		return Character.isLetter(term.charAt(0));
	}

	public class UnresolvedReferenceException extends Exception {

		private static final long serialVersionUID = 1L;

	}
}