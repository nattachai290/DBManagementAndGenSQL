package com.db.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

	protected Connection conn;
	protected PreparedStatement preparedStatement;
	protected Statement stmt;
	
	protected Connection getConnection() throws Exception{
		try{
			//open connection
			if(conn == null){
				conn = DBConnectionManager.getEMCIConnection();
			}
		}catch (SQLException e) {
			// TODO: handle exception
			 throw new Exception("DBManage[getConnection] :"+e.getMessage());
		}catch (Exception e) {
			// TODO: handle exception
			 throw new Exception("DBManage[getConnection] :"+e.getMessage());
		}
		return conn;
	}
	
	protected void closeConnection() throws SQLException{
		
		if(stmt!=null){
			stmt.close();
			stmt = null;
		}
		
		if(conn!=null){
			conn.close();
			conn = null;
		}
		
		if(preparedStatement!=null){
			preparedStatement.close();
			preparedStatement = null;
		}
	}
}
