package com.carrot.core.bootstrap;

import java.util.ArrayDeque;

public class Actuator {
    /**
     * 是否已经开始（防止重载）
     */
    private static boolean isUnderway = false;

    /**
     * 需要执行的队列
     */
    private static final ArrayDeque<Step> steps = new ArrayDeque<>();

    /**
     * 获取进行状态
     */
    public static boolean isUnderway() {
        return isUnderway;
    }

    /**
     * 正在进行中
     */
    public static void underway() {
        isUnderway = true;
    }

    /**
     * 添加到最前面
     */
    public static void addFirst(Step step) {
        steps.addFirst(step);
    }

    /**
     * 添加到最后面
     */
    public static void add(Step step) {
        steps.add(step);
    }

    /**
     * 开始时运行
     */
    public static void start() {
        if (isUnderway()) return;
        underway();

        while (!steps.isEmpty()) {
            steps.poll().execute();
        }
    }
}
