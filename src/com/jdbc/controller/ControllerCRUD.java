package com.jdbc.controller;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ControllerCRUD {

    public boolean select(Class classz,boolean insertNow,boolean selectNow,boolean updateNow,boolean deleteNow,StringBuilder sqlHeader,StringBuilder sqlMid) {
        if (CommonUtils.validateOnceQuery(insertNow,selectNow,updateNow,deleteNow)) return false;
        StringBuilder sql = new StringBuilder();
        boolean success = false;
        try {
            Table table = (Table) classz.getAnnotation(Table.class);
            Method[] method = classz.getDeclaredMethods();
            sql.append("SELECT  ");
            for (Method me : method) {
                if (me.isAnnotationPresent(Column.class)) {
                    Column column = me.getAnnotation(Column.class);
                    sql.append(column.name() + ", ");
                }
            }
            sql.deleteCharAt(sql.toString().length() - 2);
            sqlHeader.append(sql.toString());
            sqlMid.append(" FROM " + table.name());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return success;
        }
    }

    public boolean update(Object obj,boolean insertNow,boolean selectNow,boolean updateNow,boolean deleteNow,StringBuilder sql_header,List<Object> listValue) {
        if (CommonUtils.validateOnceQuery(insertNow,selectNow,updateNow,deleteNow)) return false;
        StringBuilder sqlHeader = new StringBuilder();
        String suffix = " = ? ,";
        boolean success = false;
        try {
            ControllerUtils controllerUtils = new ControllerUtils();
            Class<? extends Object> classz = obj.getClass();
            Table table = (Table) classz.getAnnotation(Table.class);
            Method[] method = classz.getDeclaredMethods();
            if (listValue == null) {
                listValue = new ArrayList<Object>();
            }
            sqlHeader.append("UPDATE " + table.name() + " SET ");
            for (Method me : method) {
                me.setAccessible(true);
                controllerUtils.checkAnnotation(me,obj,sqlHeader,null,suffix,null,Boolean.TRUE,listValue);
            }
            sqlHeader.deleteCharAt(sqlHeader.toString().length() - 1);
            sql_header.append(sqlHeader.toString());
            success = true;
        } catch (NullPointerException e) {
            System.out.println("Not compatible class");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return success;
        }
    }

    public boolean insert(Object obj,boolean insertNow,boolean selectNow,boolean updateNow,boolean deleteNow,StringBuilder sqlHead,StringBuilder sqlMid,List<Object> listValue) {
        if (CommonUtils.validateOnceQuery(insertNow,selectNow,updateNow,deleteNow)) return false;

        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        String suffix = ", ";
        String option = "?, ";
        boolean success = false;
        try {
            ControllerUtils controllerUtils = new ControllerUtils();
            Class<? extends Object> classz = obj.getClass();
            Table table = (Table) classz.getAnnotation(Table.class);
            Method[] method = classz.getDeclaredMethods();
            if (listValue == null) {
                listValue = new ArrayList<Object>();
            }

            sql1.append("INSERT INTO " + table.name() + " ( ");
            sql2.append(" VALUES ( ");
            for (Method me : method) {
                me.setAccessible(true);
                controllerUtils.checkAnnotation(me,obj,sql1,sql2,suffix,option,Boolean.TRUE,listValue);
            }
            sql1.deleteCharAt(sql1.toString().length() - 2);
            sql2.deleteCharAt(sql2.toString().length() - 2);
            sql1.append(") ");
            sql2.append(") ");
            sqlHead.append(sql1.toString());
            sqlMid.append(sql2.toString());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return success;
        }
    }

    public boolean delete(Class classz,boolean insertNow,boolean selectNow,boolean updateNow,boolean deleteNow,StringBuilder sqlHeader) {
        if (CommonUtils.validateOnceQuery(insertNow,selectNow,updateNow,deleteNow)) return false;
        StringBuilder sql = new StringBuilder();
        boolean success = false;
        try {
            Table table = (Table) classz.getAnnotation(Table.class);
            sql.append("DELETE FROM "+table.name());
            sqlHeader.append(sql.toString());
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

}
