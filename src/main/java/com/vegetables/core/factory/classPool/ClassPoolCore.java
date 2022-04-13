package com.vegetables.core.factory.classPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 类加载池核心，所有的类加载池都应该有一个ClassPoolCore
 */
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