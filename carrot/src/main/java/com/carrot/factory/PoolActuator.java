package com.carrot.factory;

public interface PoolActuator {
    /**
     * 解析
     */
    void parse(Class<?> clazz);

    /**
     * 执行
     */
    <T> void execute(T t);
}
