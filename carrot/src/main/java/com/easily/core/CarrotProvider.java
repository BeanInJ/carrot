package com.easily.core;

import com.easily.factory.Pools;
import com.easily.factory.aop.AopPool;
import com.easily.factory.controller.ControllerPool;
import com.easily.factory.filter.FilterPool;
import com.easily.label.Aop;
import com.easily.label.Controller;
import com.easily.label.Filter;

/**
 * 提供一次请求所需要的资源
 *
 * 静态资源提前加载，避免每次请求进来都加载一次浪费资源
 */
public class CarrotProvider {
    protected static FilterPool filterPool = null;
    protected static ControllerPool controllerPool = null;
    protected static AopPool aopPool = null;

    public void addPools(Pools pools){
        filterPool = pools.get(Filter.class, FilterPool.class);
        controllerPool = pools.get(Controller.class, ControllerPool.class);
        aopPool = pools.get(Aop.class, AopPool.class);
    }
}
