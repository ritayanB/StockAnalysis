package org.csf.pojo;

import java.sql.Date;

public class StocksSelected extends TableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4857508847931710529L;
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

	public long getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(long startPrice) {
		this.startPrice = startPrice;
	}

	public long getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(long endPrice) {
		this.endPrice = endPrice;
	}

	public long getPriceChange() {
		return priceChange;
	}

	public void setPriceChange(long priceChange) {
		this.priceChange = priceChange;
	}

	public long getRisk() {
		return risk;
	}

	public void setRisk(long risk) {
		this.risk = risk;
	}

	public long getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(long companyKey) {
		this.companyKey = companyKey;
	}

	public Date getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(Date analysisDate) {
		this.analysisDate = analysisDate;
	}

	private long stockCriteriaKey;
	private long startPrice;
	private long endPrice;
	private long priceChange;
	private long risk;
	private long companyKey;
	private Date analysisDate;

}
