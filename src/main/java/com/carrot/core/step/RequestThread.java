package com.carrot.core.step;

import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;
import com.carrot.system.BaseServer.pool.FilterPool;
import com.carrot.system.exception.CarrotException;
import com.carrot.system.exception.ReturnNow;
import com.carrot.system.util.BufferUtils;
import com.carrot.system.util.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * 请求解析及响应
 */
public class RequestThread implements Runnable {
    private static final Logger log = Logger.getGlobal();

    private final SocketChannel socketChannel;

    public RequestThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            request(this.socketChannel);
        }catch (ReturnNow ignore){
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void request(SocketChannel socketChannel) throws Exception {

        // 接收到的内容
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        nullThrow(buffer);

        // http内容初始化失败，直接返回
        BaseRequest request = new BaseRequest();
        try {
            request.reload(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 前置拦截器
        BaseResponse response = FilterPool.beforeController(request);

        // 拦截后如果没有返回响应，则new一个响应体，并进行请求处理
        if(response == null){
            response = new BaseResponse();
        }else {
            // 如果拦截器要求立即返回给前端，则直接返回
            if(response.isReturnNow()){
                response(socketChannel,response);
                return;
            }
        }

        // 判断拦截器是否要求跳过控制器
        if(response.isGoToController()){
            // 跳转到控制器（跳转到控制器前，HttpSetter是已经初始化了的，但是控制器有权重新初始化）
            try {
                Object returnValue = UrlMapper.httpToController(request, response);
                boolean isEmpty = returnValue == null || returnValue.toString().length() == 0;
                if(!isEmpty){
                    response.setBody(StringUtils.toStringOrJson(returnValue));
                }
            } catch (Exception e) {
                // 异常拦截可在此处写
                e.printStackTrace();
                response.setBody(e.toString());
            }
        }

        // 后置拦截器
        FilterPool.afterController(request,response);

        response(socketChannel,response);
    }

    private void response(SocketChannel socketChannel,BaseResponse response) throws IOException {
        socketChannel.write(BufferUtils.getByteBuffer(response.toString()));
    }

    private void nullThrow(ByteBuffer buffer){
        if(buffer.position() == 0) throw new ReturnNow();
    }

}
