package com.jdbc.bean;

public class Name extends MainValue{

	protected Name(String key, String name) {
		this.key1 = key;
		this.name = name;
	}
	
	public static Name as(String key,String name){
		return new Name(key,name); 
	}
}
