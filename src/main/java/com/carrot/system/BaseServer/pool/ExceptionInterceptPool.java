package com.carrot.system.BaseServer.pool;

import com.carrot.core.step.factory.Pool;

import java.lang.annotation.Annotation;

/**
 * 异常拦截类池
 */
public class ExceptionInterceptPool implements Pool {
    @Override
    public void add(Object o) {

    }

    @Override
    public void trim() {

    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return null;
    }
}
