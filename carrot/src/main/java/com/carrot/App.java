package com.carrot;


import com.carrot.core.bootstrap.Loader;
import com.carrot.core.bootstrap.Stater;
import com.carrot.core.bootstrap.Step;
import com.carrot.core.step.*;
import com.carrot.system.log.LogConfig;
import com.carrot.system.util.MethodUtils;

import java.io.*;

public class App {
    private static boolean isReplaceOrder = false;

    /**
     * Carrot启动入口
     *
     * <p>
     * <b> Loader 是系统核心加载器,Stater 是系统核心启动器。 </b> <br>
     *
     * 配置、日志、以及类工厂的初始化等会在加载器(Loader)中进行。
     * 类池的分发、socket的开放、线程池的开启等会在启动器(Stater)中进行。
     *
     * Loader、Stater是不可重载的，只能在系统启动时执行一次。
     * 这么设计是为了系统安全考虑，防止系统的核心程序被重载。
     * </p>
     *
     * @param mainClass main方法所在的类
     */
    public static void start(Class<?> mainClass) {

        // 优先执行beforeStart开头的方法方法
        MethodUtils.invokeMainMethod(mainClass);

        // 排序
        order();
        // 加载
        Loader.start(mainClass);
        // 启动
        Stater.start();
    }

    /**
     * 系统加载流程、启动流程默认执行顺序
     */
    private static void defaultOrder(){
        Loader.add(ConfigCenter::load);
        Loader.add(LogConfig::load);
        Loader.add(ClassFactory::load);
        Loader.add(Scanner::load);

        Stater.add(Scanner::start);
        Stater.add(ClassFactory::start);
        Stater.add(UrlMapper::start);
        Stater.add(AppSwitch::open);
        Stater.add(AppCpu::start);

    }

    /**
     * 排序
     */
    private static void order(){
        if(!isReplaceOrder) defaultOrder();
    }

    /**
     * 替换系统默认步骤
     */
    public static void reOrder(Step step) {
        isReplaceOrder = true;
        step.execute();
    }

    public static void main(String[] args) throws IOException {
        App.start(App.class);
    }
}
