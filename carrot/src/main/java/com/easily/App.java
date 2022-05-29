package com.easily;

import com.easily.core.bootstrap.Enter;

import java.io.IOException;

public class App {

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
        try {
            new Enter().start(mainClass);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        App.start(App.class);
    }
}
