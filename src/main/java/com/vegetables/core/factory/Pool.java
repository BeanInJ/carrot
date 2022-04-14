package com.vegetables.core.factory;

import java.lang.annotation.Annotation;

public interface Pool {
    /**
     * 用于工厂向Pool中分配内容
     */
    void add(Object o);

    /**
     * 清洗无效内容
     */
    void trim();

    /**
     * 返回该类池对应的注解
     */
    Class<? extends Annotation> getLabel();
}
