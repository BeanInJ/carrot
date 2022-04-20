package com.carrot.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * class工厂的职责 (工厂内包含了多个”池“):
 * 识别"资源类"和"类池"、注册"类池"、把"资源类"分发到各个"类池"
 *
 * 类池的职责 (一个池内包含了多个”资源类“，一个池对应一个”执行器“)：
 * 确定"资源类"是否属于该"池"、清洗无效"资源类"、存储"资源类"原始信息（资源类、注解、执行器）、将"资源类"分发到"执行器"
 *
 * 执行器的职责：
 * 解析"资源类"功能、执行"资源类"功能
 *
 */
public abstract class ClassPool implements Pool {
    protected final List<Class<?>> classes = new ArrayList<>();

    /**
     * 向池添加数据
     */
    @Override
    public void add(Class<?> clazz) {
        this.classes.add(clazz);
    }

    /**
     * 将数据分发给组件
     */
    @Override
    public void initActuator(){
        if(getActuator() == null) return;
        PoolActuator poolActuator = getActuator();
        for (Class<?> aClass : this.classes) {
            poolActuator.parse(aClass);
        }
    }
}
