package com.xyy.util;

public class DateUtils {

    public static Long nowDate(){
        return System.currentTimeMillis();
    }

    public static Long costTime(Long before){
        return System.currentTimeMillis() - before;
    }
}
