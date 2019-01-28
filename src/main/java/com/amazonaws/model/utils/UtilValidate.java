package com.amazonaws.model.utils;

public class UtilValidate {

    public static Double asDouble(Object o) {
        Double val = null;
        if (o instanceof Number) {
            val = ((Number) o).doubleValue();
        }
        return val;
    }

    public static boolean isEmpty(String string){
        if(string==null || string.length()==0){
            return true;
        } else{
            return false;
        }
    }

    public static boolean isEmpty(Object obj){
        if(obj==null){
            return true;
        } else{
            return false;
        }
    }

}
