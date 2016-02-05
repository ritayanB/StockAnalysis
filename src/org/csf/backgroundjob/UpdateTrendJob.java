package org.csf.backgroundjob;

import java.sql.SQLException;

import org.csf.dao.DerivedDataDAO;

public class UpdateTrendJob extends Thread {

	int series[];
	int daysToUpdate;
	
	UpdateTrendJob(int series[], int daysToUpdate){
		this.series = series;
		this.daysToUpdate = daysToUpdate;
	}
	
	@Override
	public void run() {
		try {
			new DerivedDataDAO().updateTrends(series, daysToUpdate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
