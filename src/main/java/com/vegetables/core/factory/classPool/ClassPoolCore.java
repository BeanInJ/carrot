package com.vegetables.core.factory.classPool;

import java.util.ArrayList;
import java.util.List;

public class ClassPoolCore {
    private final List<Class<?>> classes = new ArrayList<>();

    public ClassPoolCore() {}

    public List<Class<?>> getClasses() {
        return classes;
    }

    public void setClasses(List<Class<?>> newClasses) {
        classes.clear();
        classes.addAll(newClasses);
    }

    public void add(Class<?> clazz){
        if (!classes.contains(clazz)) {
            getClasses().add(clazz);
        }
    }

    public void remove(Class<?> clazz){
        classes.remove(clazz);
    }
}