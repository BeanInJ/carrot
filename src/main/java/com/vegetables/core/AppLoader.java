package com.vegetables.core;

import com.vegetables.core.factory.ClassFactory;
import com.vegetables.system.logging.LogConfig;
import com.vegetables.label.method.ChangeSource;

import java.util.Map;

/**
 * 资源加载器
 */
public class AppLoader {
    private AppLoader() {}

    public static void load(Class<?> main){
        // 加载配置中心
        InnerConfig.load(main);

        // 加载日志配置
        LogConfig.load();

        // 初始化class工厂
        ClassFactory.load();

        // 系统扫描器从配置中心加载需要扫描的包
        Scanner.load();

        // 系统扫描器开始工作（向class工厂提供"原料"）
        Scanner.start();

        // class工厂开始将 class分发到各个池
        ClassFactory.start();

        // 从controller池中加载url，初始化url映射器
        InnerUrlMethod.load();

    }

    @SuppressWarnings("unchecked")
    public static void updateInnerConfig(ChangeSource changeSource) {
        if(changeSource != null){
            Object o = changeSource.get();
            if(o instanceof Map) {
                InnerConfig.update((Map<String, Object>) changeSource.get());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void updateUrlMethod(ChangeSource changeSource) {
        if(changeSource != null){
            Object o = changeSource.get();
            if(o instanceof Map) {
                InnerUrlMethod.update((Map<String, Object[]>) changeSource.get());
            }
        }
    }
}
