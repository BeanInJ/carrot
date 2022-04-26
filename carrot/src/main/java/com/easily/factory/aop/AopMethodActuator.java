package com.easily.factory.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * AopMethod 内置执行器，AopMethod类型每次调用
 */
class AopMethodActuator {
    private final MethodBody methodBody;

    protected AopMethodActuator(Object obj,Method targetMethod,Object[] args){
        methodBody = new MethodBody(obj,targetMethod,args);
    }

    public void setReturnValue(Object value){
        this.methodBody.setReturnValue(value);
    }

    /**
     * 是否直接跳到finally中执行
     */
    public boolean gotoFinally(){
        // ”立即返回“ 优先级大于 “继续执行”
        return methodBody.isReturnNow() || !methodBody.isContinue();
    }

    /**
     * 返回方法执行后的结果
     */
    public Object result(){
        return this.methodBody.getReturnValue();
    }

    /**
     * 执行目标方法
     */
    public void targetInvoke(Method method,Object obj, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object returnValue = method.invoke(obj,args);
        this.setReturnValue(returnValue);
        methodBody.setStep(2);
    }

    /**
     * 执行目标方法
     */
    public void targetInvoke(AopMethod method,Object obj, Object[] args) throws Exception {
        Object returnValue = method.invoke(args);
        this.setReturnValue(returnValue);
        methodBody.setStep(2);
    }

    /**
     * 执行异常切面
     */
    public void exceptionInvoke(Method section,Object aopObject,Exception e) throws Exception {
        if (section == null) {
            throw e;
        }else {
            methodBody.setException(e);
            sectionInvoke(4, section, aopObject);
        }
    }

    /**
     * 执行切面
     */
    public void sectionInvoke(int step,Method section,Object aopObject) throws InvocationTargetException, IllegalAccessException {
        if (section == null) return;

        // 判断method有哪些参数
        List<Object> list = new ArrayList<>();

        for (Class<?> parameterType : section.getParameterTypes()) {
            if(parameterType == Object.class){
                // 传递目标方法所在的类
                list.add(methodBody.getObject());
            }else if(parameterType == Method.class){
                // 传递目标方法
                list.add(methodBody.getMethod());
            }else if(parameterType == Object[].class){
                // 传递目标方法的参数
                list.add(methodBody.getArgs());
            }else if(parameterType == MethodBody.class){
                // MethodBody中包含了目标方法的所有信息
                list.add(methodBody);
            }else {
                list.add(null);
            }
        }

        // list转数组
        Object[] args = list.toArray();
        section.invoke(aopObject, args);

        // 记录执行了哪些步骤的aop方法
        methodBody.setStep(step);
    }
}
