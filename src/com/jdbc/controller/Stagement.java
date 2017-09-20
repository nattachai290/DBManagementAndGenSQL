package com.jdbc.controller;

import java.sql.PreparedStatement;
import java.util.List;

public interface Stagement {

    public abstract void select(Class _class);
    public abstract void update(Object _object);
    public abstract void insert(Object _object);
    public abstract PreparedStatement preparedStatement(List<Object> valueList, PreparedStatement ps);


}
