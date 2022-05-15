package com.easily.core;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppCpu {
    /**
     * 线程池
     */
    private final ExecutorService service = Executors.newCachedThreadPool();

    /**
     * 开启线程池，循环处理请求
     */
    public void start(ArrayBlockingQueue<SocketChannel> channelQueue,Container container) {
        // noinspection InfiniteLoopStatement
        while (true) {
            try {
                SocketChannel socketChannel = channelQueue.take();
                service.execute(new RequestActuator(socketChannel,container));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止线程池
     */
    public void stop() {
        service.shutdown();
    }

    /**
     *  service join
     */
    public void join() throws InterruptedException {
        boolean b = service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}
