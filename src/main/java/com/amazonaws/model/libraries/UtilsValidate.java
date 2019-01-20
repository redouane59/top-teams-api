package com.amazonaws.model.libraries;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class UtilsValidate {

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
