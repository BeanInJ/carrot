package com.carrot.aop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AopTargetBody {
    // 目标方法全名
    private String targetMethodName;
    // 前置
    private final List<String> beforeMethods = new ArrayList<>();
    // 后置
    private final List<String> afterMethods = new ArrayList<>();
    // 异常
    private final List<String> catchMethods = new ArrayList<>();
    // 总是执行
    private final List<String> finallyMethods = new ArrayList<>();
}
