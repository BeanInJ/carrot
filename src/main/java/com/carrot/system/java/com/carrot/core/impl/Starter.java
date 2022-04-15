package com.carrot.system.java.com.carrot.core.impl;

import com.carrot.core.StartFlow;
import com.carrot.core.Step;

import java.util.*;

/**
 * Starter继承自一个双向队列
 */
public class Starter extends ArrayDeque<Step>  implements StartFlow {

    /**
     * 循环执行
     */
    @Override
    public void execute() {
        while(!this.isEmpty()) {
            this.poll().execute();
        }
    }

    /**
     * 停止执行
     */
    @Override
    public void stop() {
        this.clear();
        Thread.currentThread().interrupt();
    }
}
