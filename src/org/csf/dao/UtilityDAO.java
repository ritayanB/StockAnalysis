package org.csf.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.csf.pojo.CompanyForUpdate;
import org.csf.pojo.ErrorLog;

public class UtilityDAO extends AppConnection {

	/**
	 * @author Ritayan
	 * @return
	 * @throws SQLException
	 */

	public HashMap<Long, String> getBSECompanySecurityCode(boolean initialPull, boolean onlyPriority,
			boolean ratioUpdateProcess) throws SQLException {
		StringBuffer strBuff = new StringBuffer();
		HashMap<Long, String> hm = new HashMap<Long, String>();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			strBuff.append("Select \"Key\", \"SecurityCode\" from \"Company\" ");
			if (initialPull && onlyPriority) {
				strBuff.append(" where \"PriceLastUpdatedOn\" is null and \"Priority\" = 'Yes'");
			} else if (initialPull) {
				strBuff.append(" where \"PriceLastUpdatedOn\" is null");
			} else if (onlyPriority) {
				strBuff.append(" where \"Priority\" = 'Yes'");
			}

			if (ratioUpdateProcess) {
				strBuff.append(" and not exists (select 1 from \"Ratios\" where \"CompanyKey\" = \"Company\".\"Key\")");
			}

			con = AppConnection.getPostgresConnection();
			ps = con.prepareStatement(strBuff.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				hm.put(rs.getLong("Key"), rs.getString("SecurityCode"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, ps, rs);
		}
		return hm;
	}

	/**
	 * @author Ritayan
	 * @throws SQLException
	 */

	public void updateNonPriorityStocks() throws SQLException {
		StringBuffer strBuff = new StringBuffer();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = AppConnection.getPostgresConnection();

			strBuff.append("update \"Company\" set \"Priority\" = 'Yes'");

			ps = con.prepareStatement(strBuff.toString());
			ps.executeUpdate();

			strBuff = new StringBuffer();
			strBuff.append(" update \"Company\" set \"Priority\" = 'No' ");
			strBuff.append(" where \"Key\" in ( ");
			strBuff.append(" select tabb.\"Key\" ");
			strBuff.append(" from \"Company\", ");
			strBuff.append(" (select max(\"TradeDate\") \"SomeDate\", \"CompanyKey\" \"Key\" from \"StockPrice\" ");
			strBuff.append(" group by \"CompanyKey\" ");
			strBuff.append(" having max(\"TradeDate\") < current_date ");
			strBuff.append(" except ");
			strBuff.append(
					" select \"PriceLastUpdatedOn\",\"Key\" from \"Company\" where \"PriceLastUpdatedOn\" < current_date)tabb ");
			strBuff.append(" where \"SomeDate\" < (current_date-125) ");
			strBuff.append(" and \"Company\".\"Key\" = tabb.\"Key\") ");

			ps = con.prepareStatement(strBuff.toString());
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, ps);
		}
	}

	/**
	 * @author Ritayan
	 * @return
	 * @throws SQLException
	 */
	public CompanyForUpdate[] getBSECompanySecurityCodeForUpdate() throws SQLException {
		StringBuffer strBuff = new StringBuffer();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		CompanyForUpdate obj = null;
		CompanyForUpdate[] retObj = null;
		ArrayList<CompanyForUpdate> arr = new ArrayList<CompanyForUpdate>();

		try {

			strBuff.append(" select 	\"SecurityCode\", ");
			strBuff.append(" to_char(\"TradeDate\" + 1, 'Mon DD, YYYY') \"StartDate\",  ");
			strBuff.append(" to_char(current_date, 'Mon DD, YYYY') \"EndDate\", ");
			strBuff.append(" \"Company\".\"Key\" ");
			strBuff.append(" from \"Company\", ( ");
			strBuff.append(" select max(\"TradeDate\") \"TradeDate\", \"CompanyKey\" ");
			strBuff.append(" from \"StockPrice\", \"Company\" ");
			strBuff.append(" where \"Company\".\"Key\" = \"CompanyKey\" ");
			strBuff.append(" and \"Priority\" = 'Yes' ");
			strBuff.append(" and \"PriceLastUpdatedOn\" < current_date ");
			strBuff.append(" group by \"CompanyKey\", \"SecurityCode\" ");
			strBuff.append(" having max(\"TradeDate\") < current_date) tabb ");
			// strBuff.append(" having max(\"TradeDate\") < current_date) tabb
			// ");
			strBuff.append(" where tabb.\"CompanyKey\" = \"Company\".\"Key\" ");
			strBuff.append(" and \"Priority\" = 'Yes'	");

			con = AppConnection.getPostgresConnection();
			ps = con.prepareStatement(strBuff.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				obj = new CompanyForUpdate();
				obj.setSecurityCode(rs.getString("SecurityCode"));
				obj.setStartDate(rs.getString("StartDate"));
				obj.setEndDate(rs.getString("EndDate"));
				obj.setKey(rs.getLong("Key"));
				arr.add(obj);
			}

			retObj = new CompanyForUpdate[arr.size()];
			System.arraycopy(arr.toArray(), 0, retObj, 0, retObj.length);

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, ps, rs);
		}
		return retObj;
	}

	/**
	 * @author Ritayan
	 * @param key
	 * @param message
	 * @throws SQLException
	 */

	public void logImportError(Long key, String message) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer buff = new StringBuffer("Update \"Company\" set \"ImportError\" = ? where \"Key\" = ?");
		Connection con = null;
		PreparedStatement pt = null;
		try {
			con = AppConnection.getPostgresConnection();
			pt = con.prepareStatement(buff.toString());
			pt.setString(1, message);
			pt.setLong(2, key);
			pt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, pt);
		}

	}

	public void logImportError(String className, String tableName, String errorDesc, String success,
			String dataIdentifier) throws SQLException {

		ErrorLog log[] = new ErrorLog[1];
		log[0] = new ErrorLog();
		log[0].setClassName(className);
		log[0].setErrorDesc(errorDesc);
		log[0].setTableName(tableName);
		log[0].setSuccess(success);
		Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		log[0].setUpdatedOn(date);
		log[0].setDataIdentifier(dataIdentifier);
		new TableDAO().populateTableFromBO(log);
	}

	/**
	 * @author Ritayan
	 * @param key
	 * @throws SQLException
	 */

	public void updateAuditParamCompany(String operation, long key) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer buff = null;
		buff = new StringBuffer("Update \"Company\" set \"");
		buff.append(operation);
		buff.append("LastUpdatedOn\" = ?, \"ImportError\" = null where \"Key\" = ?");

		Connection con = null;
		PreparedStatement pt = null;
		try {
			con = AppConnection.getPostgresConnection();
			pt = con.prepareStatement(buff.toString());
			pt.setDate(1, new Date(System.currentTimeMillis()));
			pt.setLong(2, key);
			pt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, pt);
		}
	}

	public void updateMovingAverages(long key, int avgDuration) throws SQLException {
		// TODO Auto-generated method stub
		StringBuffer buff = new StringBuffer("update \"StockPrice\"");
		buff.append(" set \"MA");
		buff.append(avgDuration);
		buff.append("Days\" = tabb.\"MA\" ");
		buff.append(" from ");
		buff.append(" (select round(avg(st.\"ClosePrice\"),2) \"MA\" , ft.\"TradeDate\", ft.\"Key\" ");
		buff.append(" from ");
		buff.append(" (select a.*, rank() OVER (order by \"TradeDate\" desc) \"Rank\" ");
		buff.append(" from \"StockPrice\" a ");
		buff.append(" where \"CompanyKey\" = ? ");
		buff.append(" order by \"TradeDate\" desc) ft, ");
		buff.append(" (select a.*, rank() OVER (order by \"TradeDate\" desc) \"Rank\" ");
		buff.append(" from \"StockPrice\" a ");
		buff.append(" where \"CompanyKey\" = ? ");
		buff.append(" order by \"TradeDate\" desc) st ");
		buff.append(" where st.\"Rank\" > ft.\"Rank\" and st.\"Rank\" <= (ft.\"Rank\" + ?) ");
		buff.append(" group by ft.\"TradeDate\", ft.\"Key\" ");
		buff.append(" order by ft.\"TradeDate\" desc) tabb ");
		buff.append(" where \"StockPrice\".\"Key\" = tabb.\"Key\" ");

		Connection con = null;
		PreparedStatement pt = null;
		try {
			con = AppConnection.getPostgresConnection();
			pt = con.prepareStatement(buff.toString());
			pt.setLong(1, key);
			pt.setLong(2, key);
			pt.setInt(3, avgDuration);
			pt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, pt);
		}
	}

	public Long[] getAllCompanyKey() throws SQLException {
		StringBuffer buff = new StringBuffer("select \"Key\" from \"Company\" where \"Priority\" = 'Yes'");
		Connection con = null;
		PreparedStatement pt = null;
		ResultSet rs = null;
		ArrayList<Long> rr = new ArrayList<>();
		try {
			con = AppConnection.getPostgresConnection();
			pt = con.prepareStatement(buff.toString());
			rs = pt.executeQuery();
			while (rs.next()) {
				rr.add(rs.getLong("Key"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, pt);
		}
		Long lar[] = new Long[rr.size()];
		System.arraycopy(rr.toArray(), 0, lar, 0, rr.size());
		return lar;
	}

	public void moveOldData() throws SQLException {
		StringBuffer buff = new StringBuffer(" insert into \"StockPriceArchive\" ");
		buff.append(" ( ");
		buff.append(" select	\"Key\",\"CompanyKey\",\"TradeDate\", \"OpenPrice\",\"HighPrice\",\"LowPrice\", ");
		buff.append(" \"ClosePrice\",\"Volume\",\"MA10Days\",\"MA5Days\",\"MA30Days\",\"MA15Days\", ");
		buff.append(" \"MA60Days\",\"MA120Days\",\"Trend5Days\",\"Trend10Days\",\"Trend15Days\", ");
		buff.append(" \"Trend30Days\",\"Trend60Days\",\"Trend120Days\",\"Trend2Days\"  ");
		buff.append(" from	 ");
		buff.append(" (select	\"StockPrice\".*, ");
		buff.append(" rank()  over(partition by \"CompanyKey\" order by \"TradeDate\" desc) \"DateRank\" ");
		buff.append(" from \"StockPrice\", (select ? \"index\") \"intab\" ");
		buff.append(" where \"CompanyKey\" between (intab.\"index\") and (\"intab\".\"index\" + 10) ");
		buff.append(" ) tabb ");
		buff.append(" where \"DateRank\" > 125 ");
		buff.append(" order by \"CompanyKey\", \"TradeDate\" ");
		buff.append(" ) ");

		StringBuffer buf = new StringBuffer(" DELETE ");
		buf.append("	FROM	\"StockPrice\" ");
		buf.append("	WHERE 	\"Key\" in  ");
		buf.append("	( ");
		buf.append("	select	\"Key\" ");
		buf.append("	from	( ");
		buf.append("	select		\"StockPrice\".*, ");
		buf.append("	rank()  over(partition by \"CompanyKey\" order by \"TradeDate\" desc) \"DateRank\" ");
		buf.append("	from \"StockPrice\", (select ? \"index\") \"intab\" ");
		buf.append("	where \"CompanyKey\" between (intab.\"index\") and (\"intab\".\"index\" + 10) ");
		buf.append("	) tabb ");
		buf.append("	where \"DateRank\" > 125 ");
		buf.append("	order by \"CompanyKey\", \"TradeDate\" ");
		buf.append("	) ");

		Connection con = null;
		PreparedStatement pt1 = null;
		PreparedStatement pt2 = null;
		long str = System.currentTimeMillis();
		try {
			con = AppConnection.getPostgresConnection();
			pt1 = con.prepareStatement(buff.toString());
			pt2 = con.prepareStatement(buf.toString());
			con.setAutoCommit(false);
			for (int itr = 0; itr < 426; itr++) {
				pt1.setInt(1, itr * 10);
				pt2.setInt(1, itr * 10);
				System.out.println("Number of records updated : " + pt1.executeUpdate());
				System.out.println("Number of records updated : " + pt2.executeUpdate());
				System.out.println(
						"Time elapsed for iteration number " + itr + " is " + (System.currentTimeMillis() - str)/1000 + " seconds");
				con.commit();
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			close(con, pt2);
			close(con, pt1);
		}
	}

	public static void main(String rt[]) {
		UtilityDAO dd = new UtilityDAO();
		try {
			dd.moveOldData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
