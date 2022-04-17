package com.carrot.core.step;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppCpu {
    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static void start() {
        ArrayBlockingQueue<SocketChannel> channelQueue = AppSwitch.get();
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
