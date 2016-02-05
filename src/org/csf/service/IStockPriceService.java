package org.csf.service;

import org.csf.pojo.StockPrice;

public interface IStockPriceService {
	
	public StockPrice[] getHistoricalValueBSE(String securityCode, String startDate, String endDate) throws Exception;
	
}
