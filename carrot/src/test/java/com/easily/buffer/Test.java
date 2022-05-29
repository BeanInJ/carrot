package com.easily.buffer;

public class Test {
    public static void main(String[] args) {
        ResizableArrayBuffer arrayBuffer = new ResizableArrayBuffer(
                4*1024,1024,
                128*1024,32,
                1024*1024,4);

        ResizableArray resizableArray = arrayBuffer.getArray();
//        int i = resizableArray.writeToMessage();

    }
}
