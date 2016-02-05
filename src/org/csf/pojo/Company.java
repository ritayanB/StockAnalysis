package org.csf.pojo;

import java.sql.Date;

public class Company extends TableObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int key;
	int exchangeKey;
	String name;
	Date priceLastUpdatedOn;
	String importError;
	String securityID;
	String group;
	float faceValue;
	String ISIN;
	String industry;
	int securityCode;
	String instrument;

	public String getSecurityID() {
		return securityID;
	}

	public void setSecurityID(String securityID) {
		this.securityID = securityID;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public float getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(float faceValue) {
		this.faceValue = faceValue;
	}

	public String getISIN() {
		return ISIN;
	}

	public void setISIN(String iSIN) {
		ISIN = iSIN;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public int getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(int securityCode) {
		this.securityCode = securityCode;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getExchangeKey() {
		return exchangeKey;
	}

	public void setExchangeKey(int exchangeKey) {
		this.exchangeKey = exchangeKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPriceLastUpdatedOn() {
		return priceLastUpdatedOn;
	}

	public void setPriceLastUpdatedOn(Date priceLastUpdatedOn) {
		this.priceLastUpdatedOn = priceLastUpdatedOn;
	}

	public String getImportError() {
		return importError;
	}

	public void setImportError(String importError) {
		this.importError = importError;
	}

}
