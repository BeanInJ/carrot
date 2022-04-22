package com.carrot.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 因为要先目标检查是否有aop，所以AopBody先创建
 * 在创建AopInvokeFlow的同时初始化MethodBody
 */
public class AopInvokeFlow {
    private final AopBody aopBody;
    private final MethodBody methodBody;

    public AopInvokeFlow(Object targetClass, Method targetMethod, Object[] args, AopBody aopBody) {
        this.methodBody = new MethodBody(targetClass, targetMethod, args);
        this.aopBody = aopBody;
    }

    public void before() {
        // 如果当前步骤不对，停止执行
        if(isNotThisStep(0)) return;

        runAopMethod(aopBody.getBeforeMethods());

        // 记录当前执行到哪一步了
        methodBody.setStep(1);
    }

    /**
     * 执行方法
     */
    public void invoke() throws Exception {
        if(isNotThisStep(1)) return;

        Object ret = methodBody.getMethod().invoke(methodBody.getObject(), methodBody.getArgs());
        methodBody.setReturnValue(ret);

        // 记录当前执行到哪一步了
        methodBody.setStep(2);
    }

    /**
     * 目标方法执行后执行
     */
    public void after() {
        // 如果当前步骤不对，停止执行
        if(isNotThisStep(2)) return;

        runAopMethod(aopBody.getAfterMethods());

        // 记录当前执行到哪一步了
        methodBody.setStep(3);
    }

    /**
     * 异常产生时执行
     */
    public void catchException(Exception e) {
        methodBody.setException(e);
        runAopMethod(aopBody.getCatchMethods());
        // 记录当前执行到哪一步了
        methodBody.setStep(4);
    }

    /**
     * 一定会执行
     */
    public void finallyTty() {
        runAopMethod(aopBody.getFinallyMethods());
        // 记录当前执行到哪一步了
        methodBody.setStep(5);
    }

    /**
     * 需要手动指定在哪一个步骤或多个步骤中执行
     */
    public void around() {
        // 待实现
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
     * 确定是否应该执行当前步骤
     */
    private boolean isNotThisStep(int thisStep){
        int step = methodBody.getStep();
        return !(step == thisStep || step == thisStep + 1);
    }

    /**
     * 执行切面方法
     */
    private void runAopMethod(Map<Method,Object> map){
        for (Map.Entry<Method, Object> entry : map.entrySet()) {
            Method method = entry.getKey();
            Object object = entry.getValue();
            try {
                // 判断method有哪些参数
                List<Object> list = new ArrayList<>();
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
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
                    }
                }
                // list转数组
                Object[] args = list.toArray();
                method.invoke(object, args);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
