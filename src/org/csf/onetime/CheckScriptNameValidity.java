package org.csf.onetime;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.csf.dao.AppConnection;

public class CheckScriptNameValidity {

	public CheckScriptNameValidity() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		Connection con = AppConnection.getPostgresConnection();
		StringBuffer strb = new StringBuffer();
		strb.append("  select \"Key\", \"Name\", \"SecurityID\"  "); 
		strb.append("  from \"Company\"  ");
		strb.append("  where \"ExchangeKey\" = 4  ");
		String rt;
		
		try {
			ResultSet rs = con.prepareStatement(strb.toString()).executeQuery();
			
			while(rs.next()){
				rt = rs.getString("SecurityID");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
