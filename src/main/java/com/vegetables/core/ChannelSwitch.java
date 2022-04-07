package com.vegetables.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

public class ChannelSwitch implements Runnable {
    // 日志
    private static final Logger log = Logger.getLogger("ChannelSwitch");

    // 端口
    private final int port;
    // channel队列保存
    private final Queue<SocketChannel> channelQueue = new ArrayBlockingQueue<>(1024);

    public ChannelSwitch(int port) {
        this.port = port;
    }

    public Queue<SocketChannel> get() {
        return channelQueue;
    }

    // 启动
    public static Queue<SocketChannel> open(int port) {
        ChannelSwitch channelSwitch = new ChannelSwitch(port);
        Thread thread = new Thread(channelSwitch);
        thread.start();
        return channelSwitch.get();
    }

    @Override
    public void run() {

        ServerSocketChannel channel = null;
        Selector selector = null;

        // 准备选择器、ServerSocketChannel通道
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();

            channel.socket().bind(new InetSocketAddress(this.port));
            channel.configureBlocking(false);

            channel.register(selector, SelectionKey.OP_ACCEPT);

            log.info(" 已启动Http端口， " + this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert selector != null;
        assert channel != null;

        // 监听选择器中的可读事件
        // noinspection InfiniteLoopStatement
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (SelectionKey key1 : selectedKeys) {
                    if (key1.isAcceptable()) {
                        SocketChannel socketChannel = channel.accept();
                        channelQueue.offer(socketChannel);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}