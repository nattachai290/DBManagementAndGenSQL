package com.jdbc.bean;

import java.util.List;

public class SQLValue {

    protected StringBuilder header = new StringBuilder();
    protected StringBuilder middle = new StringBuilder();
    protected StringBuilder tailer = new StringBuilder();
    protected Object bean;
//    protected Object key_bean;
    protected Class class_bean;
    protected List<Object> list;

    protected Boolean whereCondition = false;
    protected Boolean selectNow = false;
    protected Boolean insertNow = false;
    protected Boolean updateNow = false;

    public String getNativeSQL() {
        return header.toString()+" "+middle.toString()+" "+tailer.toString();
    }

    protected void setSqlHeader(String sql) {
        this.header.append(sql);
    }

    protected void setSqlMid(String sql) {
        this.middle.append(sql);
    }

    protected void setSqlTail(String sql) {
        this.tailer.append(sql);
    }

}
