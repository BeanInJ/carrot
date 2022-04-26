package com.carrot.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopInvoke{
    private Object proxyObject;
    private AopInvokeFlow flow;

    // 执行方法
    public Object invoke(AopInvokeFlow aop){

        try{
            // 1  方法执行前
            aop.before();
            // true：不执行invoke()、after()方法
            if(aop.gotoFinally()) return aop.result();

            // 2  方法执行
            aop.invoke();
            // true：不执行after()方法
            if(aop.gotoFinally()) return aop.result();

            // 3  方法执行后
            aop.after();
        }catch (Exception e){

            // 4 方法异常时
            aop.catchException(e);
        }finally {

            // 5 方法最终必须执行
            // 任何情况下都会执行
            aop.finallyTty();
        }
        return aop.result();
    }
}
