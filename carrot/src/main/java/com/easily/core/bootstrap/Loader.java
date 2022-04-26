package com.easily.core.bootstrap;

/**
 * 加载器
 *
 * 总是在Starter启动器之前执行
 *
 *
 * Loader和starter的区别：
 * Loader 加载的内容总是一次性的
 * starter 启动的总是需要不断获取或者不断更新内容的
 */
public class Loader extends Actuator{
    private static Class<?> mainClass;

    public static void start(Class<?> mainClass){
        Loader.mainClass = mainClass;
        Loader.start();
    }

    public static Class<?> getMainClass(){
        return mainClass;
    }
}
