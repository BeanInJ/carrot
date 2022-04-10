package com.vegetables.system.aop;

import java.lang.reflect.Method;

/**
 * 目标方法执行前执行
 */
public class BeforeMethodRun {
    /**
     * 是否继续执行目标方法
     */
    private boolean isContinue = true;

    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public void doing(Object proxy, Method method, Object[] args){
        doing(method, args);
    }

    public void doing(Method method, Object[] args){
        doing(args);
    }

    public void doing(Object[] args){
        doing();
    }

    public void doing(){

    }
}
