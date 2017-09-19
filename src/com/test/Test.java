package com.test;

import com.jdbc.controller.GenNativeSQL;
import com.db.manager.DBConnectionManager;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnectionManager test = new DBConnectionManager();
		GenNativeSQL gen = new GenNativeSQL(DBConnectionManager.class);
		
	}

}
