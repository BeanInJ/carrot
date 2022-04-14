package com.vegetables.core;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * 启动入口
 */
public class App {
    private static final Logger log = Logger.getGlobal();

    private ArrayBlockingQueue<SocketChannel> channelQueue;

    public static void start(Class<?> main){
        App app = new App();
        app.load(main);
        app.open();
        app.start();

    }

    // 1、加载系统资源
    private void load(Class<?> main){
        AppLoader.load(main);
        log.fine("系统资源加载完成");
    }

    // 2、开启服务器
    private void open(){
        int port = InnerConfig.getAppPort();
        this.channelQueue = AppSwitch.open(port);
        log.fine("Socket已启动");
    }

    // 3、开启线程池处理请求
    private void start(){
        log.fine("线程池已启动");
        AppCpu.start(this.channelQueue);
    }

    public static void main(String[] args){
        start(App.class);
    }

}
