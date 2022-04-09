package com.vegetables.core;

import com.vegetables.system.notch.YouCanChange;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

/**
 * 静态资源类：配置中心
 *
 * Inner开头的类名，为静态资源类
 * 静态资源类是不可new的，且load()方法仅包内可调用
 * 静态资源类的load()方法一般情况下仅在AppLoader中加载
 */
public class InnerConfig implements YouCanChange {
    // 默认端口 app.port
    private static final int DEFAULT_APP_PORT = 8080;
    // 默认配置文件路径
    private static final String DEFAULT_CONFIG_FILE_PATH = "config.yml";
    // 所有配置内容
    private static Map<String,Object> config;

    public static Map<String, Object> getConfig() {
        return config;
    }

    protected static void update(Map<String,Object> newConfig) {
        config.clear();
        config.putAll(newConfig);
    }

    private InnerConfig(){}

    // 加载配置文件
    protected static void load(){
        Yaml yaml = new Yaml();
        try{
            File file = new File(DEFAULT_CONFIG_FILE_PATH);
            FileReader fileReader = new FileReader(file);
            config = yaml.load(fileReader);
        } catch (Exception ignored) {}
    }

    // 获取端口号
    public static int getAppPort(){
        Object port = getConfig().get("app.port");
        if(port == null){
            return DEFAULT_APP_PORT;
        }
        return (int)port;
    }

}
