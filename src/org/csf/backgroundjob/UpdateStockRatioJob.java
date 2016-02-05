package org.csf.backgroundjob;

import java.sql.SQLException;

import org.csf.service.StockInfoService;

public class UpdateStockRatioJob extends Thread {

	@Override
	public void run() {
		try {
			new StockInfoService().updateStockRatios();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String op[]){
		UpdateStockRatioJob t = new UpdateStockRatioJob();
		t.start();
	}

}
