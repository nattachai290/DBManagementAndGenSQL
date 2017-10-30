package com.jdbc.controller;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ControllerCRUD {

    public boolean select(Class classz,boolean insertNow,boolean selectNow,boolean updateNow,boolean deleteNow,StringBuilder sqlHeader,StringBuilder sqlMid) {
        if (ControllerUtils.validateOnceQuery(insertNow,selectNow,updateNow,deleteNow)) return false;
        StringBuilder sql = new StringBuilder();
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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return true;
        }
    }

    private boolean update(Object obj,boolean insertNow,boolean selectNow,boolean updateNow,boolean deleteNow,StringBuilder sql,List<Object> listValue) {
        if (ControllerUtils.validateOnceQuery(insertNow,selectNow,updateNow,deleteNow)) return false;
        StringBuilder sqlHeader = new StringBuilder();
        String suffix = " = ? ,";
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
            sql.append(sqlHeader.toString());
        } catch (NullPointerException e) {
            System.out.println("Not compatible class");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return true;
        }
    }

    private boolean insert(Object obj,boolean insertNow,boolean selectNow,boolean updateNow,boolean deleteNow,StringBuilder sqlHead,StringBuilder sqlMid,List<Object> listValue) {
        if (ControllerUtils.validateOnceQuery(insertNow,selectNow,updateNow,deleteNow)) return false;

        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        String suffix = ", ";
        String option = "?, ";
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return true;
        }
    }

}
