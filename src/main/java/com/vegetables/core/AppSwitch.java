package com.vegetables.core;

import com.vegetables.system.dict.Msg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * 打开ServerSocketChannel通道
 */
public class AppSwitch implements Runnable {
    private static final Logger log = Logger.getGlobal();

    // 端口
    private final int port;
    // channel队列保存
    private final ArrayBlockingQueue<SocketChannel> channelQueue = new ArrayBlockingQueue<>(1024);

    public AppSwitch(int port) {
        this.port = port;
    }

    public ArrayBlockingQueue<SocketChannel> get() {
        return channelQueue;
    }

    // 启动
    public static ArrayBlockingQueue<SocketChannel> open(int port) {
        AppSwitch channelSwitch = new AppSwitch(port);
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

            log.info( Msg.OPEN_SERVER + this.port + ", 尝试访问 http://localhost:"+this.port);
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