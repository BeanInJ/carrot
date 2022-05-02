package com.easily.factory;

import com.easily.factory.aop.AopPool;
import com.easily.factory.configure.ConfigurePool;
import com.easily.factory.controller.ControllerPool;
import com.easily.factory.filter.FilterPool;
import com.easily.label.AddPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * class -> 进入工厂 -> 分发到类池解析 -> 解析完放入类池容器
 *
 * 目前类工厂加载的所有的类都是单例的（饿汉式，在池解析时，完成类创建）
 */
public class ClassFactory {
    private static final Logger log = Logger.getGlobal();

    /**
     * ｛”类池名“，类池｝
     */
    private static final Map<String,Pool> pools = new HashMap<>();

    /**
     * 保存"原料"的临时列表，注册、分发完成会被清空
     */
    private static final Set<Class<?>> classTempSet = new HashSet<>();

    public static void load(){
        // 注册内置类池
        addPool(new AopPool());
        addPool(new ControllerPool());
        addPool(new FilterPool());
        addPool(new ConfigurePool());
    }

    public static Pool getPool(String poolName){
        return pools.get(poolName);
    }

    protected static void putPool(String poolName,Pool pool){
        pools.put(poolName,pool);
    }

    /**
     * 注册一个"池",必须在扫描器扫描之前注册才有效
     */
    private static void addPool(Pool pool){
        // 以类全名为唯一表示注册
        String key = pool.getPoolName();

        if(pools.containsKey(key)){
            log.warning("已跳过重复类池ID: " + key);
            return;
        }

        pools.put(key,pool);
    }

    /**
     * 将“原料”clazz 添加到 classTempSet
     */
    public static void addClass(Class<?> clazz) {
        classTempSet.add(clazz);
    }

    /**
     * 清空临时列表
     */
    private static void clearFactoryTemp() {
        classTempSet.clear();
    }

    /**
     * 清空临时列表
     */
    private static void clearPoolTemp() {
        pools.values().forEach(Pool::clear);
    }

    /**
     * 从临时列表中获取类池，并注册到工厂
     */
    private static void registerPoolFromTemp() {
        // 找到类上有AddPool注解的、继承自Pool接口的 , 注册类池
        classTempSet.stream()

                .filter(clazz ->
                        clazz.isAnnotationPresent(AddPool.class) || clazz.isAssignableFrom(Pool.class)
                )

                .forEach(clazz ->
                {
                    // 注册类池
                    try {
                        Pool pool = (Pool) clazz.newInstance();
                        addPool(pool);
                        log.fine("注册类池成功：" + clazz.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warning("已跳过未正确注册类池：" + clazz.getName());
                    }
                });
    }

    /**
     * 分发类
     */
    private static void distributeClass() {
        // 循环临时列表
        classTempSet.forEach(clazz ->
                // 循环池列表，找到该clazz属于哪个池
                pools.values().stream()
                        .filter(pool -> clazz.isAnnotationPresent(pool.getLabel()))
                        .forEach(pool -> pool.add(clazz))
        );
    }

    /**
     * 解析class入容器
     */
    private static void parseClass(){
        pools.values().forEach(Pool::parseToContainer);
    }

    /**
     * 工厂开始工作： 注册池、分发类
     */
    public static void start() {
        // 注册类池的优先级 > 功能类分发
        // 所有类池都注册完毕，功能类分发才能正确进行

        // 注册类池
        registerPoolFromTemp();

        // 分发类
        distributeClass();

        // 开始解析
        parseClass();

        // 清理临时列表
        clearFactoryTemp();

        // 解析完清理池中临时数据
        clearPoolTemp();
    }

}
