package com.jdbc.bean;

import java.util.Collection;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Oper extends MainValue{

	protected Oper(String key, Object value, String operation) {
		this.key1 = key;
		this.value = value;
		this.operation = operation;
	}

	protected Oper(String key, Object value,Object value2, String operation) {
		this.key1 = key;
		this.value = value;
		this.value2 = value2;
		this.operation = operation;
	}
	
	protected <E> Oper(String key,Collection<E> arryValue,String operation) {
		this.key1 = key;
		this.operation = operation;
		this.arryValue = arryValue;
	}

	protected Oper(String key1, String key2, Oper operation) {
		this.key1 = key1;
		this.key2 = key2;
		this.oper = operation;
	}
	
	protected Oper(String key1, String key2) {
		this.key1 = key1;
		this.key2 = key2;
	}
	
	public static Oper key(String key1,String key2,Oper op){
		return new Oper(key1,key2,op); 
	}
	
	public static Oper key(String key1,String key2){
		return new Oper(key1,key2); 
	}
	
	public static Oper eq(String key,Object value){
		return new Oper(key,value,"="); 
	}
	
	public static Oper notEq(String key,Object value){
		return new Oper(key,value,"<>"); 
	}
	
	public static Oper ge(String key,Object value){
		return new Oper(key,value,">="); 
	}
	
	public static Oper le(String key,Object value){
		return new Oper(key,value,"<="); 
	}
	
	public static Oper gt(String key,Object value){
		return new Oper(key,value,">"); 
	}
	
	public static Oper lt(String key,Object value){
		return new Oper(key,value,"<"); 
	}
	
	public static Oper like(String key,Object value){
		return new Oper(key,value,"LIKE"); 
	}
	
	public static <E> Oper in(String key,Collection<E> arrayValue){
		return new Oper(key,arrayValue,"IN"); 
	}
	
	public static Oper between(String key,Object value1,Object value2){
		return new Oper(key,value1,value2,"BETWEEN"); 
	}
}
