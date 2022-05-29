package com.easily.buffer;

/**
 * QueueIntFlip 可变长数组
 *
 * QueueFlip 和 QueueFillCount 有以下两点不同：
 * 1、QueueFlip用一个翻转标志记录内部是否溢出，溢出返回0。
 * 2、QueueFlip中available()方法，在QueueFillCount中是一个公共变量。
 *
 * Created by jjenkov on 18-09-2015.
 */
public class QueueIntFlip {

    public int[] elements = null;

    public int capacity = 0;
    public int writePos = 0;
    public int readPos = 0;
    public boolean flipped = false;

    public QueueIntFlip(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity];
    }

    public void reset() {
        this.writePos = 0;
        this.readPos = 0;
        this.flipped = false;
    }

    public int available() {
        if (!flipped) {
            return writePos - readPos;
        }
        return capacity - readPos + writePos;
    }

    public int remainingCapacity() {
        if (!flipped) {
            return capacity - writePos;
        }
        return readPos - writePos;
    }

    public boolean put(int element) {
        if (!flipped) {
            if (writePos == capacity) {
                writePos = 0;
                flipped = true;

                if (writePos < readPos) {
                    elements[writePos++] = element;
                    return true;
                } else {
                    return false;
                }
            } else {
                elements[writePos++] = element;
                return true;
            }
        } else {
            if (writePos < readPos) {
                elements[writePos++] = element;
                return true;
            } else {
                return false;
            }
        }
    }

    public int put(int[] newElements, int length) {
        int newElementsReadPos = 0;
        if (!flipped) {
            //readPos 比 writePos 低的部分：
            //1) 从 writePos 到 capacity
            //2) 从 0 到 readPos

            if (length <= capacity - writePos) {
                //新元素插入数组顶部
                for (; newElementsReadPos < length; newElementsReadPos++) {
                    this.elements[this.writePos++] = newElements[newElementsReadPos];
                }

                return newElementsReadPos;
            } else {
                //新元素只能插入顶部或底部

                //插入顶部
                for (; this.writePos < capacity; this.writePos++) {
                    this.elements[this.writePos] = newElements[newElementsReadPos++];
                }

                //插入底部
                this.writePos = 0;
                this.flipped = true;
                int endPos = Math.min(this.readPos, length - newElementsReadPos);
                for (; this.writePos < endPos; this.writePos++) {
                    this.elements[writePos] = newElements[newElementsReadPos++];
                }


                return newElementsReadPos;
            }

        } else {
            //readPos 比 writePos高的部分
            //1) 从 writePos 到 readPos

            int endPos = Math.min(this.readPos, this.writePos + length);

            for (; this.writePos < endPos; this.writePos++) {
                this.elements[this.writePos] = newElements[newElementsReadPos++];
            }

            return newElementsReadPos;
        }
    }


    public int take() {
        if (!flipped) {
            if (readPos < writePos) {
                return elements[readPos++];
            } else {
                return -1;
            }
        } else {
            if (readPos == capacity) {
                readPos = 0;
                flipped = false;

                if (readPos < writePos) {
                    return elements[readPos++];
                } else {
                    return -1;
                }
            } else {
                return elements[readPos++];
            }
        }
    }

    public int take(int[] into, int length) {
        int intoWritePos = 0;
        if (!flipped) {
            //writePos higher than readPos - available section is writePos - readPos

            int endPos = Math.min(this.writePos, this.readPos + length);
            for (; this.readPos < endPos; this.readPos++) {
                into[intoWritePos++] = this.elements[this.readPos];
            }
            return intoWritePos;
        } else {
            //readPos higher than writePos - available sections are top + bottom of elements array

            if (length <= capacity - readPos) {
                //length is lower than the elements available at the top of the elements array - copy directly
                for (; intoWritePos < length; intoWritePos++) {
                    into[intoWritePos] = this.elements[this.readPos++];
                }

                return intoWritePos;
            } else {
                //length is higher than elements available at the top of the elements array
                //split copy into a copy from both top and bottom of elements array.

                //copy from top
                for (; this.readPos < capacity; this.readPos++) {
                    into[intoWritePos++] = this.elements[this.readPos];
                }

                //copy from bottom
                this.readPos = 0;
                this.flipped = false;
                int endPos = Math.min(this.writePos, length - intoWritePos);
                for (; this.readPos < endPos; this.readPos++) {
                    into[intoWritePos++] = this.elements[this.readPos];
                }

                return intoWritePos;
            }
        }
    }

}
