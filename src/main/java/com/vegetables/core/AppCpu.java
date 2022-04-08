package com.vegetables.core;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用可缓存线程池，处理每个进来的请求
 */
public class AppCpu {
    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static void start(ArrayBlockingQueue<SocketChannel> channelQueue) {
        // noinspection InfiniteLoopStatement
        while (true) {
            try {
                SocketChannel socketChannel = channelQueue.take();
                service.execute(new RequestThread(socketChannel));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        service.shutdown();
    }

    public void join() throws InterruptedException {
        boolean b = service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

}
