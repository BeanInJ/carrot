package com.carrot.system.java.com.carrot.core.impl;

import com.carrot.core.LoadFlow;
import com.carrot.core.Step;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Loader extends ArrayDeque<Step> implements LoadFlow {
    private static Class<?> mainClass;

    @Override
    public void execute() {
        List<Step> steps = new ArrayList<>(this);
        for (Step step : steps) {
            step.execute();
        }
    }

    /**
     * 重载
     */
    @Override
    public void reload() {
        execute();
    }

    @Override
    public void addMain(Class<?> mainClass) {
        Loader.mainClass = mainClass;
    }

    /**
     * execute() 执行完之后添加
     */
    @Override
    public void afterAdd(Step step) {
        boolean suc = super.add(step);
        if (suc) {
            step.execute();
        }
    }
}
