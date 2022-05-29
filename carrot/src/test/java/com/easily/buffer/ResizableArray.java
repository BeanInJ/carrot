package com.easily.buffer;

import java.nio.ByteBuffer;

/**
 * 解析来源：<a href="https://zhuanlan.zhihu.com/p/109263240">https://zhuanlan.zhihu.com/p/109263240</a>
 *
 * 可变长数组设计：
 * 包含两个组件：ResizableArray、ResizableArrayBuffer
 * ResizableArrayBuffer 包含三个数组：小数组，中数组，大数组。小数据不会占用中、大数组的内存，大数据也不会占用小数组内存。
 *
 * 由于底层存储以小数据开始，如果小数组存储空间耗尽，那么无论中数组或大数组是否还有空间，都无法分配新的数组。可以让使小数组足够大，减小发生这种情况的可能性。
 *
 *
 */
public class ResizableArray {

    private ResizableArrayBuffer resizableArrayBuffer = null;

    public byte[] sharedArray = null;
    public int    offset      = 0; //offset into sharedArray where this message data starts.
    public int    capacity    = 0; //the size of the section in the sharedArray allocated to this message.
    public int    length      = 0; //the number of bytes used of the allocated section.

    public ResizableArray(ResizableArrayBuffer resizableArrayBuffer) {
        this.resizableArrayBuffer = resizableArrayBuffer;
    }

    /**
     * Writes data from the ByteBuffer into this message - meaning into the buffer backing this message.
     *
     * @return
     */
    public int writeToMessage(ByteBuffer byteBuffer){
        int remaining = byteBuffer.remaining();

        while(length + remaining > capacity){
            //expand message.
            if(!this.resizableArrayBuffer.expandArray(this)) {
                return -1;
            }
        }

        int bytesToCopy = Math.min(remaining, this.capacity - this.length);
        byteBuffer.get(this.sharedArray, this.offset + this.length, remaining);
        this.length += bytesToCopy;

        return bytesToCopy;
    }

    public void free() {
        this.resizableArrayBuffer.free(this);
    }




}
