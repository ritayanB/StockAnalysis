package org.csf.backgroundjob;

import java.sql.SQLException;

import org.csf.service.StockInfoService;

public class UpdateStockPriceJob extends Thread {

	@Override
	public void run() {
		
		try {
			new StockInfoService().updateStockPrice();
			//new StockInfoService().updateMovingAverages();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
