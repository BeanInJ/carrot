package com.vegetables.core;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;

/**
 * 启动入口
 */
public class App {
    private final static ConfigCenter configCenter = ConfigCenter.load();

    private Queue<SocketChannel> channelQueue;

    public static void start(){
        App app = new App();
        app.hotPan();
        app.oiling();

    }

    // 热锅:启动http服务端
    private void hotPan(){
        int port = configCenter.getAppPort();
        this.channelQueue = ChannelSwitch.open(port);
    }

    // 上油:解析前拦截
    private void oiling() {
        // noinspection InfiniteLoopStatement
        while (true) {
            SocketChannel socketChannel = channelQueue.poll();
            while (socketChannel != null) {
                RequestThread requestThread = new RequestThread(socketChannel);
                requestThread.start();
                socketChannel = channelQueue.poll();
            }
        }

    }

    // 炝锅:解析http请求
    public static void friedPan(Selector selector){

    }

    // 加辣椒:解析后拦截
    public static void addPepper() {
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
        start();
    }


}
