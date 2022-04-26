package com.easily.factory;

import java.lang.annotation.Annotation;

public interface Pool {
    /**
     * 工厂向池中分配原料
     */
    void add(Class<?> clazz);

    /**
     * 返回该类池对应的注解
     */
    Class<? extends Annotation> getLabel();

    /**
     * 返回类池标识符（不可重复）
     */
    String getPoolName();

    /**
     * 清除池中数据
     */
    void clear();

    /**
     * 解析入容器
     */
    void parseToContainer();

    /**
     * 获取产品容器
     */
    <T> T getProductContainer(Class<T> clazz);
}
