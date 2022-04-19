package com.carrot.core.step;

import com.carrot.core.http.BaseRequest;
import com.carrot.core.http.BaseResponse;
import com.carrot.system.BaseServer.pool.FilterPool;
import com.carrot.system.util.BufferUtils;
import com.carrot.system.util.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class RequestActuator implements Runnable{
    private static final Logger log = Logger.getGlobal();
    private final SocketChannel socketChannel;
    private BaseRequest request;
    private BaseResponse response;

    public RequestActuator(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void run() {
        try {
            this.flow();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行请求处理流程
     */
    private void flow() throws IOException {
        try {
            boolean flow = this.getRequest() && this.passBeforeFilter() && this.passController() && this.passAfterFilter();
        }catch (Exception e){
            e.printStackTrace();
            this.ifException();
        }
        this.sendResponse();
    }

    /**
     * 获取请求体
     */
    private boolean getRequest() throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            this.socketChannel.read(buffer);
            if (buffer.position() == 0) {
                return false;
            } else {
                this.request = new BaseRequest(buffer);
                return true;
            }
    }

    /**
     * 通过前置过略器
     */
    private boolean passBeforeFilter() {
        this.response = FilterPool.beforeController(this.request);
        // 如果经过过略器链后，response还未初始化，则在此初始化
        if (this.response == null) {
            this.response = new BaseResponse();
        }
        return true;
    }

    /**
     * 通过控制器
     */
    private boolean passController() {
        if (this.response.isGoToController()) {
                Object returnValue = UrlMapper.httpToController(this.request, this.response);
                boolean isEmpty = returnValue == null || returnValue.toString().length() == 0;
                if (!isEmpty) {
                    this.response.setBody(StringUtils.toStringOrJson(returnValue));
                }
        }
        return true;
    }

    /**
     * 通过后置过滤器
     */
    private boolean passAfterFilter() {
        FilterPool.afterController(this.request, this.response);
        return true;
    }

    /**
     * 向前端发送数据
     */
    private void sendResponse() throws IOException {
        if(this.response != null)
            this.socketChannel.write(BufferUtils.getByteBuffer(this.response.toString()));
    }

    /**
     * 执行异常拦截器
     */
    private void ifException(){
    }
}
