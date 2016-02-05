package org.csf.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppConnection {

	public static final String CONNECTION_DRIVER_POSRTGRESQL = "org.postgresql.Driver";
	static final String CONNECTION_URL_POSRTGRESQL = "jdbc:postgresql://127.0.0.1:5432/postgres";
	static final String CONNECTION_USERNAME_POSRTGRESQL = "postgres";
	static final String CONNECTION_PASSWORD_POSRTGRESQL = "1234";

	private static long connectionCounter = 0;

	/**
	 * 
	 * This is explicitly for the postGres connection
	 * 
	 * @return {@link Connection}
	 */
	public static Connection getPostgresConnection() {
		return getConnection(CONNECTION_DRIVER_POSRTGRESQL, CONNECTION_URL_POSRTGRESQL, CONNECTION_USERNAME_POSRTGRESQL,
				CONNECTION_PASSWORD_POSRTGRESQL);
	}

	/**
	 * 
	 * @param connectionDriver
	 * @param connectionURL
	 * @param connectionUserName
	 * @param connectionPassword
	 * @return {@link Connection}
	 */

	public static Connection getConnection(String connectionDriver, String connectionURL, String connectionUserName,
			String connectionPassword) {
		Connection con = null;
		try {
			Class.forName(connectionDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(connectionURL, connectionUserName, connectionPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("One connection opened. Open connection : " + ++connectionCounter);
		return con;
	}

	public void close(Connection con) {
		try {
			con.close();
			System.out.println("One connection closed. Open connection : " + --connectionCounter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(Connection con, Statement st) {
		try {
			close(con);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(Connection con, Statement st, ResultSet rs) {
		try {
			close(con, st);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
