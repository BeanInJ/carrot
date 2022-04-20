package com.carrot.factory;

import java.lang.annotation.Annotation;

public interface Pool {
    /**
     * 用于工厂向Pool中分配内容
     */
    void add(Class<?> clazz);

    /**
     * 清洗无效内容
     */
    void trim();

    /**
     * 返回该类池对应的注解
     */
    Class<? extends Annotation> getLabel();

    /**
     * 返回类池ID
     */
    String getPoolName();

    /**
     * 返回组件
     */
    PoolActuator getActuator();

    /**
     * 初始化功能组件
     */
    void initActuator();
}
