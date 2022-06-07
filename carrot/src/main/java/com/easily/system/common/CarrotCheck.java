package com.easily.system.common;

import com.easily.system.exception.NullParamException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarrotCheck {
    public static void cantNull(Map<String,?> map,String... strings){
        List<String> nullKey = new ArrayList<>();
        for (String key:strings){
            Object o = map.get(key);
            if (o==null) nullKey.add(key);
        }
        if (nullKey.size()>0) throw new NullParamException(nullKey);
    }

    public static void cantNull(Object pojo,String... strings) {
        List<String> nullKey = new ArrayList<>();
        Class<?> aClass = pojo.getClass();
        try {
            for (String key : strings) {
                Field field = aClass.getDeclaredField(key);
                field.setAccessible(true);
                if (field.getName().equals(key)) {
                    if (field.get(pojo) == null) nullKey.add(key);
                }
            }
        }catch (NoSuchFieldException | IllegalAccessException exception){
            throw new RuntimeException(exception);
        }
        if (nullKey.size()>0) throw new NullParamException(nullKey);
    }
}
