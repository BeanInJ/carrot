package com.easily.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class工厂的职责 (工厂内包含了多个”池“):
 * 识别"资源类"和"类池"、注册"类池"、把"资源类"分发到各个"类池"
 *
 * 类池的职责 (一个池内包含了多个”资源类“，一个池对应一个”执行器“)：
 * 确定"资源类"是否属于该"池"、清洗无效"资源类"、存储"资源类"原始信息（资源类、注解、执行器）、将"资源类"解析到池容器等待使用
 *
 * 池容器的职责：
 * 存储解析得到的产品、根据查询条件返回具体产品
 *
 */
public abstract class ClassPool implements Pool {
    // 原料
    protected final List<Class<?>> classes = new ArrayList<>();

    @Override
    public void add(Class<?> clazz) {
        this.classes.add(clazz);
    }

    @Override
    public void clear(){
        classes.clear();
    }
}
