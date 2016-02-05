package org.csf.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.csf.dao.TableDAO;
import org.csf.dao.UtilityDAO;
import org.csf.htmlfeed.GoogleFinanceReader;
import org.csf.htmlfeed.WSJReader;
import org.csf.pojo.CompanyForUpdate;
import org.csf.pojo.Ratios;
import org.csf.pojo.StockPrice;
import org.csf.statics.CustomMessage;

public class StockInfoService {

	/**
	 * @author Ritayan
	 * @return
	 * @throws SQLException
	 */
	public boolean insertStockPrice() throws SQLException {

		HashMap<Long, String> companySecurity = new UtilityDAO().getBSECompanySecurityCode(true, false, false);
		StockPrice[] rprice = null;

		TableDAO tdao = new TableDAO();
		UtilityDAO dao = new UtilityDAO();

		IStockPriceService service = new GoogleFinanceReader();

		Date endDate = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 5);
		java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

		SimpleDateFormat ft = new SimpleDateFormat("MMM dd',' yyyy");

		for (Long key : companySecurity.keySet()) {
			try {
				rprice = service.getHistoricalValueBSE(String.valueOf(companySecurity.get(key)), ft.format(startDate),
						ft.format(endDate));
				for (StockPrice temp : rprice) {
					temp.setCompanyKey(key);
				}
				if (rprice.length == 0) {
					dao.updateAuditParamCompany(CustomMessage.OPERATION_PRICE, key);
					dao.logImportError(key, CustomMessage.EXCEPTION_NO_HISTORIAL_DATA);
				} else {
					tdao.populateTableFromBO(rprice);
					dao.updateAuditParamCompany(CustomMessage.OPERATION_PRICE, key);
				}
				System.out.println("Sleeping...");
				Thread.sleep((long) (Math.random() * 6000));
			} catch (Exception e) {
				dao.logImportError(key, e.getMessage());
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * @author Ritayan
	 * @return
	 * @throws SQLException
	 */
	public boolean updateStockPrice() throws SQLException {

		long startTime = System.currentTimeMillis();

		int numberLeft = 0;

		UtilityDAO dao = new UtilityDAO();

		dao.updateNonPriorityStocks();

		CompanyForUpdate[] companySecurity = dao.getBSECompanySecurityCodeForUpdate();

		System.out.println("Number of stocks due for update : " + companySecurity.length);

		numberLeft = companySecurity.length;

		StockPrice[] rprice = null;

		TableDAO tdao = new TableDAO();

		IStockPriceService service = new GoogleFinanceReader();

		for (CompanyForUpdate tempU : companySecurity) {
			try {
				rprice = service.getHistoricalValueBSE(String.valueOf(tempU.getSecurityCode()), tempU.getStartDate(),
						tempU.getEndDate());
				for (StockPrice temp : rprice) {
					temp.setCompanyKey(tempU.getKey());
				}
				if (rprice.length > 0) {
					tdao.populateTableFromBO(rprice);
				}
				dao.updateAuditParamCompany(CustomMessage.OPERATION_PRICE, tempU.getKey());
				// System.out.println("Sleeping...");
				System.out.println("Number left : " + --numberLeft);
				Thread.sleep((long) (Math.random() * 6000));
			} catch (Exception e) {
				dao.logImportError(tempU.getKey(), e.getMessage());
				e.printStackTrace();
			}
		}

		System.out.println("Update of stocks ends in : "
				+ ((System.currentTimeMillis() - startTime) / 60000 + " minutes for number of stocks : ")
				+ companySecurity.length);

		return true;
	}

	public void updateStockRatios() throws SQLException {

		Ratios ratio[] = new Ratios[1];
		IStockFundamental fundam = new WSJReader();
		UtilityDAO dao = new UtilityDAO();
		TableDAO tdao = new TableDAO();
		HashMap<Long, String> hm = dao.getBSECompanySecurityCode(false, true, true);

		for (Long key : hm.keySet()) {
			try {
				ratio = fundam.getFundamantalRatios(String.valueOf(hm.get(key)));
				ratio[0].setCompanyKey(key);
				tdao.cleanUPData("Ratios", "CompanyKey", Long.class, String.valueOf(key));
				tdao.populateTableFromBO(ratio);
				dao.updateAuditParamCompany(CustomMessage.OPERATION_RATIO, key);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				dao.logImportError(fundam.getClass().getName(), "Ratios", e.getMessage(), "No",
						String.valueOf(hm.get(key)));
				e.printStackTrace();
			}

		}

	}

	public void updateMovingAverages() throws SQLException {

		long startTime = System.currentTimeMillis();
		UtilityDAO dao = new UtilityDAO();
		HashMap<Long, String> hm = dao.getBSECompanySecurityCode(false, true, false);
		int all = hm.size();
		int cnt = 0;
		for (Long key : hm.keySet()) {
			try {
				dao.updateMovingAverages(key, 15);
				dao.updateMovingAverages(key, 60);
				dao.updateMovingAverages(key, 120);
				System.out.println("Number of stock pending for update : " + (all - cnt++));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("IStockInfoService:updateMovingAverages()  completed in "
				+ ((System.currentTimeMillis() - startTime) / 1000) + " seconds");

	}

	public static void main(String op[]) {

		try {
			new StockInfoService().insertStockPrice();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
