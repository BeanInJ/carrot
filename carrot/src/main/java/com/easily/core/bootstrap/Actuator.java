package com.easily.core.bootstrap;

import java.util.ArrayDeque;

/**
 * 执行器
 */
public class Actuator {
    /**
     * 是否已经开始（防止重载）
     */
    private boolean isUnderway = false;

    /**
     * 需要执行的队列
     */
    private final ArrayDeque<Element> steps = new ArrayDeque<>();

    /**
     * 获取进行状态
     */
    public boolean isUnderway() {
        return isUnderway;
    }

    /**
     * 正在进行中
     */
    public void underway() {
        isUnderway = true;
    }

    /**
     * 添加到最前面
     */
    public void addFirst(Element step) {
        steps.addFirst(step);
    }

    /**
     * 添加到最后面
     */
    public void add(Element step) {
        steps.add(step);
    }

    /**
     * 开始时运行
     */
    public void start() throws IllegalAccessException, InstantiationException {
        if (isUnderway()) return;
        underway();

        Object o = null;
        while (!steps.isEmpty()) {
            o = steps.poll().execute(o);
        }
    }
}
