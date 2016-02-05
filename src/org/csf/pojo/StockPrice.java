package org.csf.pojo;

import java.sql.Date;

public class StockPrice extends TableObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long key;
	long companyKey;
	Date tradeDate;
	float openPrice;
	float closePrice;
	float highPrice;
	float lowPrice;
	long volume;

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public float getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(float closePrice) {
		this.closePrice = closePrice;
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public long getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(long companyKey) {
		this.companyKey = companyKey;
	}

	public Date getTradeDate() {
		return new Date(tradeDate.getTime());
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public float getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(float openPrice) {
		this.openPrice = openPrice;
	}

	public float getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(float highPrice) {
		this.highPrice = highPrice;
	}

	public float getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
	}
}
