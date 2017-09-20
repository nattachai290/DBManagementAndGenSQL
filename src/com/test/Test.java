package com.test;

import com.db.ResultSetMapper;
import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;
import com.jdbc.controller.GenNativeSQL;
import com.db.manager.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		Connection conn = getConnection();
		//Update parameter Object from bean
		GenNativeSQL sqlUpdate = GenNativeSQL.forCLASS("");
		sqlUpdate.settingUpdate();
		PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate.getNativeSQL());
		sqlUpdate.settingPreparedStatement(psUpdate).executeUpdate();

		//Insert parameter Object from bean
		GenNativeSQL sqlInsert = GenNativeSQL.forCLASS("");
		sqlInsert.settingInsert();
		PreparedStatement psInsert = conn.prepareStatement(sqlInsert.getNativeSQL());
		sqlInsert.settingPreparedStatement(psInsert).executeUpdate();

		//Select parameter Class from bean
		GenNativeSQL sqlSelect = GenNativeSQL.forCLASS("");
		sqlSelect.settingSelect();
		//Alias
		sqlSelect.alias(Name.as("name","alias"));

		//Condition
		sqlSelect.where(Oper.between("name","ket1","key2"));
		sqlSelect.where(Oper.in("name",new ArrayList<String>()));
		sqlSelect.where(Oper.eq("key1","key2"));
		sqlSelect.where(Oper.ge("key1","key2"));
		sqlSelect.where(Oper.gt("key1","key2"));
		sqlSelect.where(Oper.le("key1","key2"));
		sqlSelect.where(Oper.lt("key1","key2"));
		sqlSelect.where(Oper.like("name","key2%"));
		sqlSelect.where(Oper.notEq("name","key2%"));

		PreparedStatement psSelect = conn.prepareStatement(sqlSelect.getNativeSQL());
		ResultSet resultSet = sqlInsert.settingPreparedStatement(psSelect).executeQuery();
		ResultSetMapper rsm = new ResultSetMapper();
		List<WelfarePayeeComp> list = rsm.mapRersultSetToObject(resultSet, WelfarePayeeComp.class);
		for(WelfarePayeeComp wel : list) {
			System.out.println(wel.toString());
		}
	}

	private static Connection getJdbcConnection(String url, String user, String pass) {
		Connection conn = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	private static Connection getConnection() throws Exception{
		Connection conn = null;
		try{
			//open connection
			if(conn == null){
				conn = getJdbcConnection("jdbc:db2://10.2.154.15:50013/CORT", "coradm", "coradm");
			}
		}catch (Exception e) {
			// TODO: handle exception
			throw new Exception("DBManage[getConnection] :"+e.getMessage());
		}
		return conn;
	}


}
