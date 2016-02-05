package org.csf.backgroundjob;

import java.sql.SQLException;

import org.csf.service.StockInfoService;
import org.csf.statics.StaticValues;

public class MasterJobScheduler {

	public static void main(String op[]) {
		
		int daysToUpdate = StaticValues.NUMBER_OF_RECORDS_FOR_TREND_UPDATE;
		
		try {
			new StockInfoService().updateStockPrice();
			new StockInfoService().updateStockPrice(); //**This is retry event to overcome the failure cases occurred out of the first trial**//*
		} catch (SQLException e) {
			e.printStackTrace();
		}

		new UpdateMovingAverageJob(new int[] { 1 ,5, 10, 15 }, daysToUpdate).start();
		new UpdateMovingAverageJob(new int[] { 30, 60, 120, 240 }, daysToUpdate).start();

		new UpdateTrendJob(new int[] { 1, 2, 5, 10, 15 }, daysToUpdate).start();
		new UpdateTrendJob(new int[] { 30, 60, 120, 240 }, daysToUpdate).start();
	}
}
