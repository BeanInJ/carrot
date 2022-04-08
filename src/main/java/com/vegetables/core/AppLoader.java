package com.vegetables.core;

import com.vegetables.system.logging.LogConfig;

import java.util.List;

/**
 * 资源中心
 */
public class AppLoader {
    private static ConfigCenter configCenter;
    private static List<Class<?>> controllerClass;
    private static UrlDistribute urlMap;

    public static ConfigCenter getConfigCenter() {
        return configCenter;
    }

    public static List<Class<?>> getControllerClass() {
        return controllerClass;
    }

    public static UrlDistribute getUrlMap() {
        return urlMap;
    }
    public static void load(){
        updateSourceLoader();
    }

    public static void updateSourceLoader() {
        // 加载配置中心
        configCenter = ConfigCenter.load();

        LogConfig.load();

        // 加载controller
        controllerClass = ControllerScanner.load();
        // 加载url映射器
        urlMap = UrlDistribute.load(controllerClass);
    }
}
