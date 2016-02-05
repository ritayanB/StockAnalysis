package org.csf.dao;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.csf.exception.NoDataFoundException;
import org.csf.pojo.TableObject;

/**
 * 
 * @author 165505
 *
 */
public class InsertPreparedStatement implements PreparedStatement {

	StringBuffer query;

	public StringBuffer getQuery() {
		return query;
	}

	Method methods[];

	public Method[] getMethods() {
		return methods;
	}

	public void setQuery(StringBuffer query) {
		this.query = query;

	}

	public String[] getMethodRetType() {
		return methodRetType;
	}

	public void setMethodRetType(String[] methodRetType) {
		this.methodRetType = methodRetType;
	}

	PreparedStatement preparedStatement;

	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	public void setPreparedStatement(InsertPreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}

	String methodRetType[];

	/**
	 * 
	 * @param query
	 * @param methodRetType
	 */
	public InsertPreparedStatement(StringBuffer query, String methodRetType[]) {
		this.query = query;
		this.methodRetType = methodRetType;
	}

	/**
	 * 
	 * @param Object
	 *            that needs to be inserted
	 * @throws SQLException
	 * @throws NoDataFoundException
	 */
	public InsertPreparedStatement(TableObject obj[], Connection con, String schemaName) throws SQLException {

		this.query = new StringBuffer();
		StringBuffer placeholders = new StringBuffer();
		int loopCount = 0;

		
		String className = obj[0].getClass().getName();

		String tableName = className.split("\\.")[className.split("\\.").length - 1];

		if ("".equalsIgnoreCase(schemaName)) {
			query.append("INSERT INTO \"" + tableName + "\" ( ");
		} else {
			query.append("INSERT INTO \"" + schemaName + "\".\"" + tableName + "\" ( ");
		}

		methods = obj[0].getClass().getDeclaredMethods();
		methodRetType = new String[methods.length];

		for (Method mm : methods) {
			if (mm.getName().contains("get")) {
				query.append("\"" + mm.getName().replace("get", "") + "\"" + ", ");
				placeholders.append("?, ");
				methodRetType[loopCount++] = mm.getReturnType().getName();
			}
		}
		query.deleteCharAt(query.length() - 1).deleteCharAt(query.length() - 1);
		placeholders.deleteCharAt(placeholders.length() - 1).deleteCharAt(placeholders.length() - 1).append(" )");

		query.append(" ) VALUES ( ");
		query.append(placeholders);

		preparedStatement = con.prepareStatement(query.toString());

	}

	void executeInsert(TableObject ob[]) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {

		StringBuffer methodInvokedPs = new StringBuffer();
		Object value;
		Method psMethod;
		Class<? extends InsertPreparedStatement> psClass = this.getClass();

		int executeCounter = 0;

		int iPlaceHolderCount;
		String methodSuffix;

		for (Object obb : ob) {
			iPlaceHolderCount = 1;
			for (Method mm : this.getMethods()) {
				if (mm.getName().contains("get")) {
					value = mm.invoke(obb);

					methodSuffix = mm.getReturnType().getName()
							.split("\\.")[mm.getReturnType().getName().split("\\.").length - 1];
					if (Character.isLowerCase(methodSuffix.charAt(0))) {
						methodSuffix = String.valueOf(Character.toUpperCase(methodSuffix.charAt(0)))
								+ methodSuffix.substring(1);
					}

					methodInvokedPs.append("set" + methodSuffix);

					psMethod = psClass.getDeclaredMethod(methodInvokedPs.toString().trim(), int.class,
							mm.getReturnType());

					psMethod.invoke(this, iPlaceHolderCount, value);

					iPlaceHolderCount++;
					methodInvokedPs.delete(0, methodInvokedPs.length());
				}

			}

			if (executeCounter == 500) {
				this.executeBatch();
				executeCounter = 0;
			} else {
				this.addBatch();
				executeCounter++;
			}

		}
		this.executeBatch();
	}

	@Override
	public void addBatch(String sql) throws SQLException {

		preparedStatement.addBatch(sql);
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() throws SQLException {
		preparedStatement.cancel();
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.clearBatch();

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.close();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.closeOnCompletion();

	}

	@Override
	public boolean execute(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.execute(sql);
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.execute(sql, autoGeneratedKeys);
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.execute(sql, columnIndexes);
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.execute(sql, columnNames);
	}

	@Override
	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.executeBatch();
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.executeQuery(sql);
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.executeUpdate(sql);
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.executeUpdate(sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.executeUpdate(sql, columnIndexes);
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return executeUpdate(sql, columnNames);
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getConnection();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getFetchDirection();
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getFetchSize();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getGeneratedKeys();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getMaxFieldSize();
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getMaxRows();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getMoreResults();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getMoreResults(current);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getQueryTimeout();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getResultSet();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getResultSetConcurrency();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getResultSetHoldability();
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getResultSetType();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getUpdateCount();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return preparedStatement.getWarnings();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBatch() throws SQLException {
		preparedStatement.addBatch();
		// TODO Auto-generated method stub

	}

	@Override
	public void clearParameters() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int executeUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setArray(int arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBlob(int arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBoolean(int arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setByte(int arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBytes(int arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClob(int arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDate(int arg0, Date arg12) throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.setDate(arg0, arg12);
	}

	@Override
	public void setDate(int arg0, Date arg1, Calendar arg2) throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.setDate(arg0, arg1, arg2);

	}

	@Override
	public void setDouble(int arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFloat(int arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.setFloat(arg0, arg1);
	}

	@Override
	public void setInt(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.setInt(arg0, arg1);

	}

	@Override
	public void setLong(int arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub
		preparedStatement.setLong(arg0, arg1);
	}

	@Override
	public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNull(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNull(int arg0, int arg1, String arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setObject(int arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setObject(int arg0, Object arg1, int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setObject(int arg0, Object arg1, int arg2, int arg3) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRef(int arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setShort(int arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setString(int arg0, String arg1) throws SQLException {

		preparedStatement.setString(arg0, arg1);
		// TODO Auto-generated method stub

	}

	@Override
	public void setTime(int arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setURL(int arg0, URL arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUnicodeStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

}
