package org.csf.pojo;

public class CompanyForUpdate extends TableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5632770687466589713L;

	String SecurityCode;
	String StartDate;
	String EndDate;
	long key;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getSecurityCode() {
		return SecurityCode;
	}

	public void setSecurityCode(String securityCode) {
		SecurityCode = securityCode;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

}
