package com.jdbc.controller;

import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;

import javax.persistence.Column;
import java.lang.reflect.Method;

public class ControllerSelect {

    public String where(Oper oper,boolean whereCondition,boolean insertNow,boolean updateNow,boolean selectNow,Class classz) {
        if (insertNow || (updateNow && selectNow)) return null;

        StringBuilder sql = new StringBuilder();
        String operator = " " + oper.getOperation() + " ";
        try {
            char first = Character.toUpperCase(oper.getKey1().charAt(0));
            Method method = classz.getDeclaredMethod("get" + first + oper.getKey1().substring(1));
            Column column = method.getAnnotation(Column.class);

            if (whereCondition) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }
            sql.append(column.name() + operator);
            if (oper.getOperation().equalsIgnoreCase("between")) {
                sql.append(ControllerUtils.checkString(oper.getValue()));
                sql.append(" AND ");
                sql.append(ControllerUtils.checkString(oper.getValue2()));
            } else if (oper.getOperation().equalsIgnoreCase("in")) {
                sql.append("(");
                for (Object val : oper.getArryValue()) {
                    sql.append(ControllerUtils.checkString(val) + ",");
                }
                sql.deleteCharAt(sql.toString().length() - 1);
                sql.append(") ");
            } else {
                sql.append(ControllerUtils.checkString(oper.getValue()));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }finally {
            return sql.toString();
        }
    }

    public void alias(Name name,boolean selectNow,Class classz,StringBuilder headerSQL) {
        if (!selectNow) return ;

        String alias = " As " + name.getName() + " ";
        name.getName();
        try {
            char first = Character.toUpperCase(name.getKey1().charAt(0));
            Method method = classz.getDeclaredMethod("get" + first + name.getKey1().substring(1));
            Column column = method.getAnnotation(Column.class);
            int index = headerSQL.toString().indexOf(column.name());
            headerSQL.insert(index + column.name().length(), alias);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
