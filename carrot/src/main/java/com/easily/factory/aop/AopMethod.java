package com.easily.factory.aop;

import com.easily.label.AopAfter;
import com.easily.label.AopBefore;
import com.easily.label.AopException;
import com.easily.label.AopFinally;
import com.easily.system.util.MethodUtils;

import java.lang.reflect.Method;

/**
 * AopMethod：经过aop切面功能封装后的方法体
 *
 * AopMethod可多层封装 （AopMethod的本质是一个单项链表，rootAopMethod为下一个节点）
 */
public class AopMethod {
    // 原始目标方法
    private final Method method;
    // 包裹了aop的目标方法
    private final AopMethod rootAopMethod;
    // 原始目标类
    private Object object;
    // 原始目标方法全名，一个目标方法不管包裹了多少层aop，它对应methodTag都是同一个
    private final String methodTag;

    // 记录aop包裹层数
    private int aopNum = 0;

    // aop切入方法所在的类
    private Object aopObject;
    // aop切入方法
    private Method beforeMethod;
    private Method afterMethod;
    private Method exceptionMethod;
    private Method finallyMethod;

    /**
     * 用原始方法构建aop方法
     */
    public AopMethod(Method method,Object object, Object aopObject){
        this.method = method;
        this.rootAopMethod = null;
        this.methodTag = MethodUtils.getFullMethodName(method);
        init(object,aopObject);
        this.aopNum = 1;
    }

    /**
     * 在aop方法的基础之上再次包裹aop方法
     */
    public AopMethod(AopMethod method,Object object, Object aopObject){
        this.rootAopMethod = method;
        this.methodTag = method.getMethodTag();
        this.method = null;
        init(object,aopObject);
        this.aopNum =  method.getAopNum() + 1;
    }

    /**
     * 调用aop方法
     *
     * 经过多层包裹的invoke执行顺序：
     * before3 -> before2 -> before1 -> method -> after1 exception1 finally1 -> after2 exception2 finally2 -> after3 exception3 finally3
     */
    public Object invoke(Object... args) throws Exception {
        AopMethodActuator aop = new AopMethodActuator(object,method,args);
        try{
            // 1  方法执行前
            aop.sectionInvoke(1,beforeMethod,aopObject);
            if(aop.gotoFinally()) return aop.result();

            // 2  方法执行
            if(rootAopMethod == null){
                // 如果包裹的是原始方法
                aop.targetInvoke(method,object,args);
            }else{
                // 如果包裹的是aop方法
                aop.targetInvoke(rootAopMethod,object,args);
            }
            if(aop.gotoFinally()) return aop.result();

            // 3  方法执行后
            aop.sectionInvoke(3,afterMethod,aopObject);
            if(aop.gotoFinally()) return aop.result();
        }catch (Exception e){
            // 4 方法异常时
            aop.exceptionInvoke(exceptionMethod, aopObject,e);
        }finally {
            // 5 方法最终必须执行
            aop.sectionInvoke(5, finallyMethod, aopObject);
        }
        return aop.result();
    }

    public String getMethodTag() {
        return methodTag;
    }
    public int getAopNum() {
        return this.aopNum;
    }
    public Object getAopObject(){
        return this.aopObject;
    }

    public Method getMethod() {
        return method;
    }

    public AopMethod getRootAopMethod() {
        return rootAopMethod;
    }

    /**
     * 初始化aop功能
     */
    private void init(Object object, Object aopObject){
        this.object = object;
        this.aopObject = aopObject;

        for (Method aopMethod : aopObject.getClass().getDeclaredMethods()) {
            if(aopMethod.isAnnotationPresent(AopBefore.class)){
                this.beforeMethod = aopMethod;
            }else if(aopMethod.isAnnotationPresent(AopAfter.class)){
                this.afterMethod = aopMethod;
            }else if(aopMethod.isAnnotationPresent(AopException.class)){
                this.exceptionMethod = aopMethod;
            }else if(aopMethod.isAnnotationPresent(AopFinally.class)) {
                this.finallyMethod = aopMethod;
            }
        }
    }

}
