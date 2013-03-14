import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A spreadsheet which is a collection of cells together
 */
public class Spreadsheet {

	@SuppressWarnings("unused")
	// number of rows in the spreadsheet
	private int nRows; 
	// number of columns, must be less than or equal to 26
	private int nCols; 
	// All the cells
	private ArrayList<Cell> cells; 

	// order

	/**
	 * Constructor
	 * 
	 * @param nRows
	 *            number of rows 
	 * @param nCols
	 *            number of columns 
	 * @param exprArray
	 *            an array of Strings containing the expressions for all cells
	 *            of the spreadsheet specified row by row.
	 */
	public Spreadsheet(int nRows, int nCols, String[] exprArray) {
		this.nRows = nRows;
		this.nCols = nCols;
		cells = new ArrayList<Cell>(exprArray.length);
		for (String expressionString : exprArray) {
			cells.add(new Cell(this, expressionString));
		}
	}

	/**
	 * Solve a spreadsheet and return the values of each cell.
	 * 
	 * @return array of doubles containing the solution to each cell row by
	 *         row.
	 * @throws CyclicReferenceException
	 *             if spreadsheet contains a Cyclic reference, it will throw a exception
	 */
	public Double[] calculate() throws CyclicReferenceException {
		// number of valid cells with valid numbers
		int validCells = 0; 
		int validCellsPreviousIteration = 0; 
		try {
			while (validCells < cells.size()) {
				// evaluate each cell in the spreadsheet
				evaluate(); 
				validCells = countValidCells();
				if (validCells == validCellsPreviousIteration) {
					System.out
							.println("There contains a cyclic reference in the input file.");
					throw new CyclicReferenceException(); 
				} else {
					validCellsPreviousIteration = validCells;
				}
			}
		} catch (InvalidOperatorException e) {
			System.out.println(e);
		}
		try {
			return getValues();
		} catch (InvalidValueException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public Cell getCell(int row, int column) {
		return cells.get(row * nCols + column);
	}

	private Double[] getValues() throws InvalidValueException {
		Double[] values = new Double[cells.size()];
		int i = 0;
		for (Cell cell : cells) {
			values[i++] = cell.getValue();
		}
		return values;
	}

	private int countValidCells() {
		int validCells = 0;
		for (Cell cell : cells) {
			if (cell.isValueValid()) {
				validCells++;
			}
		}
		return validCells;
	}

	private void evaluate() throws InvalidOperatorException {
		for (Cell cell : cells) {
			cell.evaluate();
		}
	}

	public class CyclicReferenceException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	public class UnresolvedReferenceException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	
	public static void main(String[] args) throws IOException {
		
		/*String[] exprArray = new String[6];
    	exprArray[0] = "A2";
    	exprArray[1] = "4 5 *";
    	exprArray[2] = "A1";
    	exprArray[3] = "A1 B2 / 2 +";
    	exprArray[4] = "3";
    	exprArray[5] = "39 B1 B2 * /";
    	Spreadsheet ss = new Spreadsheet(2, 3, exprArray);
    	System.out.println("3 2");
    	Double[] results = ss.dump();
    	for(double x : results){
    		System.out.println(String.format("%.5f", x));
    	}*/

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		ArrayList<String> al = new ArrayList<String>();

		
		s = br.readLine();
		//System.out.println(s);

		String[] temp = s.split(" ");
		if (temp.length != 2) {
			System.out
					.println("The input file is not well formatted. Please check the row and col numbers.");
		} else {
			// Calculate the cells and print them.
			int cols = Integer.parseInt(temp[0]);
			int rows = Integer.parseInt(temp[1]);

			while ((s = br.readLine()) != null) {
				al.add(s.toUpperCase().trim());
			}
			br.close();
			String[] exprArray = new String[al.size()];
			exprArray = al.toArray(exprArray);
			Spreadsheet ss = new Spreadsheet(rows, cols, exprArray);
			
			// print the number of rows and cols which are on the first line of stdin
			System.out.println(cols + " " + rows);
			Double[] results = ss.calculate();
			for (double x : results) {
				System.out.println(String.format("%.5f", x));
			}
		}
	}
}