package com.easily.core.http;

import com.easily.core.DataSwap;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

/**
 * 当http请求body中包含文件时，还未处理
 */
public class HttpReader {
    private ByteBuffer buffer;
    public BlockingDeque<ByteBuffer> moreByteBuffer;
    private String method;
    private String url;
    private String version;

    private String body;
    private final Map<String, String> headers = new HashMap<>();

    private Boolean isHttp;

    private int residueLength;

    public HttpReader(DataSwap dataSwap) {
        this.buffer = dataSwap.request;
        this.buffer.flip();
        this.moreByteBuffer = dataSwap.moreRequest;
    }

    public HttpReader(ByteBuffer buffer) {
        buffer.flip();
        this.buffer = buffer;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public String getVersion() {
        return this.version;
    }

//    public boolean initHttp() {
//        byte[] lineFlag = new byte[]{'\r', '\n'};
//        byte blankFlag = ' ';
//        byte[] libertyFlag = new byte[]{':', ' '};
//
//        this.method = this.nextString(blankFlag);
//        if("".equals(this.method) || this.method.length() > 10 ) return false;
//        this.url = this.nextString(blankFlag);
//        this.version = this.nextString(lineFlag);
//
//        if (this.version.startsWith("HTTP")) {
//
//            // 解析请求头
//            while(true) {
//                String key = this.nextString(libertyFlag);
//                if (key.length() == 0) {
//                    break;
//                }
//                String value = this.nextString(lineFlag);
//                this.headers.put(key, value);
//            }
//
//            // 解析请求body
//            StringBuilder bodyBuilder = new StringBuilder();
//            byte[] bufferRam = new byte[buffer.limit() - buffer.position()];
//            buffer.get(bufferRam);
//            bodyBuilder.append(new String(bufferRam));
//            buffer.clear();
//            for (ByteBuffer buffer1:this.moreByteBuffer){
//                buffer1.flip();
//                byte[] bufferMore = new byte[buffer1.limit()];
//                bodyBuilder.append(new String(bufferMore));
//            }
//            this.body = bodyBuilder.toString();
//
//            return true;
//        }
//        return false;
//    }

    private String nextString(byte endFlag) {
        StringBuilder builder = new StringBuilder();

        while(this.buffer.hasRemaining()) {
            byte b = this.buffer.get();
            if (b == endFlag) {
                return builder.toString();
            }
            builder.append((char)b);
        }

        return builder.toString();
    }

    private String nextString(byte[] endFlag) {
        StringBuilder builder = new StringBuilder();

        while(this.buffer.hasRemaining()) {
            byte b = this.buffer.get();
            if (b == '\r') {
                this.buffer.mark();
                if (this.buffer.get() == '\n' || this.buffer.get() == '\r' || this.buffer.get() == '\n') {
                    this.buffer.position(this.buffer.position());
                    return builder.toString();
                }
                this.buffer.reset();
            }

            if (b == endFlag[0]) {
                for(int i = 1; i < endFlag.length; ++i) {
                    byte b2 = this.buffer.get();
                    if (b2 != endFlag[i]) {
                        builder.append((char)b);
                        builder.append((char)b2);
                        break;
                    }
                }
                return builder.toString();
            }

            builder.append((char)b);
        }

        return builder.toString();
    }

    public boolean parseHttp() {
        if(isHttp == null) {
            byte[] lineFlag = new byte[]{'\r', '\n'};
            byte blankFlag = ' ';
            byte[] libertyFlag = new byte[]{':', ' '};

            this.method = this.nextString(blankFlag);
            if ("".equals(this.method) || this.method.length() > 10) {
                this.isHttp = false;
                return false;
            }
            this.url = this.nextString(blankFlag);
            this.version = this.nextString(lineFlag);

            if (this.version.startsWith("HTTP")) {

                // 解析请求头
                while (true) {
                    String key = this.nextString(libertyFlag);
                    if (key.length() == 0) {
                        break;
                    }
                    String value = this.nextString(lineFlag);
                    this.headers.put(key, value);
                }
                this.isHttp = true;
                return true;
            }
            this.isHttp = false;
            return false;
        }else {
            return isHttp;
        }
    }

    public boolean checkLength(){
        // 判断长度剩余长度是否和Content-Length相等
        this.residueLength = getResidueLength();
        String contentLengthString = this.headers.get("Content-Length");

        // 没有"Content-Length"返回true
        if(contentLengthString == null) return true;

        int contentLength = 0;
        try {
            contentLength = Integer.parseInt(contentLengthString);
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw new RuntimeException("文件长度不能超过一个int");
        }
        return contentLength == this.residueLength;
    }

    // 只能get一次
    public String getStringBody() {
        if (buffer == null) return null;

        StringBuilder bodyBuilder = new StringBuilder();
        byte[] bufferRam = new byte[buffer.limit()-buffer.position()];
        buffer.get(bufferRam);

        bodyBuilder.append(new String(bufferRam, StandardCharsets.UTF_8));
        buffer.clear();

        for (ByteBuffer buffer1:this.moreByteBuffer){
            buffer1.flip();
            byte[] bufferMore = new byte[buffer1.limit()];
            buffer1.get(bufferMore);
            bodyBuilder.append(new String(bufferMore));
        }

        buffer = null;
        this.moreByteBuffer = null;
        return bodyBuilder.toString();
    }

    // 获取请求体长度
    private int getResidueLength() {
        int len = this.buffer.limit() - this.buffer.position();
        if(moreByteBuffer != null){
            // 循环获取每个buffer的长度
            for (ByteBuffer byteBuffer : moreByteBuffer) {
                len += byteBuffer.position();
            }
        }
        return len;
    }

    public Boolean getHttp() {
        return isHttp;
    }
}
