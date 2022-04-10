package com.vegetables.core;

import com.vegetables.entity.HttpGetter;
import com.vegetables.entity.HttpSetter;
import com.vegetables.system.notch.BeforeEnterFunction;
import com.vegetables.system.notch.BeforeReturnFunction;
import com.vegetables.util.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;

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

        // 接收到的内容
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        if(buffer.position() == 0) return;
        String string = BufferUtils.getString(buffer);
        HttpGetter request = new HttpGetter(string);
        HttpSetter response = null;

        // 请求前拦截器加载
        Set<Class<?>> beforeEnters = InnerScanner.getBeforeEnters();
        for (Class<?> beforeEnter:beforeEnters){
            response = beforeEnter(request,beforeEnter);
        }

        // 拦截后如果没有返回响应，则new一个响应体，并进行请求处理
        if(response == null){
            response = new HttpSetter();
        }else {
            // 如果拦截器要求立即返回给前端，则直接返回
            if(response.isReturnNow()){
                response(socketChannel,response);
                return;
            }
        }

        // 如果拦截器不跳过控制器，则进入控制器
        if(response.isGoToController()){
            // 跳转到控制器（跳转到控制器前，HttpSetter是已经初始化了的，但是控制器有权重新初始化）
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

        // controller返回前拦截
        Set<Class<?>> beforeReturns = InnerScanner.getBeforeReturns();
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
