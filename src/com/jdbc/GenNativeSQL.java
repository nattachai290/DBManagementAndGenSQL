package com.jdbc;

import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;
import com.jdbc.bean.SQLValue;

import java.sql.PreparedStatement;

public class GenNativeSQL extends SQLValue {

	public GenNativeSQL(Object obj){
		this.class_bean = obj.getClass();
		this.bean = obj;
	}

	public GenNativeSQL(Class classz){
		this.class_bean = classz;
	}

	public static GenNativeSQL forCLASS(Object entity){
		GenNativeSQL nativeSql = null;
		if(entity instanceof Class ) {
			nativeSql = new GenNativeSQL((Class) entity);
		}else{
			nativeSql = new GenNativeSQL(entity);
		}
		return nativeSql;
	}

	public void settingSelect() {
		this.stagement.select(this.class_bean);
	}

	public void settingInsert() {
		this.stagement.insert(this.bean);
	}

	public void settingUpdate() {
		this.stagement.update(this.bean);
	}

	public PreparedStatement settingPreparedStatement(PreparedStatement ps){
		return this.stagement.preparedStatement(this.list,ps);
	}

	public void where(Oper oper) {
		this.option.where(oper);
	}

	public void alias(Name alias) {
		this.option.alias(alias);
	}
}
