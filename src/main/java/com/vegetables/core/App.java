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
        app.hotPan(main);
        app.oiling();
        app.friedPan();

    }

    // 1、热锅:加载系统资源
    private void hotPan(Class<?> main){
        AppLoader.load(main);
        log.info("系统资源加载完成");
    }

    // 2、上油:开启服务器
    private void oiling(){
        int port = InnerConfig.getAppPort();
        this.channelQueue = AppSwitch.open(port);
        log.info("Socket已启动");
    }

    // 3、炝锅:开启线程池处理请求
    private void friedPan(){
        log.info("线程池已启动");
        AppCpu.start(this.channelQueue);

    }

    // 加辣椒:解析后拦截
    private void addPepper() {
        System.out.println("加辣椒");
    }

    // 炒菜:封装返回结果
    private static void stirFry() {
        System.out.println("炒菜");
    }

    // 加菜:返回结果前拦截
    public static void addVegetable() {
        System.out.println("加菜");
    }

    // 出锅:渲染结果
    private static void boil() {
        System.out.println("出锅");
    }

    // 上菜:返回结果
    private static void serving() {
        System.out.println("上菜");
    }

    public static void main(String[] args){
        start(App.class);
    }

}
