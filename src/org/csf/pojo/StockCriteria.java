package org.csf.pojo;

public class StockCriteria extends TableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8430727427798115179L;

	private long key;
	private String name;
	private String query;
	private long investmentWeek;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public long getInvestmentWeek() {
		return investmentWeek;
	}

	public void setInvestmentWeek(long investmentWeek) {
		this.investmentWeek = investmentWeek;
	}

}
