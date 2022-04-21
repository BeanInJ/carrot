package com.carrot.aop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AopTargetBody {
    /**
     * 目标方法名 （类全名.方法名）
     *
     * 如果有方法名相同，参数类型、数量不同的方法，将被视为都要进行aop切面执行，
     * 如此，在进行切面
     */
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
