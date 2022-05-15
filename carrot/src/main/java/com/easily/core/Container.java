package com.easily.core;

import com.easily.factory.ClassFactory;
import com.easily.factory.Pool;
import com.easily.factory.aop.AopContainer;
import com.easily.factory.controller.ControllerContainer;
import com.easily.factory.exceptionIntercept.ExceptionInterceptContainer;
import com.easily.factory.filter.FilterContainer;
import com.easily.system.dict.INNER;

public class Container {
    /**
     * 切面容器
     */
    private final AopContainer aopContainer;
    /**
     * url方法容器
     */
    private final ControllerContainer controllerContainer;
    /**
     * 过略器方法容器
     */
    private final FilterContainer filterContainer;
    /**
     * 异常拦截器容器
     */
    private ExceptionInterceptContainer exceptionInterceptContainer;

    public Container(ClassFactory classFactory){
        Pool aopPool = classFactory.getPool(INNER.AOP_POOl_NAME);
        aopContainer = aopPool.getProductContainer(AopContainer.class);

        Pool controllerPool = classFactory.getPool(INNER.CONTROLLER_POOl_NAME);
        controllerContainer = controllerPool.getProductContainer(ControllerContainer.class);

        Pool filterPool = classFactory.getPool(INNER.FILTER_POOl_NAME);
        filterContainer = filterPool.getProductContainer(FilterContainer.class);

//        Pool exceptionInterceptPool = ClassFactory.getPool(INNER.EXCEPTION_POOl_NAME);
//        exceptionInterceptContainer = exceptionInterceptPool.getProductContainer(ExceptionInterceptContainer.class);
    }

    public AopContainer getAopContainer() {
        return aopContainer;
    }

    public ControllerContainer getControllerContainer() {
        return controllerContainer;
    }

    public FilterContainer getFilterContainer() {
        return filterContainer;
    }

    public ExceptionInterceptContainer getExceptionInterceptContainer() {
        return exceptionInterceptContainer;
    }
}
