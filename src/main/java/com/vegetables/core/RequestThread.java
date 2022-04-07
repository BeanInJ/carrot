package com.vegetables.core;

import com.vegetables.entity.RequestContent;
import com.vegetables.entity.ResponseContent;
import com.vegetables.util.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 请求解析及响应
 */
public class RequestThread extends Thread {
    private final SocketChannel socketChannel;

    public RequestThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void run() {
        try {
            request(this.socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void request(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 接收到的内容
        socketChannel.read(buffer);
        String string = BufferUtils.getString(buffer);
        System.out.println(string);
        System.out.println("------------------");
        RequestContent httpContent = RequestContent.init(string);

        // 返回内容
        response(socketChannel);
    }

    private void response(SocketChannel socketChannel) throws IOException {
        socketChannel.write(BufferUtils.getByteBuffer(new ResponseContent().toString()));
        socketChannel.close();
    }
}
