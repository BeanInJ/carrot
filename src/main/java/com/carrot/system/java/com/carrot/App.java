package com.carrot.system.java.com.carrot;

import com.carrot.core.Bootstrap;
import com.carrot.core.Flow;

import java.util.List;

public class App {
    public static void main(String[] args) {

    }

    /**
     * 按默认内核启动
     */
    public static void start(Class<?> main) {
        new Thread(new Bootstrap(main, null)).start();
    }

    /**
     * 按传入内核启动
     */
    public static void start(Class<?> main, List<Flow> flows) {
        new Thread(new Bootstrap(main, flows)).start();
    }
}
