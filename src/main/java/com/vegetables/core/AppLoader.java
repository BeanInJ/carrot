package com.vegetables.core;

import com.vegetables.system.logging.LogConfig;

import java.util.List;

/**
 * 资源中心
 */
public class AppLoader {
    private AppLoader() {}

    public static void load(){
        // 加载配置中心
        ConfigCenter.load();

        // 加载日志配置
        LogConfig.load();

        // 加载controller
        ControllerScanner.load();

        // 加载url映射器
        UrlDistribute.load();
    }
    public static void update(ConfigCenter configCenter, List<Class<?>> controllerClass, UrlDistribute urlMap) {

    }
}
