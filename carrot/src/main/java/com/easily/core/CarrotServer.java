package com.easily.core;

import com.easily.factory.Pools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

public class CarrotServer {
    private static final Logger log = Logger.getGlobal();
    private volatile static Selector selector;
    public static ExecutorService service;
    private final BlockingDeque<SocketChannel> socketChannels = new LinkedBlockingDeque<>();

    private Pools pools;

    /**
     * 单例创建 Selector、线程池
     */
    public static void openSelector() throws IOException {
        if (selector == null && service == null) {
            synchronized (CarrotServer.class) {
                if (selector == null) {
                    selector = Selector.open();
                }
                if (service == null) {
                    service = Executors.newCachedThreadPool();
                }
            }
        }
    }

    /**
     * 开放一个端口
     */
    public SelectionKey openPort(int port) throws IOException {
        openSelector();

        // 开放一个端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        log.info("开放端口: http://localhost:"+port);
        // 注册为可接收事件
        return serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 循环获取SocketChannel
     */
    public void loopGet() {
        // noinspection InfiniteLoopStatement
        while (true) {
            try {
                // 当注册事件到达时，方法返回，否则该方法会一直阻塞
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        // 接收到新请求，往队列添加,ServerSocketChannel是非阻塞模式的
                        SocketChannel socketChannel = server.accept();
                        socketChannel.configureBlocking(false);
                        socketChannels.add(socketChannel);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 循环处理请求
     */
    public void loopDeal() {
        // noinspection InfiniteLoopStatement
        while (true) {
            try {
                SocketChannel take = socketChannels.take();
                service.execute(() -> {
                    try {
                        DataSwap dataSwap = new DataSwap();
                        dataSwap.socketChannel = take;
                        dataSwap.pools = pools;
                        // 读
                        int read = read(dataSwap);
                        if(read == -1){}
                        else {
                            // 读完，执行请求流程
                            new RequestActuator(dataSwap).flow();
                            if (dataSwap.Response != null && dataSwap.Response.hasRemaining()){
                                write(dataSwap);
                                long timed = dataSwap.Response.position()/1048576;
                                Thread.sleep(10L * (timed + 1));
                            }
                        }

                        take.close();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int read(DataSwap dataSwap) throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        int read = dataSwap.socketChannel.read(buffer);
        int total = read;
        while (read > 0) {
            Thread.sleep(1);
            read = dataSwap.socketChannel.read(buffer);
            total = total + read;
        }

        if(read == -1) return -1;
        dataSwap.in(buffer);

        if (buffer.remaining() == 0){
            // buffer满了，入新的buffer写
            total += read(dataSwap);
        }
        return total;
    }

    private void write(DataSwap dataSwap) throws IOException {
        if(dataSwap.Response != null) dataSwap.socketChannel.write(dataSwap.out());
    }

    public void addPools(Pools pools){
        this.pools = pools;
    }

//    public byte[] getResponse() {
//        return ("HTTP/1.1 200 OK\r\n" +
//                "Content-Length: 38\r\n" +
//                "Content-Type: text/html\r\n" +
//                "\r\n" +
//                "<html><body>Hello World!</body></html>").getBytes(StandardCharsets.UTF_8);
//    }
}
