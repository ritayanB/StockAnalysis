package org.csf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DerivedDataDAO extends AppConnection {

	public void updateMovingAverages(int maNum[], int daysToUpdate) throws SQLException {
		long start = System.currentTimeMillis();
		int batchCommitSize = 100;
		Connection con = null;
		PreparedStatement ps = null;
		UtilityDAO dao = new UtilityDAO();

		con = getPostgresConnection();
		Long companyKey[] = null;
		try {
			companyKey = dao.getAllCompanyKey();
			// con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("Stocks due for update : " + companyKey.length);
		for (int ou = 0; ou < maNum.length; ou++) {
			StringBuffer buff = new StringBuffer(" UPDATE \"StockPrice\" ");
			buff.append(" SET \"MA");
			buff.append(maNum[ou]);
			buff.append("Days\" = tabb.\"MA\" ");
			buff.append(" FROM ");
			buff.append(" (SELECT round(avg(st.\"ClosePrice\"),2) \"MA\", ");
			buff.append(" ft.\"TradeDate\", ");
			buff.append(" ft.\"Key\", ");
			buff.append(" ft.\"Rank\" ");
			buff.append(" FROM ");
			buff.append(" (SELECT a.*, ");
			buff.append(" rank() OVER ( ");
			buff.append(" ORDER BY \"TradeDate\" DESC) \"Rank\" ");
			buff.append(" FROM \"StockPrice\" a ");
			buff.append(" WHERE \"CompanyKey\" = ? ");
			buff.append("       ORDER BY \"TradeDate\" DESC) ft, ");
			buff.append(" 		     (SELECT a.*, ");
			buff.append(" rank() OVER ( ");
			buff.append(" ORDER BY \"TradeDate\" DESC) \"Rank\" ");
			buff.append(" FROM \"StockPrice\" a ");
			buff.append(" WHERE \"CompanyKey\" = ? ");
			buff.append(" ORDER BY \"TradeDate\" DESC) st ");
			buff.append(" WHERE st.\"Rank\" > ft.\"Rank\" ");
			buff.append(" AND st.\"Rank\" <= (ft.\"Rank\" + ?) ");
			buff.append(" GROUP BY ft.\"TradeDate\", ");
			buff.append(" ft.\"Key\", ");
			buff.append(" ft.\"Rank\" ");
			buff.append(" ORDER BY ft.\"TradeDate\" DESC) tabb ");
			buff.append(" WHERE \"StockPrice\".\"Key\" = tabb.\"Key\" ");
			buff.append(" AND tabb.\"Rank\" <= ? ");
			
			ps = con.prepareStatement(buff.toString());
			
			for (int i = 0; i < companyKey.length; i++) {
				long startTime = System.currentTimeMillis();
				try {
					ps.setLong(1, companyKey[i]);
					ps.setLong(2, companyKey[i]);
					ps.setLong(3, maNum[ou]);
					ps.setLong(4, daysToUpdate);
					ps.addBatch();
					System.out.println("Updated for stock number : " + i + " : MA : " + maNum[ou]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (i % batchCommitSize == 0) {
					System.out.println("Number of records updated : " + ps.executeBatch().length + " in " + ((System.currentTimeMillis()- startTime)/1000) + " seconds"); 
					ps.clearBatch();
				}
			}
			ps.executeBatch();
		}
		close(con, ps);
		System.out.println("Moving Avegare update completed in : " + (System.currentTimeMillis() - start)/60000 + " minutes");
	}
	
	public void updateTrends(int maNum[], int daysToUpdate) throws SQLException {
		long start = System.currentTimeMillis();
		int batchCommitSize = 10;
		Connection con = null;
		PreparedStatement ps = null;
		UtilityDAO dao = new UtilityDAO();

		con = getPostgresConnection();
		Long companyKey[] = null;
		try {
			companyKey = dao.getAllCompanyKey();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("Stocks due for update : " + companyKey.length);
		for (int ou = 0; ou < maNum.length; ou++) {
			StringBuffer buff = new StringBuffer(" UPDATE \"StockPrice\" ");
			buff.append(" SET  \"Trend");
			buff.append(maNum[ou]);
			buff.append("Days\" = \"TrendValue\" ");
			buff.append(" FROM ");
			buff.append(" (SELECT round(((100*(ft.\"ClosePrice\" - st.\"ClosePrice\"))/st.\"ClosePrice\"),2) \"TrendValue\", ");
			buff.append(" ft.\"TradeDate\", ");
			buff.append(" ft.\"Key\", ");
			buff.append(" rank() OVER ( ");
			buff.append(" ORDER BY ft.\"Rank\")  \"Rank\" ");
			buff.append(" FROM ");
			buff.append(" (SELECT a.\"TradeDate\", ");
			buff.append(" a.\"ClosePrice\", ");
			buff.append(" a.\"Key\", ");
			buff.append(" ((RANK() OVER (PARTITION BY \"CompanyKey\" ");
			buff.append(" ORDER BY \"TradeDate\" DESC)) + ?) \"Rank\" ");
			buff.append(" FROM \"StockPrice\" a ");
			buff.append(" WHERE \"CompanyKey\" = ? ");
			buff.append(" ORDER BY \"TradeDate\" DESC) ft, ");
			buff.append(" (SELECT a.\"TradeDate\", ");
			buff.append(" a.\"ClosePrice\", ");
			buff.append(" a.\"Key\", ");
			buff.append(" RANK() OVER (PARTITION BY \"CompanyKey\" ");
			buff.append(" ORDER BY \"TradeDate\" DESC) \"Rank\" ");
			buff.append(" FROM \"StockPrice\" a ");
			buff.append(" WHERE \"CompanyKey\" = ? ");
			buff.append(" ORDER BY \"TradeDate\" DESC) st ");
			buff.append(" WHERE st.\"Rank\" = ft.\"Rank\" ");
			buff.append(" ORDER BY ft.\"TradeDate\" DESC) tabb ");
			buff.append(" WHERE \"StockPrice\".\"Key\" = tabb.\"Key\" ");
			//buff.append(" AND   \"Rank\" <= ? ");
			
			
			
			ps = con.prepareStatement(buff.toString());
			
			for (int i = 0; i < companyKey.length; i++) {
				long startTime = System.currentTimeMillis();
				try {
					ps.setInt(1, maNum[ou]);
					ps.setLong(2, companyKey[i]);
					ps.setLong(3, companyKey[i]);
					//ps.setLong(4, daysToUpdate);
					ps.addBatch();
					System.out.println("Updated for stock number : " + i + " : Trend : " + maNum[ou]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (i % batchCommitSize == 0) {
					System.out.println("Number of records updated : " + ps.executeBatch().length + " in " + ((System.currentTimeMillis()- startTime)/1000) + " seconds"); 
					ps.clearBatch();
				}
			}
			ps.executeBatch();
		}
		close(con, ps);
		System.out.println("Trend update completed in : " + (System.currentTimeMillis() - start)/60000 + " minutes");
	}

}
