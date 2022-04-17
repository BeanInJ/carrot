package com.carrot.aop;

import com.carrot.aop.annotation.AopAfter;
import com.carrot.aop.annotation.AopBefore;
import com.carrot.aop.annotation.AopException;
import com.carrot.aop.annotation.AopFinally;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注意，如果多个aop方法拦截到同一个目标方法，aop方法是无序的
 */
public class AopActuator {
    private final MethodBody methodBody;
    private final List<Class<?>> aopClasses;
    private final Map<Method,Object> beforeMethods = new HashMap<>();
    private final Map<Method,Object> afterMethods = new HashMap<>();
    private final Map<Method,Object> catchMethods = new HashMap<>();
    private final Map<Method,Object> finallyMethods = new HashMap<>();

    public AopActuator(Object proxy, Method method, Object[] args){
        // 初始化 “目标方法容器”
        this.methodBody = new MethodBody(proxy,method,args);
        // 初始化 “切面类容器”
        this.aopClasses = AopHelper.getMethodAop(method,proxy);
        // 解析切面类到相应的方法容器
        parseAopClasses();
    }

    public void before() {
        // 如果当前步骤不对，停止执行
        if(isNotThisStep(0)) return;

        runAopMethod(beforeMethods);

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

        runAopMethod(afterMethods);

        // 记录当前执行到哪一步了
        methodBody.setStep(3);
    }

    /**
     * 异常产生时执行
     */
    public void catchException(Exception e) {
        methodBody.setException(e);
        runAopMethod(catchMethods);
        // 记录当前执行到哪一步了
        methodBody.setStep(4);
    }

    /**
     * 一定会执行
     */
    public void finallyTty() {
        runAopMethod(finallyMethods);
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

    /**
     * 解析切面类容器
     */
    private void parseAopClasses(){
        for (Class<?> aopClass : aopClasses) {
            Object object;
            try {
                object = aopClass.newInstance();
            }catch (ReflectiveOperationException e){
                e.printStackTrace();
                continue;
            }

            for (Method method : aopClass.getMethods()) {
                // 判断是否是切面方法
                if(method.isAnnotationPresent(AopBefore.class)){
                    beforeMethods.put(method,object);
                }else if(method.isAnnotationPresent(AopAfter.class)){
                    afterMethods.put(method,object);
                }else if(method.isAnnotationPresent(AopException.class)){
                    catchMethods.put(method,object);
                }else if(method.isAnnotationPresent(AopFinally.class)){
                    finallyMethods.put(method,object);
                }
            }
        }
    }


}
