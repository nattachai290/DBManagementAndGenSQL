package com.jdbc.controller.impl;

import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;
import com.jdbc.bean.SQLValue;
import com.jdbc.controller.Option;

import javax.persistence.Column;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OptionImpl extends SQLValue implements Option {

    @Override
    public void where(Oper oper) {
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

    @Override
    public void alias(Name name) {
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
}
