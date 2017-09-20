package com.jdbc.bean;

import java.util.Collection;

public class MainValue<E> {
	
	protected String key1;
	protected String key2;
	protected Object value;
	protected Object value2;
	protected String operation;
	protected Collection<E> arryValue;
	protected Oper oper;
	
	protected String name;
	
	public Object getValue() {
		return value;
	}
	public Object getValue2() {
		return value2;
	}
	public String getOperation() {
		return operation;
	}
	public Collection<E> getArryValue() {
		return arryValue;
	}
	public String getKey1() {
		return key1;
	}
	public String getKey2() {
		return key2;
	}
	public String getName() {
		return name;
	}
	
	
	

	
	
	
}
