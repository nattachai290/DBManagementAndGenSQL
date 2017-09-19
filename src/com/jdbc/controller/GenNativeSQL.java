package com.jdbc.controller;

import com.jdbc.bean.SQLValue;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GenNativeSQL extends SQLValue {

	public GenNativeSQL(){

	}

	public GenNativeSQL(Object obj){
		this.bean = obj;
	}

	public PreparedStatement update(){
		return null;
	}

	private PreparedStatement setPrepare(PreparedStatement prepared, List<Object> valueList){
		int count = 1;
		try {
			if(CollectionUtils.isNotEmpty(valueList)){
				for(Object value : valueList){
					if(value instanceof Date){
						Date stamp = (Date) value;
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(stamp.getTime());
						prepared.setDate(count++, new java.sql.Date(calendar.getTime().getTime()));
					}else{
						prepared.setObject(count++, value);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return prepared;
	}

}
