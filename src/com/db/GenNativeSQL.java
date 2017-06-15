package com.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

public class GenNativeSQL {

	private Integer count = null;
	
	public String genInsertFromClass(Object entity){
		StringBuilder sqlHeader = new StringBuilder();
		StringBuilder sqlTail = new StringBuilder();
		boolean checkNull = true;
		try{
			Class<? extends Object> bean = entity.getClass();
			Table table = (Table) bean.getAnnotation(Table.class);
			Field[] fields = bean.getDeclaredFields();

			sqlHeader.append("INSERT INTO "+table.name()+ " ( ");
			sqlTail.append(" VALUES ( ");
			for(Field f : fields){
				f.setAccessible(true);
				if(f.get(entity) != null && f.isAnnotationPresent(Column.class)){
					checkNull = false;
					Column column = f.getAnnotation(Column.class);
					sqlHeader.append(column.name()+", ");
					sqlTail.append("?, ");
				}
			}
			sqlHeader.deleteCharAt(sqlHeader.toString().length()-2);
			sqlTail.deleteCharAt(sqlTail.toString().length()-2);
			sqlHeader.append(" ) ");
			sqlTail.append(" ) ");
			
			sqlHeader.append(sqlTail.toString());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return checkNull? null:sqlHeader.toString();
	}
	
	public PreparedStatement genInsertPreparedStatement(Object entity, PreparedStatement prepared){
		try{
			Class<? extends Object> bean = entity.getClass();
			Field[] fields = bean.getDeclaredFields();
			count = 1;
			prepared = setPrepare(prepared, fields, entity);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return prepared;
	}

	public String genUpdateFromClass(Object entity,Object entityKey){
		StringBuilder sqlHeader = new StringBuilder();
		StringBuilder sqlTail = new StringBuilder();
		boolean checkNull = true;
		boolean checkNullKey = true;
		try{
			Class<? extends Object> bean = entity.getClass();
			Class<? extends Object> key = entityKey.getClass();
			
			Table table = (Table) bean.getAnnotation(Table.class);
			Field[] fields = bean.getDeclaredFields();
			Field[] fieldsKey = key.getDeclaredFields();
			
			sqlHeader.append("UPDATE "+table.name()+ " SET ");
			sqlTail.append(" WHERE ");
			for(Field f : fields){
				f.setAccessible(true);
				if(f.get(entity) != null && f.isAnnotationPresent(Column.class)){
					checkNull = false;
					Column column = f.getAnnotation(Column.class);
					sqlHeader.append(column.name()+" = ? ,");
				}
			}
			for(Field f : fieldsKey){
				f.setAccessible(true);
				if(f.get(entityKey) != null && f.isAnnotationPresent(Column.class)){
					checkNullKey = false;
					Column column = f.getAnnotation(Column.class);
					sqlTail.append(column.name()+" = ? ,");
				}
			}
			
			sqlHeader.deleteCharAt(sqlHeader.toString().length()-1);
			sqlTail.deleteCharAt(sqlTail.toString().length()-1);
			sqlHeader.append(sqlTail.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return (checkNull || checkNullKey) ? null: sqlHeader.toString();
	}
	
	public PreparedStatement genUpdatePreparedStatement(Object entity,Object entity2, PreparedStatement prepared){
		try{
			Class<? extends Object> bean = entity.getClass();
			Class<? extends Object> bean2 = entity2.getClass();
			
			Field[] fields = bean.getDeclaredFields();
			Field[] fields2 = bean2.getDeclaredFields();
			count = 1;
			prepared = setPrepare(prepared, fields, entity);
			prepared = setPrepare(prepared, fields2, entity2);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return prepared;
	}
	
	public String genSelectFromClass(Object entity,boolean star){
		StringBuilder sql = new StringBuilder();
		boolean checkNull = true;
		try{
			Class<? extends Object> bean = entity.getClass();
			Table table = (Table) bean.getAnnotation(Table.class);
			Field[] fields = bean.getDeclaredFields();
			
			sql.append("SELECT  ");
			for(Field f : fields){
				f.setAccessible(true);
				checkNull = false;
				if(f.isAnnotationPresent(Column.class)){
					Column column = f.getAnnotation(Column.class);
					if(star){
						sql.append(column.name()+", ");
					}else{
						if(f.get(entity) != null){
							sql.append(column.name()+", ");
						}
					}
				}
			}
			sql.deleteCharAt(sql.toString().length()-2);
			sql.append(" FROM "+table.name());
			if(!star){
				sql.append("WHERE  ");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return checkNull? null: sql.toString();
	}
	
	private PreparedStatement setPrepare(PreparedStatement prepared,Field[] fields,Object entity){
		
		for(Field f : fields){
			f.setAccessible(true);
			try {
				if(f.get(entity) != null && f.isAnnotationPresent(Column.class)){
					Object value  = f.get(entity);
					if(value instanceof Date){
						Date stamp = (Date) value;
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(stamp.getTime());
						prepared.setDate(count++, new java.sql.Date(calendar.getTime().getTime()));
					}else{
						prepared.setObject(count++, value);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return prepared;
	}

}
