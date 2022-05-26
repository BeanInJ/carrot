package com.easily.system.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BufferUtils {
    /**
     * 从socketChannel读取数据到ByteBuffer
     * 返回数据大小，返回-1表示无数据
     */
    public static int socketChannelRead(SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
        int readNum = socketChannel.read(buffer);
        System.out.println("eeeeeeeeeeeeee:"+readNum);
        int total = 0;
        while (readNum > 0) {
            total += readNum;
            try {
                if (readNum > 200000) {
                    Thread.sleep(100 * (readNum / 200000));
                } else {
                    Thread.sleep(10);
                }
            } catch (InterruptedException ignore) {
            }
            readNum = socketChannel.read(buffer);
        }
        System.out.println("vvvvvvvvvv:"+total);
        return total;
    }

    /**
     * 从ByteBuffer中读取 byte[]
     */
    public static byte[] byteBufferGet(ByteBuffer buffer, int total) {
        buffer.flip();
        byte[] bytes = new byte[total];
        buffer.get(bytes, 0, total);
        buffer.clear();
        return bytes;
    }

    /**
     * 从ByteBuffer中读取String
     */
    public static String byteBufferGetString(ByteBuffer buffer, int total) {
        return new String(byteBufferGet(buffer, total), StandardCharsets.UTF_8);
    }

    /**
     * 从ByteBuffer中读取String，传入编码格式
     */
    public static String byteBufferGetString(ByteBuffer buffer, int total, Charset charset) {
        if (charset == null) charset = StandardCharsets.UTF_8;
        return new String(byteBufferGet(buffer, total), charset);
    }

    /**
     * 从ByteBuffer中读取String，传入编码格式
     */
    public static String byteBufferGetString(ByteBuffer buffer, int total, String charset) {
        Charset charset1 = charset == null ? StandardCharsets.UTF_8 : Charset.forName(charset);
        return new String(byteBufferGet(buffer, total), charset1);
    }

    /**
     * 将ByteBuffer写入 socketChannel
     */
    public static int socketChannelWrite(SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
        int write = socketChannel.write(buffer);
        int total = write;

        while (write > 0 && buffer.hasRemaining()) {
            write = socketChannel.write(buffer);
            total += write;
        }
        return total;
    }

    // ByteBuffer转String
    public static String getString(ByteBuffer buffer) {
        buffer.flip();
        char[] chars = new char[buffer.remaining()];

        while (buffer.hasRemaining()) {
            char c = (char) buffer.get();
            chars[buffer.position() - 1] = c;
        }
        buffer.clear();
        return new String(chars);
    }

    // ByteBuffer转StringBuffer
    public static StringBuffer getStringBuffer(ByteBuffer buffer) {
        buffer.flip();
        StringBuffer sb = new StringBuffer();

        while (buffer.hasRemaining()) {
            char c = (char) buffer.get();
            sb.append(c);
        }
        buffer.clear();
        return sb;
    }

    // StringBuffer转ByteBuffer
    public static ByteBuffer getByteBuffer(StringBuffer sb) {
        byte[] bytes = sb.toString().getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    // String转ByteBuffer
    public static ByteBuffer getByteBuffer(String str, String charset) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes(charset);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer getByteBuffer(String str) throws UnsupportedEncodingException {
        return getByteBuffer(str, StandardCharsets.UTF_8.name());
    }
}
