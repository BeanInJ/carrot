package com.carrot.system.java.com.carrot.core;

/**
 * 加载流程控制
 */
public interface LoadFlow extends Flow{
    /**
     * 重载
     */
    void reload();

    void afterAdd(Step step);

    void addMain(Class<?> clazz);
}
