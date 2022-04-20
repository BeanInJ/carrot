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

/**
 * getRequest、passBeforeFilter、passController、passAfterFilter、passAfterFilter
 * 返回 false 表示直接跳到sendResponse执行
 * 返回 true  表示继续执行
 */
public class RequestActuator implements Runnable {
    private static final Logger log = Logger.getGlobal();
    private final SocketChannel socketChannel;
    private BaseRequest request;
    private BaseResponse response;

    public RequestActuator(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void run() {
        try {
            // 执行请求流程
            this.flow();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭当前请求通道
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
            // 获取请求->前过滤->控制器->后过滤
            boolean flow = this.getRequest() && this.passBeforeFilter() && this.passController() && this.passAfterFilter();
        } catch (Exception e) {
            e.printStackTrace();
            // 异常拦截
            this.ifException();
        }
        // 返回到前端
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
        // 如果经过过略器后，response还未初始化，则在此初始化
        if (this.response == null) {
            this.response = new BaseResponse();
        }
        return !this.response.isReturnNow();
    }

    /**
     * 通过控制器
     */
    private boolean passController() {
        if (this.response.isGoToController()) {
            Object returnValue = UrlMapper.httpToController(this.request, this.response);
            if (returnValue instanceof BaseResponse) {
                this.response = (BaseResponse) returnValue;
            } else if (StringUtils.isNotBlankOrNull(returnValue)) {
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
        return !this.response.isReturnNow();
    }

    /**
     * 向前端发送数据
     */
    private void sendResponse() throws IOException {
        if (this.response != null)
            this.socketChannel.write(BufferUtils.getByteBuffer(this.response.toString()));
    }

    /**
     * 执行异常拦截器
     */
    private void ifException() {
    }
}
