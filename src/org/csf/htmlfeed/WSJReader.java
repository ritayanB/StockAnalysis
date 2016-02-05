package org.csf.htmlfeed;

import java.io.IOException;
import java.sql.SQLException;

import org.csf.pojo.Ratios;
import org.csf.service.IStockFundamental;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WSJReader extends HTMLFeedReader implements IStockFundamental {

	@Override
	public Ratios[] getFundamantalRatios(String secutiryCode) throws IOException, SQLException {
		long startTime = System.currentTimeMillis();
		StringBuffer URL = new StringBuffer();
		Ratios ratio[] = new Ratios[1];
		int iCounter = 0;
		URL.append("http://quotes.wsj.com/IN/XBOM/");
		URL.append(secutiryCode);
		URL.append("/financials");
		try {
			Connection htmlConnect = Jsoup.connect(URL.toString());
			System.out.println("Web query : " + URL);
			htmlConnect.timeout(8000);
			Document doc = htmlConnect.get();
			Elements tableElements1 = doc.select("table").select(".cr_sub_valuation");
			Elements tableElements2 = doc.select("table").select(".cr_sub_profitability");
			Elements tableElements3 = doc.select("table").select(".cr_sub_efficiency");
			Elements tableElements4 = doc.select("table").select(".cr_sub_capital");
			Elements tableElements5 = doc.select("table").select(".cr_sub_liquidity");

			Elements tableHeaderElest = tableElements1.select("td");
			tableHeaderElest.addAll(tableElements2.select("td"));
			tableHeaderElest.addAll(tableElements3.select("td"));
			tableHeaderElest.addAll(tableElements4.select("td"));
			tableHeaderElest.addAll(tableElements5.select("td"));

			Elements tableHeaderElement = tableHeaderElest.select(".data_lbl");

			if (tableHeaderElement.size() == 0) {
				if ("Please wait, the login page is opening...".equals(doc.select("title").text())) {
					throw new IOException("Proxy error");
				}else if("Company Not Found".equals(doc.select(".cr_notfound_header").select("h1").text())){
					throw new IOException("Company Not Found at WSJ site");
				}else if("Data not available.".equals(doc.select(".data_none").get(0).text())){
					throw new IOException("Data not available at WSJ site");
				}
			} else if(tableHeaderElement.size() != 34){
				throw new IOException("Number of data point fetched : " + tableHeaderElement.size() + " instead of 34");
			}

			ratio[0] = new Ratios();

			ratio[0].setPERatioTTM(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setPERatioincludingextraordinaryitems(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setPricetoSalesRatio(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setPricetoBookRatio(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setPricetoCashFlowRatio(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setEnterpriseValuetoEBITDA(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setEnterpriseValuetoSales(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setTotalDebttoEnterpriseValue(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setTotalDebttoEBITDA(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setEPSrecurring(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setEPSbasic(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setEPSdiluted(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setGrossMargin(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setOperatingMargin(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setPretaxMargin(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setNetMargin(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setReturnonAssets(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setReturnonEquity(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setReturnonTotalCapital(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setReturnonInvestedCapital(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setRevenueEmployee(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setIncomePerEmployee(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setReceivablesTurnover(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setTotalAssetTurnover(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setTotalDebttoTotalEquity(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setTotalDebttoTotalCapital(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setTotalDebttoTotalAssets(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setInterestCoverage(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setLongTermDebttoEquity(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setLongTermDebttoTotalCapital(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setLongTermDebttoAssets(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setCurrentRatio(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setQuickRatio(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter++).text(), "").replace("-", "-1").replace(",", "")));

			ratio[0].setCashRatio(Float.parseFloat(tableHeaderElest.get(iCounter).text()
					.replace(tableHeaderElement.get(iCounter).text(), "").replace("-", "-1").replace(",", "")));

			System.out.println("Ratio Data pull done in " + ((System.currentTimeMillis() - startTime) / 1000)
					+ " seconds for security code: " + secutiryCode);
			return ratio;

		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	public static void main(String op[]) {
		try {
			new WSJReader().getFundamantalRatios("538666");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
