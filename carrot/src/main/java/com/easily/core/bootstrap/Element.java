package com.easily.core.bootstrap;

/**
 * 一个Element为一个独立的执行单元
 */
@FunctionalInterface
public interface Element {
    Object execute(Object o);
}
