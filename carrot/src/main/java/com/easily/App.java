package com.easily;

import com.easily.core.bootstrap.Enter;

import java.io.IOException;

public class App {

    /**
     * Carrot启动入口
     * @param mainClass main方法所在的类
     */
    public static void start(Class<?> mainClass) {
        start(mainClass,null);
    }

    public static void start(Class<?> mainClass,String[] args) {
        try {
            new Enter().start(mainClass,args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        App.start(App.class,args);
    }
}
