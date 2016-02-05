package org.csf.htmlfeed;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.csf.pojo.StockPrice;
import org.csf.service.IStockPriceService;
import org.csf.statics.StaticValues;

public class GoogleFinanceReader extends HTMLFeedReader implements IStockPriceService {

	@Override
	public StockPrice[] getHistoricalValueBSE(String securityCode, String startDate, String endDate) throws Exception {

		long startTime = System.currentTimeMillis();
		int recordStartNumber = 0;
		ArrayList<String> rr;
		ArrayList<String> retRR = new ArrayList<String>();

		StockPrice[] ret;

		HTMLReader reader = new HTMLReader();

		StringBuffer webQuery = new StringBuffer("https://www.google.com/finance/historical?q=BOM%3A");
		webQuery.append(String.valueOf(securityCode));
		webQuery.append(String.valueOf("&startdate="));
		webQuery.append(startDate);
		webQuery.append(String.valueOf("&enddate="));
		webQuery.append(endDate);
		webQuery.append(String.valueOf("&num=200&start="));

		for (int i = 0; i < 10; i++) {
			recordStartNumber = 200 * i;
			rr = reader.getTableRowsFromURL(webQuery.toString() + String.valueOf(recordStartNumber),
					new ElementProperties(ElementProperties.STYLE_CLASS, ElementProperties.TYPE_TABLE,
							"historical_price"),
					new QueryColumn("*", 6));
			if(rr.isEmpty()){
				break;
			}else{
				retRR.addAll(rr);
				if((rr.size()/6) < 200){
					break;
				}
			}
		}

		ret = new StockPrice[retRR.size() / 6];
		int index = 0;

		for (int counter = 0; counter < ret.length; counter++) {
			ret[counter] = new StockPrice();
			ret[counter].setTradeDate(getDate(retRR.get(index++)));
			ret[counter].setOpenPrice(Float.parseFloat(retRR.get(index++).replace(",", "").replace("-", "0")));
			ret[counter].setHighPrice(Float.parseFloat(retRR.get(index++).replace(",", "").replace("-", "0")));
			ret[counter].setLowPrice(Float.parseFloat(retRR.get(index++).replace(",", "").replace("-", "0")));
			ret[counter].setClosePrice(Float.parseFloat(retRR.get(index++).replace(",", "").replace("-", "0")));
			ret[counter].setVolume(Long.parseLong(retRR.get(index++).replace(",", "").replace("-", "0")));
		}

		System.out.println("HTML Data pull done in " + ((System.currentTimeMillis() - startTime) / 1000)
				+ " seconds for security code: " + securityCode + "; pulled record count : " + ret.length);
		if(StaticValues.NUMBER_OF_RECORDS_FOR_TREND_UPDATE < ret.length){
			StaticValues.NUMBER_OF_RECORDS_FOR_TREND_UPDATE = ret.length;
		}
		return ret;
	}

	private Date getDate(String str) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(str.substring((str.length() - 4), (str.length()))));
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.substring(4, str.indexOf(","))));
		calendar.set(Calendar.MONTH, getMonth(str.substring(0, 3)));

		java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());
		return date;
	}

	int getMonth(String str) {
		int reti = 0;
		if ("Jan".equalsIgnoreCase(str)) {
			reti = 0;
		} else if ("Feb".equalsIgnoreCase(str)) {
			reti = 1;
		} else if ("Mar".equalsIgnoreCase(str)) {
			reti = 2;
		} else if ("Apr".equalsIgnoreCase(str)) {
			reti = 3;
		} else if ("May".equalsIgnoreCase(str)) {
			reti = 4;
		} else if ("Jun".equalsIgnoreCase(str)) {
			reti = 5;
		} else if ("Jul".equalsIgnoreCase(str)) {
			reti = 6;
		} else if ("Aug".equalsIgnoreCase(str)) {
			reti = 7;
		} else if ("Sep".equalsIgnoreCase(str)) {
			reti = 8;
		} else if ("Oct".equalsIgnoreCase(str)) {
			reti = 9;
		} else if ("Nov".equalsIgnoreCase(str)) {
			reti = 10;
		} else if ("Dec".equalsIgnoreCase(str)) {
			reti = 11;
		}
		return reti;
	}
	
}
