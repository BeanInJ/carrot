package com.vegetables.util;

import java.nio.ByteBuffer;

public class BufferUtils {
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
    // String转ByteBuffer
    public static ByteBuffer getByteBuffer(String str) {
        byte[] bytes = str.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
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

}
