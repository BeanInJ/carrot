package com.carrot.core.step;

import com.carrot.system.dict.CONFIG;
import com.carrot.system.dict.MSG;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

public class AppSwitch implements Runnable {
    private static final Logger log = Logger.getGlobal();

    // 端口
    private final int port;

    // channel队列
    private static ArrayBlockingQueue<SocketChannel> channelQueue = null;

    {
        int size = (int) ConfigCenter.get(CONFIG.APP_CHANNEL_SIZE);
        channelQueue = new ArrayBlockingQueue<>(size);
    }

    // 构造函数,传入端口
    public AppSwitch(int port) {
        this.port = port;
    }

    // 获取channel队列
    protected static ArrayBlockingQueue<SocketChannel> get() {
        return channelQueue;
    }

    // 启动
    public static void open() {
        int port = ConfigCenter.getAppPort();
        AppSwitch channelSwitch = new AppSwitch(port);
        Thread thread = new Thread(channelSwitch);
        thread.start();
    }

    @Override
    public void run() {

        ServerSocketChannel channel;
        Selector selector;

        // 准备选择器、ServerSocketChannel通道
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();

            channel.socket().bind(new InetSocketAddress(this.port));
            channel.configureBlocking(false);

            channel.register(selector, SelectionKey.OP_ACCEPT);
            log.info( MSG.OPEN_SERVER + this.port + ", 尝试访问 http://localhost:"+this.port);
        }catch (BindException e){
            e.printStackTrace();
            if("Address already in use: bind".equals(e.getMessage())){
                log.warning("端口"+this.port+" 已被占用，建议在config.yml中修改端口号");
            }
            log.warning("程序异常退出！");
            System.exit(0);
            return;
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }

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
