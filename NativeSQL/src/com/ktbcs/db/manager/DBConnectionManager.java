package com.ktbcs.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnectionManager {

	public static Connection getEMCIConnection() throws SQLException,Exception {
    	return getConnection("eMCIODSDataSource");
//    	return getJdbcConnection("jdbc:db2://10.2.154.15:50014/CORU", "coradm", "coradm"); //For Test UAT Local Only
//    	return getJdbcConnection("jdbc:db2://10.2.154.15:50013/CORT", "coradm", "coradm"); //For Test SIT Local Only
    }
 
	public static Connection getCBPayConnection() throws SQLException,Exception {
    	return getConnection("CBPayDataSource");
    }
	public static Connection getLogODSConnection() throws SQLException,Exception {
    	return getConnection("LogODSDataSource");
    }
	public static Connection getPortalConnection() throws SQLException,Exception {
    	return getConnection("portalODSDataSource");
    }
	
	 private static Connection getJdbcConnection(String url,String user,String pass) {
		// TODO Auto-generated method stub
		  Connection conn = null;
	      try {
	    	  Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      try {
			conn = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		return conn;
	}

 	private static Connection getConnection(String dataSourceJNDIName) throws SQLException,Exception {
        Connection con = null;
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(dataSourceJNDIName);
            con = ds.getConnection();
        } catch (SQLException e) {  
        	//Important don't add LogUtil to this line and don't remove this System Log printStackTrace().
        	e.printStackTrace(); // Don't remove this line
        	throw e;
        } catch (NamingException ex) {
        	//Important don't add LogUtil to this line and don't remove this System Log printStackTrace().
            throw new Exception(ex.getMessage());
        } catch (Exception e) {
        	//Important don't add LogUtil to this line and don't remove this System Log printStackTrace().
            throw new Exception(e.getMessage());
        }
        return con;        
    }
}
