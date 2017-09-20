package com.test;

import com.db.ResultSetMapper;
import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;
import com.jdbc.controller.GenNativeSQL;
import com.test.bean.Amphur;
import com.test.bean.AmphurId;
import com.test.bean.WelfarePayeeComp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        System.out.println("------------- Start ---------------");
        testUpdate(null);

        System.out.println("------------- end ---------------");
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
			//open CONNECTION
			if(conn == null){
				conn = getJdbcConnection("jdbc:db2://10.2.154.15:50013/CORT", "coradm", "coradm");
			}
		}catch (Exception e) {
			// TODO: handle exception
			throw new Exception("DBManage[getConnection] :"+e.getMessage());
		}
		return conn;
	}

	private static void testUpdate(Connection conn) throws SQLException {

        AmphurId amphurId = new AmphurId();
        amphurId.setAmprCd("0001");
        amphurId.setAmprCtryCd("0002");
        amphurId.setAmprPronProvnCd("0003");

        Amphur amphur = new Amphur();
        amphur.setId(amphurId);
        amphur.setAmprCreatBy("arm");

        //--------------------------- Update parameter Object from bean ---------------------------
        GenNativeSQL sqlUpdate = GenNativeSQL.forCLASS(amphur);
        sqlUpdate.settingUpdate();

        //Condition
        sqlUpdate.where(Oper.between("name","KET1","key2"));
        sqlUpdate.where(Oper.in("name",new ArrayList<String>()));
        sqlUpdate.where(Oper.eq("key1","key2"));
        sqlUpdate.where(Oper.ge("key1","key2"));
        sqlUpdate.where(Oper.gt("key1","key2"));
        sqlUpdate.where(Oper.le("key1","key2"));
        sqlUpdate.where(Oper.lt("key1","key2"));
        sqlUpdate.where(Oper.like("name","key2%"));
        sqlUpdate.where(Oper.notEq("name","key2%"));

        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate.getNativeSQL());
        sqlUpdate.settingPreparedStatement(psUpdate).executeUpdate();
    }

	private void testInsert(Connection conn) throws SQLException {

        WelfarePayeeComp payeeComp = new WelfarePayeeComp();
        payeeComp.setWpcCreateBy("");

        //--------------------------- Insert parameter Object from bean ---------------------------
        GenNativeSQL sqlInsert = GenNativeSQL.forCLASS(payeeComp);
        sqlInsert.settingInsert();
        PreparedStatement psInsert = conn.prepareStatement(sqlInsert.getNativeSQL());
        sqlInsert.settingPreparedStatement(psInsert).executeUpdate();
    }

	private void testSelect(Connection conn) throws SQLException {
        //--------------------------- Select parameter Class from bean ---------------------------
        GenNativeSQL sqlSelect = GenNativeSQL.forCLASS(WelfarePayeeComp.class);
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

        ResultSet resultSet = conn.prepareStatement(sqlSelect.getNativeSQL()).executeQuery();
        List<WelfarePayeeComp> list = ResultSetMapper.resultSetMapper(resultSet, WelfarePayeeComp.class);
        for(WelfarePayeeComp wel : list) {
            System.out.println(wel.toString());
        }

    }


}
