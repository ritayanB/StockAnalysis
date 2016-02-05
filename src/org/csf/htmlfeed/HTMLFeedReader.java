package org.csf.htmlfeed;

import java.util.ArrayList;

import org.csf.pojo.Company;
import org.csf.pojo.Country;

public class HTMLFeedReader {

	/**
	 * @author Ritayan 
	 * @return Country[]
	 * @throws Exception
	 */
	
	public static void main(String p[]){
		try {
			getCountryData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Country[] getCountryData() throws Exception {

		Country[] countries;
		int counter = 0;

		ArrayList<String> rr = new HTMLReader()
				.getTableRowsFromURL(
						"http://www.indian-share-tips.com/2011/11/nse-stock-codes-stock-code-list-india.html",
						new ElementProperties(null,
								ElementProperties.TYPE_TABLE, "tableizer-table"),
						new QueryColumn("*", 2));
		/*ArrayList<String> rr2 = new HTMLReader()
				.getTableRowsFromURL(
						"http://www.nationsonline.org/oneworld/countries_of_the_world.htm",
						new ElementProperties(ElementProperties.STYLE_CLASS,
								ElementProperties.TYPE_TR, "tdx"),
						new QueryColumn("1", 4));*/
		//rr.addAll(rr2);

		countries = new Country[rr.size()];

		for (String rec : rr) {
			countries[counter] = new Country();
			countries[counter++].setName(rec);
			System.out.println(rec);
			
		}

		return countries;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Company[] getCompanyData() throws Exception {

		Company[] companies;
		int arrayCounter = 0;
		// int counter = 0;
		
		/** NSE data: start **/
/*		ArrayList<String> rr = new HTMLReader()
				.getTableRowsFromURL(
						"http://www.nseindia.com/content/corporate/eq_research_reports_listed.htm",
						new ElementProperties(ElementProperties.STYLE_CLASS,
								ElementProperties.TYPE_TABLE, "tabular_data"),
						new QueryColumn("2,3", 8));

		ArrayList<String> rr2 = new HTMLReader().getTableRowsFromURL(
				"http://www.nseindia.com/content/corporate/eq_rrl_m2z.htm",
				new ElementProperties(ElementProperties.STYLE_CLASS,
						ElementProperties.TYPE_TD, "t0"), new QueryColumn(
						"2,3", 3));

		rr.addAll(rr2);

		companies = new Company[rr.size() / 2];

		for (int counter = 0; counter < companies.length; counter++) {
			companies[counter] = new Company();
			companies[counter].setExchangeKey(2);// for NSE
			companies[counter].setSymbolAtExchange(rr.get(arrayCounter++));
			companies[counter].setName(rr.get(arrayCounter++));
		}*/
		/** NSE data: end **/
		
		/** BSE data: start **/
		
		ArrayList<String> rr = new HTMLReader()
		.getTableRowsFromURL(
				"http://localhost:8080/sheet0012.htm",
				new ElementProperties(ElementProperties.STYLE_CLASS,
						ElementProperties.TYPE_TD, "selectit"),
				new QueryColumn("*", 9));
		
		companies = new Company[rr.size()/ 9];

		for (int counter = 0; counter < companies.length; counter++) {
			companies[counter] = new Company();
			companies[counter].setExchangeKey(3);// for BSE
			
			companies[counter].setSecurityCode(Integer.valueOf(rr.get(arrayCounter++)));
			
			System.out.println("SecurityID : " + rr.get(arrayCounter));
			
			companies[counter].setSecurityID(rr.get(arrayCounter++));
			companies[counter].setName(rr.get(arrayCounter++));
			
			arrayCounter++;arrayCounter++;
			
			companies[counter].setFaceValue((Float.valueOf(rr.get(arrayCounter++))));
			companies[counter].setISIN(rr.get(arrayCounter++));
			companies[counter].setIndustry(rr.get(arrayCounter++));
			companies[counter].setInstrument(rr.get(arrayCounter++));
			
		}
		
		/** BSE data: end **/
		
		return companies;
	}
}
