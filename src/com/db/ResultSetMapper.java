package com.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.commons.beanutils.BeanUtils;

public class ResultSetMapper<T> {

    public static List resultSetMapper(ResultSet rs, Class outputClass) {
        return new ResultSetMapper().mapResultSetToObject(rs, outputClass);
    }

    private List<T> mapResultSetToObject(ResultSet rs, Class outputClass) {
        List<T> outputList = new ArrayList<T>();
        try {
            if (rs != null) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Field[] fields = outputClass.getDeclaredFields();
                Map<String, String> nameFieldMap = new HashMap<String, String>();

                for (Field f : fields) {
                    if (f.isAnnotationPresent(Column.class)) {
                        Column column = f.getAnnotation(Column.class);
                        nameFieldMap.put(column.name(), f.getName());
                    }
                }
                while (rs.next()) {
                    T bean = (T) outputClass.newInstance();
                    for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                        String columnName = rsmd.getColumnName(_iterator + 1);
                        Object columnValue = rs.getObject(_iterator + 1);
                        if (nameFieldMap.containsKey(columnName) && columnValue != null) {
                            if (columnValue instanceof Timestamp) {
                                Timestamp stamp = (Timestamp) columnValue;
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(stamp.getTime());
                                BeanUtils.setProperty(bean, nameFieldMap.get(columnName), calendar.getTime());
                            } else {
                                BeanUtils.setProperty(bean, nameFieldMap.get(columnName), columnValue);
                            }
                        }
                    }
                    outputList.add(bean);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return outputList;
    }
}
