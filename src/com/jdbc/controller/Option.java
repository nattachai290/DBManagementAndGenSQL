package com.jdbc.controller;

import com.jdbc.bean.Name;
import com.jdbc.bean.Oper;

public interface Option {
    public abstract void where(Oper oper);
    public abstract void alias(Name name);
}
