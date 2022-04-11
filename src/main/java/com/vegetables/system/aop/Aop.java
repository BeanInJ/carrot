package com.vegetables.system.aop;

import com.vegetables.system.aop.entity.MethodBody;

import java.lang.reflect.Method;

public class Aop {
    private final MethodBody methodBody;
    private Class<?> aopClass;

    public Aop(Object proxy, Method method, Object[] args){
        this.methodBody = new MethodBody(proxy,method,args);
    }

    public void before() {
        // 如果当前步骤不对，停止执行
        if(methodBody.getStep() != 0) return;

        // 匹配当前method，然后执行
//        for (AopLinkTarget before : new AopMethods().getBefores()) {
//            if(before.getTargetMethod().getMethod() == method){
//                for (MethodAndClass aopMethod : before.getAopMethods()) {
//                    // 执行要切入的方法，这里要检测切入方法所需要的参数
//                    // aopMethod.getMethod().invoke(aopMethod.getObject(),args);
//
//                    Method method1 = aopMethod.getMethod();
//                    // 循环参数类型
//                    for (Class<?> parameterType : method1.getParameterTypes()) {
//
//                    }
//
//                }
//            }
//        }



        System.out.println("before");

        // 记录当前执行到哪一步了
        methodBody.setStep(1);
    }

    public void after() {
        // 如果当前步骤不对，停止执行
        if(methodBody.getStep() != 2) return;

        System.out.println("after");

        // 记录当前执行到哪一步了
        methodBody.setStep(3);
    }

    public void catchException(Exception e) {
        methodBody.setException(e);
        // 异常可能在任何步骤出现

        System.out.println("afterReturning");

        // 记录当前执行到哪一步了
        methodBody.setStep(4);
    }

    public void finallyTty() {
        System.out.println("afterThrowing");

        // 记录当前执行到哪一步了
        methodBody.setStep(5);
    }

    /**
     * 需要手动指定在哪一个步骤或多个步骤中执行
     */
    public void around() {
        System.out.println("around");
    }

    public void invoke() throws Exception {
        if(methodBody.getStep() != 1) return;

        Object ret = methodBody.getMethod().invoke(methodBody.getProxy(), methodBody.getArgs());
        methodBody.setReturnValue(ret);

        // 记录当前执行到哪一步了
        methodBody.setStep(2);
    }

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
}
