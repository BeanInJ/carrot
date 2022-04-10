package com.vegetables.system.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 方法”保姆“
 *
 * 在方法执行 前、后、异常时、最终执行时，做出相对应的处理
 */
public class Nanny implements InvocationHandler {
    private Object targetObject;

    public Object theChild(Object targetObject){
        this.targetObject=targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){

        Object ret=null;
        try{
            Aop.before(proxy,method,args);
            ret = method.invoke(targetObject, args);
            Aop.after(proxy,method,args,ret);
        }catch (Exception e){
            Aop.catchException(method,e,ret);
        }finally {
            Aop.finallyTty(proxy,method,args,ret);
        }
        return ret;
    }
}
