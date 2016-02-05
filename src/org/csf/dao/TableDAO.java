package org.csf.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.csf.pojo.TableObject;

public class TableDAO extends AppConnection {

	/**
	 * @author Ritayan
	 * @param tableObject
	 * @return
	 * @throws SQLException
	 */
	public boolean populateTableFromBO(TableObject tableObject[]) throws SQLException {

		Connection con = getPostgresConnection();

		InsertPreparedStatement psmt = new InsertPreparedStatement(tableObject, con, "");
		try {
			psmt.executeInsert(tableObject);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			return false;
		} finally {
			close(con, psmt);
		}
		return true;
	}
	
	/**
	 * @author Ritayan
	 * @param tableName
	 * @param columnName
	 * @param columnType
	 * @param columnValue
	 * @return
	 * @throws SQLException
	 */

	public boolean cleanUPData(String tableName, String columnName, Class<?> columnType, String columnValue)
			throws SQLException {
		Connection con = getPostgresConnection();
		PreparedStatement psmt = null;

		StringBuffer query = new StringBuffer();
		query.append("delete from \"");
		query.append(tableName);
		query.append("\" where \"");
		query.append(columnName);
		query.append("\" = ?");

		try {
			psmt = con.prepareStatement(query.toString());

			if (Long.class == columnType) {
				psmt.setLong(1, Long.parseLong(columnValue));
			}

			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		} finally {
			close(con, psmt);
		}
		return true;
	}
	
	/**
	 * @author Ritayan
	 * @param pst
	 * @param cls
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object[] getResultSetToObjectArray(PreparedStatement pst, Class cls) throws SQLException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ResultSet rs = pst.executeQuery();

		Object obj = null;
		HashMap<String, String> metadata = new HashMap<String, String>();

		for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
			metadata.put(rs.getMetaData().getColumnLabel(i + 1), rs.getMetaData().getColumnTypeName(i + 1));
		}

		ArrayList<Method> arl = new ArrayList<Method>();
		ArrayList<Object> retObject = new ArrayList<Object>();

		for (int i = 0; i < cls.getMethods().length; i++) {
			if (cls.getMethods()[i].getName().startsWith("set")) {
				arl.add(cls.getMethods()[i]);
			}
		}
		Method tempMethod[] = new Method[arl.size()];
		System.arraycopy(arl.toArray(), 0, tempMethod, 0, arl.size());

		while (rs.next()) {
			obj = cls.newInstance();
			for (String temy : metadata.keySet()) {
				for (int i = 0; i < tempMethod.length; i++) {
					if (("set" + temy).equals(tempMethod[i].getName())) {
						if ("numeric".equals(metadata.get(temy))) {
							tempMethod[i].invoke(obj, rs.getLong(temy));
						} else if ("text".equals(metadata.get(temy))) {
							tempMethod[i].invoke(obj, rs.getString(temy));
						}else if ("date".equals(metadata.get(temy))) {
							tempMethod[i].invoke(obj, rs.getDate(temy));
						}
					}
				}
			}
			retObject.add(obj);
		}
		return retObject.toArray();
	}

}