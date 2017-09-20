package com.jdbc.controller.impl;

import com.jdbc.bean.SQLValue;
import com.jdbc.controller.Stagement;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StagementImpl extends SQLValue implements Stagement {

    @Override
    public void select(Class classz) {
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

    @Override
    public void update(Object obj) {
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

    @Override
    public void insert(Object obj) {
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

    @Override
    public PreparedStatement preparedStatement(List<Object> valueList, PreparedStatement ps) {
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

    private boolean validateOnecQuery() {
        return 	this.insertNow || this.selectNow || this.updateNow;
    }
}
