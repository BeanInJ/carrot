package com.vegetables.core;

import com.vegetables.entity.HttpGetter;
import com.vegetables.entity.HttpSetter;
import com.vegetables.system.notch.BeforeEnterFunction;
import com.vegetables.system.notch.BeforeReturnFunction;
import com.vegetables.util.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void request(SocketChannel socketChannel) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 接收到的内容
        socketChannel.read(buffer);
        if(buffer.position() == 0) return;
        String string = BufferUtils.getString(buffer);
        HttpGetter request = new HttpGetter(string);
        HttpSetter response = null;

        // 请求处理前拦截
        List<Class<?>> beforeEnters = InnerScanner.getBeforeEnters();
        for (Class<?> beforeEnter:beforeEnters){
            response = beforeEnter(request,beforeEnter);
        }

        if(response == null){
            response = new HttpSetter();
        }else {
            if(response.isReturnNow()){
                response(socketChannel,response);
                return;
            }else if(response.isGoToController()){

                // 拦截过后，跳转到控制器
                try {
                    Object o = InnerUrlMethod.HttpToController(request, response);
                    boolean isEmpty = o == null || o.toString().length() == 0;
                    if(!isEmpty){
                        response = new HttpSetter();
                        response.setData(o);
                    }
                } catch (Exception e) {
                    // 异常拦截可在此处写
                    e.printStackTrace();
                    response.setData(e);
                }
            }
        }

        // controller返回前拦截
        List<Class<?>> beforeReturns = InnerScanner.getBeforeReturns();
        for (Class<?> beforeReturn:beforeReturns){
            beforeReturn(request,response,beforeReturn);
        }

        response(socketChannel,response);
    }

    private void response(SocketChannel socketChannel,HttpSetter response) throws IOException {
        socketChannel.write(BufferUtils.getByteBuffer(response.toString()));
        socketChannel.close();
    }

    /**
     * 处理请求前拦截
     */
    private HttpSetter beforeEnter(HttpGetter request,Class<?> clazz){
        try {
            BeforeEnterFunction beforeEnterFunction =(BeforeEnterFunction) clazz.newInstance();
            HttpSetter httpSetter = beforeEnterFunction.beforeEnterAndReturn(request);

            if(httpSetter == null){
                beforeEnterFunction.beforeEnter(request);
                return null;
            }else {
                return httpSetter;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理请求后拦截
     */
    private void beforeReturn(HttpGetter request,HttpSetter response,Class<?> clazz){
        try {
            BeforeReturnFunction beforeEnterFunction =(BeforeReturnFunction) clazz.newInstance();
            beforeEnterFunction.beforeReturn(request,response);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
