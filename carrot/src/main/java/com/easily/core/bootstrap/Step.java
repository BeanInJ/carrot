package com.easily.core.bootstrap;

/**
 * 一个Step为执行器的一个执行单元/步骤
 */
@FunctionalInterface
public interface Step {
    void execute();
}
