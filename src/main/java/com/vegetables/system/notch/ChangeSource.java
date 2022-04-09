package com.vegetables.system.notch;

/**
 * 需要修改系统内部资源就继承此接口
 */
public interface ChangeSource {
    Object get();
}
