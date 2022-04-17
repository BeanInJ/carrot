package com.carrot.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 方法”保姆“
 *
 * 在方法执行 前、后、异常时、最终执行时，做出相对应的处理
 */
public class Nanny implements InvocationHandler {
    private Object proxyObject;

    // 不需要代理
    public Nanny(){}

    // 生成代理类
    public Nanny(Object targetObject){
        this.proxyObject = Proxy.newProxyInstance(
                targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(),
                this);
    }

    // 获取代理类
    public Object getProxy(){
        return proxyObject;
    }

    // 执行方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){

        // 方法最终返回值,在Aop.methodBody中进行初始化
        AopActuator aop = new AopActuator(proxy,method,args);

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

    /**
     * 跳到Finally，并返回
     */
    private boolean gotoFinallyAndReturn(MethodBody methodBody){
        // ”立即返回“ 优先级大于 “继续执行”
        return methodBody.isReturnNow() || !methodBody.isContinue();
    }
}
