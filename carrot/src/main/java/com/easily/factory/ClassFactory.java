package com.easily.factory;

import com.easily.core.ConfigCenter;
import com.easily.factory.aop.AopPool;
import com.easily.factory.configure.ConfigurePool;
import com.easily.factory.controller.ControllerPool;
import com.easily.factory.filter.FilterPool;
import com.easily.label.AddPool;
import com.easily.system.util.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * class -> 进入工厂 -> 分发到类池解析 -> 解析完放入类池容器
 * <p>
 * 目前类工厂加载的所有的类都是单例的（饿汉式，在池解析时，完成类创建）
 */
public class ClassFactory {
    private static final Logger log = Logger.getGlobal();

    private final Pools pools = new Pools();
    private Scanner scanner;

    public ClassFactory(Scanner scanner,ConfigCenter configCenter){
        this.scanner = scanner;
        this.scanner.configCenter(configCenter);

        // 注册内置类池
        pools.add(new AopPool());
        pools.add(new ControllerPool());
        pools.add(new FilterPool());
        pools.add(new ConfigurePool());
    }

    /**
     * 从临时列表中获取类池，并注册到工厂
     */
    private void registerPool() {
        // 找到类上有AddPool注解的、继承自Pool接口的 , 注册类池

        this.scanner.getAll()
                .stream()
                .filter(classMeta ->
                        classMeta.getClazz().isAnnotationPresent(AddPool.class) && classMeta.getClazz().isAssignableFrom(Pool.class)
                )
                .forEach(classMeta -> {
                    // 注册类池
                    try {
                        pools.add(classMeta.getNewObject());
                        log.fine("注册类池成功：" + classMeta.getClazz().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warning("已跳过未正确注册类池：" + classMeta.getClazz().getName());
                    }
                });
    }

    /**
     * 分发类
     */
    private void distributeClass() {
        // 循环临时列表
        this.scanner.getAll().forEach(classMeta ->

                // 循环池列表，找到该clazz属于哪个池
                pools.getAll().values().stream()
                        .filter(pool -> AnnotationUtils.hasAnnotation(classMeta.getClazz(), pool.getLabel()))
                        .forEach(pool -> {
                            try {
                                pool.put(classMeta);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                            classMeta.setPoolType(pool.getClass());
                        })

        );
    }


    public Pools getPools(){
        return this.pools;
    }

    /**
     * class初始化
     */
    private void classesInit() throws IllegalAccessException, InstantiationException {
        this.scanner.unifyInit();
    }

    /**
     * 工厂开始工作： 注册池、分发类
     */
    public void start() throws IllegalAccessException, InstantiationException {

        // class初始化
        classesInit();

        // 注册类池
        registerPool();

        // 分发类
        distributeClass();

        pools.end();
    }

}
