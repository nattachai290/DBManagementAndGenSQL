package com.jdbc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonUtils {

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

}
