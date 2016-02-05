package org.csf.htmlfeed;

import java.util.ArrayList;

public class QueryColumn {

	private ArrayList<Integer> columnNumbers;

	private int elementCountPerRow;

	public int getElementCountPerRow() {
		return elementCountPerRow;
	}

	public void setElementCountPerRow(int elementCountPerRow) {
		this.elementCountPerRow = elementCountPerRow;
	}

	public ArrayList<Integer> getColumnNumbers() {
		return columnNumbers;
	}

	/**
	 * 
	 * @param query
	 *            If data from column number 2, 6, and 9 are needed put "2,6,9";
	 *            for all the columns put "*"
	 * @param elementCountPerRow
	 *            This should be the number of TDs in a single TRs of the target
	 *            table
	 */

	public QueryColumn(String query, int elementCountPerRow) {

		this.elementCountPerRow = elementCountPerRow;

		columnNumbers = new ArrayList<Integer>();

		if ("*".equals(query)) {
			for (int i = 0; i <= elementCountPerRow; i++) {
				columnNumbers.add(new Integer(i));
			}
		} else {
			for (String colNum : query.split(",")) {
				columnNumbers.add(Integer.parseInt(colNum.trim()));
			}
		}
	}
}
