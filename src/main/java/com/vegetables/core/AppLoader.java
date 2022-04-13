package com.vegetables.core;

import com.vegetables.core.factory.classPool.ClassFactory;
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

        // 加载class工厂
        ClassFactory.load();

        // 系统扫描器开始工作
        Scanner.load();

        // 加载url映射器
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
