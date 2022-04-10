package com.vegetables.system.aop.entity;

import com.vegetables.system.aop.active.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 保存已注册的AOP方法列表
 */
public class AopMethods {
    private List<Class<?>> classes;
    private List<AopLinkTarget> befores;
    private List<AopLinkTarget> arounds;
    private List<AopLinkTarget> afters;
    private List<AopLinkTarget> catchs;
    private List<AopLinkTarget> finallys;

    public AopMethods(){
        for(Class<?> aClass:classes){
            for (Method method : aClass.getMethods()) {
                if(method.isAnnotationPresent(AOPBefore.class)){

                }else if(method.isAnnotationPresent(AOPAfter.class)){

                }else if(method.isAnnotationPresent(AOPAround.class)){

                }else if(method.isAnnotationPresent(AOPCatch.class)){

                }else if(method.isAnnotationPresent(AOPFinally.class)){

                }
            }

        }
    }

    public List<AopLinkTarget> getBefores() {
        return befores;
    }

    public void setBefores(List<AopLinkTarget> befores) {
        this.befores = befores;
    }

    public List<AopLinkTarget> getArounds() {
        return arounds;
    }

    public void setArounds(List<AopLinkTarget> arounds) {
        this.arounds = arounds;
    }

    public List<AopLinkTarget> getAfters() {
        return afters;
    }

    public void setAfters(List<AopLinkTarget> afters) {
        this.afters = afters;
    }

    public List<AopLinkTarget> getCatchs() {
        return catchs;
    }

    public void setCatchs(List<AopLinkTarget> catchs) {
        this.catchs = catchs;
    }

    public List<AopLinkTarget> getFinallys() {
        return finallys;
    }

    public void setFinallys(List<AopLinkTarget> finallys) {
        this.finallys = finallys;
    }
}
