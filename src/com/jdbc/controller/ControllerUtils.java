package com.jdbc.controller;

import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ControllerUtils {

    public static Object checkString(Object value) {
        if (value instanceof String) {
            value = "'" + value + "'";
        } else if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date date = (Date) value;
            value = "to_date('" + sdf.format(date) + "','dd/MM/yyyy')";
        }
        return value;
    }

    public static boolean validateOnceQuery(boolean insertNow, boolean selectNow, boolean updateNow, boolean deleteNow) {
        return insertNow || selectNow || updateNow || deleteNow;
    }

    protected PreparedStatement preparedStatement(List<Object> valueList, PreparedStatement ps) {
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

    public void checkAnnotation(Method method, Object obj, StringBuilder sql, StringBuilder sql2, String suffix, String option, boolean isJoin,List<Object> listValue) throws InvocationTargetException, IllegalAccessException {
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

        if(value!=null){
            if (method.isAnnotationPresent(Column.class) && isJoin) {
                if (!method.getReturnType().isPrimitive()
                        || method.getReturnType().isPrimitive() && ((Number) value).doubleValue() != 0) {
                    Column column = method.getAnnotation(Column.class);
                    System.out.println(column.name());
                    sql.append(column.name()+suffix);
                    listValue.add(value);

                    if(option!=null){
                        sql2.append(option);
                    }
                }
            }else if (method.isAnnotationPresent(EmbeddedId.class)) {
                Class<? extends Object> _class_id = value.getClass();
                Method[] _method = _class_id.getDeclaredMethods();
                for (Method _me : _method) {
                    _me.setAccessible(true);
                    checkAnnotation(_me,value,sql,sql2,suffix,option,Boolean.TRUE,listValue);
                }
            }else if (method.isAnnotationPresent(JoinColumn.class)) {
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
                        checkAnnotation(_me,value,sql,sql2,suffix,option,Boolean.FALSE,listValue);
                    }
                }
            }else if(method.isAnnotationPresent(Id.class)){
                listValue.add(value);
            }
        }
    }


}
