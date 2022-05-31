package com.easily.core;

import com.easily.core.http.HttpReader;
import com.easily.factory.Pools;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 数据交换
 */
public class DataSwap {
    public ByteBuffer request = null;
    public ByteBuffer Response = null;
    public BlockingDeque<ByteBuffer> moreRequest = new LinkedBlockingDeque<>();
    public SocketChannel socketChannel;

    public HttpReader httpReader;

    /**
     * 输入
     */
    public void in(ByteBuffer request){
        if(this.request == null){
            this.request = request;
        }else {
            this.moreRequest.add(request);
        }
    }

    /**
     * 输出，并释放资源
     */
    public ByteBuffer out(){
        this.request = null;
        moreRequest = null;
        return this.Response;
    }
}
