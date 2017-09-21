package com.jdbc;

import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;
import com.jdbc.bean.SQLValue;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenNativeSQL extends SQLValue {

    public GenNativeSQL(Object obj) {
        this.class_bean = obj.getClass();
        this.bean = obj;
    }

//    public GenNativeSQL(Object bean,Object key_bean) {
//        this.bean = bean;
//        this.key_bean = key_bean;
//    }


    public GenNativeSQL(Class classz) {
        this.class_bean = classz;
    }

    public static GenNativeSQL forCLASS(Object entity) {
        GenNativeSQL nativeSql = null;
        if (entity instanceof Class) {
            nativeSql = new GenNativeSQL((Class) entity);
        } else {
            nativeSql = new GenNativeSQL(entity);
        }
        return nativeSql;
    }

//    public static GenNativeSQL forCLASS(Object entity,Object key) {
//        return new GenNativeSQL(entity,key);
//    }

    public void settingSelect() {
        select(this.class_bean);

    }

    public PreparedStatement settingInsertWithConnection(Connection connection) throws SQLException {
        insert(this.bean);
        PreparedStatement prepareStatement = connection.prepareStatement(getNativeSQL());
        return settingPreparedStatement(prepareStatement);
    }

    public void settingInsert(){
        insert(this.bean);
    }


    public PreparedStatement settingUpdateWithConnection(Connection connection) throws SQLException {
        update(this.bean);
        PreparedStatement prepareStatement = connection.prepareStatement(getNativeSQL());
        return settingPreparedStatement(prepareStatement);
    }

    public void settingUpdate(){
        update(this.bean);
    }


    public PreparedStatement settingPreparedStatement(PreparedStatement ps) {
        return preparedStatement(this.list, ps);
    }

    private void select(Class classz) {
        if (validateOnceQuery()) return;
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
            setSqlHeader(sql.toString());
            setSqlMid(" FROM " + table.name());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.selectNow = true;
        }
    }

    private void checkAnnotation(Method method,Object obj,StringBuilder sql,StringBuilder sql2,String suffix,String option,boolean isJoin) throws InvocationTargetException, IllegalAccessException {
        Object value = null;
        if (method.isAnnotationPresent(Column.class)){
            value = method.invoke(obj);
        }else if (method.isAnnotationPresent(EmbeddedId.class)){
            value = method.invoke(obj);
        }else if (method.isAnnotationPresent(JoinColumn.class)) {
            value = method.invoke(obj);
        }else if(method.isAnnotationPresent(Id.class)){
            value = method.invoke(obj);
        }

        if (method.isAnnotationPresent(Column.class) && value != null && isJoin) {
            if (!method.getReturnType().isPrimitive()
                    || method.getReturnType().isPrimitive() && ((Number) value).doubleValue() != 0) {
                Column column = method.getAnnotation(Column.class);
                System.out.println(column.name());
                sql.append(column.name()+suffix);
                this.list.add(value);

                if(option!=null){
                    sql2.append(option);
                }
            }
        }else if (method.isAnnotationPresent(EmbeddedId.class) && value != null) {
            Class<? extends Object> _class_id = value.getClass();
            Method[] _method = _class_id.getDeclaredMethods();
            for (Method _me : _method) {
                _me.setAccessible(true);
                checkAnnotation(_me,value,sql,sql2,suffix,option,Boolean.TRUE);
            }
        }else if (method.isAnnotationPresent(JoinColumn.class) && value != null) {
            JoinColumn join = method.getAnnotation(JoinColumn.class);
            if (!(join.referencedColumnName() != null && !join.referencedColumnName().trim().equalsIgnoreCase(""))) {
                Class<? extends Object> _class_id = value.getClass();
                sql.append(join.name()+suffix);
                if(option!=null){
                    sql2.append(option);
                }
                Method[] _method = _class_id.getDeclaredMethods();
                for (Method _me : _method) {
                    _me.setAccessible(true);
                    checkAnnotation(_me,value,sql,sql2,suffix,option,Boolean.FALSE);
                }
            }
        }else if(method.isAnnotationPresent(Id.class)&& value != null){
            this.list.add(value);
        }
    }

    private void update(Object obj) {
        if (validateOnceQuery()) return;
        StringBuilder sqlHeader = new StringBuilder();
        String suffix = " = ? ,";
        try {
            Class<? extends Object> classz = obj.getClass();
            Table table = (Table) classz.getAnnotation(Table.class);
            Method[] method = classz.getDeclaredMethods();
            if (this.list == null) {
                this.list = new ArrayList<Object>();
            }
            sqlHeader.append("UPDATE " + table.name() + " SET ");
            for (Method me : method) {
                me.setAccessible(true);
                checkAnnotation(me,obj,sqlHeader,null,suffix,null,Boolean.TRUE);
            }
            sqlHeader.deleteCharAt(sqlHeader.toString().length() - 1);
            setSqlHeader(sqlHeader.toString());
        } catch (NullPointerException e) {
            System.out.println("Not compatible class");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.updateNow = true;
        }
    }

    private void insert(Object obj) {
        if (validateOnceQuery()) return;

        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        String suffix = ", ";
        String option = "?, ";
        try {
            Class<? extends Object> classz = obj.getClass();
            Table table = (Table) classz.getAnnotation(Table.class);
            Method[] method = classz.getDeclaredMethods();
            if (this.list == null) {
                this.list = new ArrayList<Object>();
            }

            sql1.append("INSERT INTO " + table.name() + " ( ");
            sql2.append(" VALUES ( ");
            for (Method me : method) {
                me.setAccessible(true);
                checkAnnotation(me,obj,sql1,sql2,suffix,option,Boolean.TRUE);
            }
            sql1.deleteCharAt(sql1.toString().length() - 2);
            sql2.deleteCharAt(sql2.toString().length() - 2);
            sql1.append(") ");
            sql2.append(") ");
            setSqlHeader(sql1.toString());
            setSqlMid(sql2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.insertNow = true;
        }
    }

    private PreparedStatement preparedStatement(List<Object> valueList, PreparedStatement ps) {
        int count = 1;
        try {
            if (CollectionUtils.isNotEmpty(valueList)) {
                for (Object value : valueList) {
                    if (value instanceof Date) {
                        Date stamp = (Date) value;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(stamp.getTime());
                        ps.setDate(count++, new java.sql.Date(calendar.getTime().getTime()));
                    } else {
                        ps.setObject(count++, value);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    private boolean validateOnceQuery() {
        return this.insertNow || this.selectNow || this.updateNow;
    }

    public void where(Oper oper) {
        if (this.insertNow || (this.updateNow && this.selectNow)) return;

        StringBuilder sql = new StringBuilder();
        String operator = " " + oper.getOperation() + " ";
        try {
            char first = Character.toUpperCase(oper.getKey1().charAt(0));
            Method method = this.class_bean.getDeclaredMethod("get" + first + oper.getKey1().substring(1));
            Column column = method.getAnnotation(Column.class);

            if (this.whereCondition) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
            }
            sql.append(column.name() + operator);
            if (oper.getOperation().equalsIgnoreCase("between")) {
                sql.append(checkString(oper.getValue()));
                sql.append(" AND ");
                sql.append(checkString(oper.getValue2()));
            } else if (oper.getOperation().equalsIgnoreCase("in")) {
                sql.append("(");
                for (Object val : oper.getArryValue()) {
                    sql.append(checkString(val) + ",");
                }
                sql.deleteCharAt(sql.toString().length() - 1);
                sql.append(") ");
            } else {
                sql.append(checkString(oper.getValue()));
            }
            setSqlTail(sql.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            this.whereCondition = true;
        }
    }

    public void alias(Name name) {
        if (!this.selectNow) return;

        String alias = " As " + name.getName() + " ";
        name.getName();
        try {
            char first = Character.toUpperCase(name.getKey1().charAt(0));
            Method method = this.class_bean.getDeclaredMethod("get" + first + name.getKey1().substring(1));
            Column column = method.getAnnotation(Column.class);
            int index = this.header.toString().indexOf(column.name());

            this.header.insert(index + column.name().length(), alias);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private Object checkString(Object value) {
        if (value instanceof String) {
            value = "'" + value + "'";
        } else if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date date = (Date) value;
            value = "to_date('" + sdf.format(date) + "','dd/MM/yyyy')";
        }
        return value;
    }

}
