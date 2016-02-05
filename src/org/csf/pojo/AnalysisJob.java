package org.csf.pojo;

import java.sql.Date;

public class AnalysisJob extends TableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6078690316588869844L;

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

	public Date getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(Date analysisDate) {
		this.analysisDate = analysisDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	private long stockCriteriaKey;
	private Date analysisDate;
	private String status;
	private String log;

}
