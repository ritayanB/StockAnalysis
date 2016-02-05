package org.csf.pojo;

public class StockCriteriaDetail extends TableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4773993680488105374L;
	private long key;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public long getStockCriteriaKey() {
		return stockCriteriaKey;
	}

	public void setStockCriteriaKey(long stockCriteriaKey) {
		this.stockCriteriaKey = stockCriteriaKey;
	}

	public String getLeftHandSide() {
		return leftHandSide;
	}

	public void setLeftHandSide(String leftHandSide) {
		this.leftHandSide = leftHandSide;
	}

	public String getRightHandSide() {
		return rightHandSide;
	}

	public void setRightHandSide(String rightHandSide) {
		this.rightHandSide = rightHandSide;
	}

	private long stockCriteriaKey;
	private String leftHandSide;
	private String comparator;

	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	private String rightHandSide;

}
