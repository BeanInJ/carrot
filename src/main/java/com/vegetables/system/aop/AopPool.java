package com.vegetables.system.aop;

import java.util.ArrayList;
import java.util.List;

public class AopPool {
    private static final List<Class<?>> classes = new ArrayList<>();

    public static List<Class<?>> getClasses() {
        return classes;
    }

    public static void setClasses(List<Class<?>> classes) {
        AopPool.classes.clear();
        AopPool.classes.addAll(classes);
    }

    public static void add(Class<?> clazz){
        if (!classes.contains(clazz)) {
            AopPool.getClasses().add(clazz);
        }
    }
}
