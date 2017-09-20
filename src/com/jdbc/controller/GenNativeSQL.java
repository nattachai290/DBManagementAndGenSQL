package com.jdbc.controller;

import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;
import com.jdbc.bean.SQLValue;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenNativeSQL extends SQLValue {

	public GenNativeSQL(Object obj){
		this.class_bean = obj.getClass();
		this.bean = obj;
	}

	public GenNativeSQL(Class classz){
		this.class_bean = classz;
	}

	@SuppressWarnings("rawtypes")
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
		initialSelect(this.class_bean);
	}

	public void settingInsert() {
		initialInsert(this.bean);
	}

	public void settingUpdate() {
		initialUpdate(this.bean);
	}

	public PreparedStatement settingPreparedStatement(PreparedStatement ps){
		return setPrepare(this.list,ps);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialSelect(Class classz){
		if(validateOnecQuery()) return;
		StringBuilder sql = new StringBuilder("");
		try{
			Table table = (Table) classz.getAnnotation(Table.class);
			Method[] method = classz.getDeclaredMethods();
			sql.append("SELECT  ");
			for(Method me : method){
				if(me.isAnnotationPresent(Column.class)) {
					Column column = me.getAnnotation(Column.class);
					sql.append(column.name()+", ");
				}
			}
			sql.deleteCharAt(sql.toString().length()-2);
			setSqlHeader(sql.toString());

			sql = new StringBuilder("");
			sql.append(" FROM "+table.name());
			setSqlMid(sql.toString());

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.selectNow = true;
		}
	}

	private void initialInsert(Object obj){
		if(validateOnecQuery()) return;

		StringBuilder sql1 = new StringBuilder("");
		StringBuilder sql2 = new StringBuilder("");
		try{
			Class<? extends Object> classz = obj.getClass();
			Table table = (Table) classz.getAnnotation(Table.class);
			Method[] method = classz.getDeclaredMethods();
			if(this.list == null) {
				this.list = new ArrayList<Object>();
			}

			sql1.append("INSERT INTO "+table.name()+ " ( ");
			sql2.append(" VALUES ( ");
			for(Method me : method){
				me.setAccessible(true);
				if(me.isAnnotationPresent(Column.class) && me.invoke(obj)!=null) {
					if(!me.getReturnType().isPrimitive() || me.getReturnType().isPrimitive() && ((Number) me.invoke(obj)).doubleValue() != 0) {
						Column column = me.getAnnotation(Column.class);
						sql1.append(column.name()+", ");
						sql2.append("?, ");
						this.list.add(me.invoke(obj));
					}
				}else if(me.isAnnotationPresent(EmbeddedId.class)){
					Class<? extends Object> _class_id = me.invoke(obj).getClass();
					Method[] _method = _class_id.getDeclaredMethods();
					for(Method _me : _method){
						_me.setAccessible(true);
						if(_me.isAnnotationPresent(Column.class) && _me.invoke(me.invoke(obj))!=null){
							Column _column = _me.getAnnotation(Column.class);
							sql1.append(_column.name()+", ");
							sql2.append("?, ");
							this.list.add(_me.invoke(me.invoke(obj)));
						}
					}
				}
			}
			sql1.deleteCharAt(sql1.toString().length()-2);
			sql2.deleteCharAt(sql2.toString().length()-2);
			sql1.append(" ) ");
			sql2.append(" ) ");
			setSqlHeader(sql1.toString());
			setSqlMid(sql2.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.insertNow = true;
		}
	}

	private void initialUpdate(Object obj){
		if(validateOnecQuery()) return;
		StringBuilder sqlHeader = new StringBuilder();

		try{
			Class<? extends Object> classz = obj.getClass();
			Table table = (Table) classz.getAnnotation(Table.class);
			Method[] method = classz.getDeclaredMethods();
			if(this.list == null) {
				this.list = new ArrayList<Object>();
			}
			sqlHeader.append("UPDATE "+table.name()+ " SET ");
			for(Method me : method){
				me.setAccessible(true);
				if(me.isAnnotationPresent(Column.class) && me.invoke(obj)!=null) {
					if(!me.getReturnType().isPrimitive() || me.getReturnType().isPrimitive() && ((Number) me.invoke(obj)).doubleValue() != 0) {
						Column column = me.getAnnotation(Column.class);
						sqlHeader.append(column.name()+" = ? ,");
						this.list.add(me.invoke(obj));
					}
				}else if(me.isAnnotationPresent(EmbeddedId.class)){
					Class<? extends Object> _class_id = me.invoke(obj).getClass();
					Method[] _method = _class_id.getDeclaredMethods();
					for(Method _me : _method){
						_me.setAccessible(true);
						if(_me.isAnnotationPresent(Column.class) && _me.invoke(me.invoke(obj))!=null){
							Column _column = _me.getAnnotation(Column.class);
							sqlHeader.append(_column.name()+" = ? ,");
							this.list.add(_me.invoke(me.invoke(obj)));
						}
					}
				}
			}

			sqlHeader.deleteCharAt(sqlHeader.toString().length()-1);
			setSqlHeader(sqlHeader.toString());
		}catch (NullPointerException e) {
			System.out.println("Not compatible class");
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.updateNow = true;
		}
	}

	@SuppressWarnings("unchecked")
	public void where(Oper oper){
		if(this.insertNow || (this.updateNow && this.selectNow) ) return;

		StringBuilder sql = new StringBuilder();
		String operator = " "+oper.getOperation()+" ";
		try {
			char first = Character.toUpperCase(oper.getKey1().charAt(0));
			Method method = this.class_bean.getDeclaredMethod("get"+first+oper.getKey1().substring(1));
			Column column = method.getAnnotation(Column.class);

			if(this.whereCondition) {
				sql.append(" AND ");
			}else {
				sql.append(" WHERE ");
			}
			sql.append(column.name()+operator) ;
			if(oper.getOperation().equalsIgnoreCase("between")) {
				sql.append(checkString(oper.getValue()));
				sql.append(" AND ");
				sql.append(checkString(oper.getValue2())) ;
			}else if(oper.getOperation().equalsIgnoreCase("in")) {
				sql.append("(");
				for(Object val : oper.getArryValue()) {
					sql.append(checkString(val)+",");
				}
				sql.deleteCharAt(sql.toString().length()-1);
				sql.append(") ");
			}else {
				sql.append(checkString(oper.getValue()));
			}
			setSqlTail(sql.toString());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}finally {
			this.whereCondition = true;
		}
	}

	@SuppressWarnings("unchecked")
	public void alias(Name name){
		if(!this.selectNow) return;

		String alias = " As "+name.getName()+" ";
		name.getName();
		try {
			char first = Character.toUpperCase(name.getKey1().charAt(0));
			Method method = this.class_bean.getDeclaredMethod("get"+first+name.getKey1().substring(1));
			Column column = method.getAnnotation(Column.class);
			int index = this.header.toString().indexOf(column.name());

			this.header.insert(index+column.name().length(), alias);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	private Object checkString(Object value) {
		if(value instanceof String){
			value = "'"+value+"'";
		}else if(value instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			Date date = (Date) value;
			value = "to_date('"+sdf.format(date)+"','dd/MM/yyyy')";
		}
		return value;
	}

	private boolean validateOnecQuery() {
		return 	this.insertNow || this.selectNow || this.updateNow;
	}

	private PreparedStatement setPrepare(List<Object> valueList,PreparedStatement ps){
		int count = 1;
		try {
			if(CollectionUtils.isNotEmpty(valueList)){
				for(Object value : valueList){
					if(value instanceof Date){
						Date stamp = (Date) value;
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(stamp.getTime());
						ps.setDate(count++, new java.sql.Date(calendar.getTime().getTime()));
					}else{
						ps.setObject(count++, value);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}

}
