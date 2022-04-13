package com.vegetables.core.factory;

public interface Pool {
    /**
     * 用于工厂向Pool中分配内容
     */
    void add(Object o);

    /**
     * 清洗无效内容
     */
    void trim();
}
