package com.carrot.core.step;

import com.carrot.core.http.BaseHttp;
import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;
import com.carrot.system.BaseServer.pool.FilterPool;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * 请求处理器
 */
public class RequestActuator implements Runnable{
    private static final Logger log = Logger.getGlobal();

    private final SocketChannel socketChannel;

    public RequestActuator(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {

    }

    private void flow(){

        // 初始化请求内容
        BaseRequest request = initRequest();
        if(request == null) return;

        // 通过前置过滤器
        BaseResponse response = passBeforeFilter(request);
    }

    /**
     * 初始化请求内容
     */
    private BaseRequest initRequest(){
        try {

            // 接收为buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            this.socketChannel.read(buffer);

            // buffer为空返回
            if (buffer.position() == 0) return null;

            // http内容初始化
            return new BaseRequest(buffer);

        }catch (Exception e){
            // http内容初始化失败
            log.fine("http内容初始化失败");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过前置过滤器，返回的是响应体
     */
    private BaseResponse passBeforeFilter(BaseRequest request){
        // 经过拦截器
        BaseResponse response = FilterPool.beforeController(request);

        // 拦截后如果没有返回响应体，则new一个响应体
        if(response == null){
            response = new BaseResponse();
        }
        return response;
    }

    /**
     *
     */
}
