package com.carrot.core.bootstrap;

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
