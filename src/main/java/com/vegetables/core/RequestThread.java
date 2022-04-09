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
public class RequestThread implements Runnable {
    private final SocketChannel socketChannel;

    public RequestThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
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
        if(buffer.position() == 0) return;
        String string = BufferUtils.getString(buffer);
        RequestContent request = new RequestContent(string);

        // 检查拦截器中是否有拦截

        // 在url映射器中查找对应的方法
        // 如果没有找到，则返回404
        // 如果找到，则执行方法，并返回结果
        // 如果方法执行失败，则返回500
        // 如果方法执行成功，则返回20


        ResponseContent response = new ResponseContent();
        System.out.println(request);

        // 返回内容
        response(socketChannel);
    }

    private void response(SocketChannel socketChannel) throws IOException {
        socketChannel.write(BufferUtils.getByteBuffer(new ResponseContent().toString()));
        socketChannel.close();
    }
}
