package com.vegetables.core.factory;

import com.vegetables.core.factory.classPool.AopPool;
import com.vegetables.core.factory.classPool.BeforeEnterPool;
import com.vegetables.core.factory.classPool.BeforeReturnPool;
import com.vegetables.core.factory.classPool.ControllerPool;
import com.vegetables.label.annotation.AddPool;
import com.vegetables.label.annotation.BeforeEnter;
import com.vegetables.label.annotation.BeforeReturn;
import com.vegetables.label.annotation.Controller;
import com.vegetables.core.factory.Pool;
import com.vegetables.system.aop.active.AOP;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.logging.Logger;

/**
 * ClassFactory 的实现参考了工厂模式
 *
 * 扫描器扫描到的所有 class 在这里统一处理
 * ClassFactory 4个对外开放的方法:
 * 1、load()            初始化、加载内部类池。仅供 AppLoader.load()调用
 * 2、addClass()        添加“原料”。仅供 Scanner.scannerOne()调用
 * 3、start()           开始工作（注册外部类池、分发原料）。仅供 Scanner
 */
public class ClassFactory {
    private static final Logger log = Logger.getGlobal();

    /**
     * 保存已注册的“池”
     */
    private static final List<Pool> poolList = new ArrayList<>();

    /**
     * 保存原料的临时列表，注册、分发完成会被清空
     */
    private static final Set<Class<?>> classTempSet = new HashSet<>();

    /**
     * 工厂初始化（在扫描器运行 Scanner.load(); 之前调用）
     */
    public static void load(){
        // 注册类池

        // 注册 Controller 控制器 类池
        addPool(new ControllerPool());
        // 注册 BeforeEnter 前置拦截器 类池
        addPool(new BeforeEnterPool());
        // 注册 BeforeReturn 后置拦截器 类池
        addPool(new BeforeReturnPool());
        // 注册 AOP 切面 类池
        addPool(new AopPool());

        // 外部若进行类池注册，应当在Scanner.load();之前
    }

    /**
     * 注册一个"池",必须在扫描器扫描之前注册才有效
     */
    private static boolean addPool(Pool pool){
        return poolList.add(pool);
    }

    /**
     * 将“原料”clazz 添加到 classTempSet
     */
    public static void addClass(Class<?> clazz){
        classTempSet.add(clazz);
    }

    /**
     * 获取所已注册的类池
     */
    public static List<Pool> getAllPool(){
        return poolList;
    }

    /**
     * 统一清理类池中的无效元素
     */
    private static void trim(){
        poolList.forEach(Pool::trim);
    }

    /**
     * 清空临时列表
     */
    private static void trimTemp(){
        classTempSet.clear();
    }

    /**
     * 从临时列表中获取类池，并注册到工厂
     */
    private static void registerPoolFromTemp(){
        // 找到类上有AddPool注解的、继承自Pool接口的 , 注册类池
        classTempSet.stream().filter(clazz -> clazz.isAnnotationPresent(AddPool.class)
                        || clazz.isAssignableFrom(Pool.class))
                .forEach(clazz -> {
                    // 注册类池
                    try{
                        Pool pool = (Pool) clazz.newInstance();
                        addPool(pool);
                        log.info("注册类池成功：" + clazz.getName());
                    }catch (ReflectiveOperationException e){
                        e.printStackTrace();
                        log.warning("已跳过未正确注册类池：" + clazz.getName());
                    }
                });
    }

    /**
     * 分发类
     */
    private static void distributeClass(){

        classTempSet.forEach(clazz ->
                // 根据clazz头上的注解，被分配到不同的类池中
                poolList.stream()
                        .filter(pool -> clazz.isAnnotationPresent(pool.getLabel()))
                        .forEach(pool -> pool.add(clazz))
        );
    }

    /**
     * 工厂开始工作： 注册池、分发类
     */
    public static void start(){
        // 注册类池的优先级 > 功能类分发
        // 所有类池都注册完毕，功能类分发才能正确进行

        // 注册类池
        registerPoolFromTemp();

        // 分发类
        distributeClass();

        // 清理临时列表
        trimTemp();

        // 清理类池中的无效类
        trim();
    }

}
