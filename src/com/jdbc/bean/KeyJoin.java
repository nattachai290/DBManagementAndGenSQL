package com.jdbc.bean;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class KeyJoin {
	
	private String key1;
	private String key2;
	private String table;
	private Map<String,String> column;
	
	public static KeyJoin setKey(Class classz,Oper op){
		return new KeyJoin(classz,op.getKey1(),op.getKey2());
	}
	
	
	public KeyJoin(Class classz, String key1, String key2) {
		Field[] fields = classz.getDeclaredFields();
		if(column == null){
			column = new HashMap<String, String>();
		}
		Table table = (Table) classz.getAnnotation(Table.class);
		
		for(Field f : fields){
			if(f.isAnnotationPresent(Column.class)){
				Column col = f.getAnnotation(Column.class);
				column.put(f.getName(), col.name());
			}
		}
		this.table = table.name();
		this.key1 = key1;
		this.key2 = key2;
	}


	public String getKey1() {
		return key1;
	}

	public String getKey2() {
		return key2;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}


	public Map<String, String> getColumn() {
		return column;
	}


	public void setColumn(Map<String, String> column) {
		this.column = column;
	}


	
	
}
