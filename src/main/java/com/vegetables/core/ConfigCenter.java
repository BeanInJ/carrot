package com.vegetables.core;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

/**
 * 静态资源类：配置中心
 */
public class ConfigCenter {
    // 默认端口 app.port
    private static final int DEFAULT_APP_PORT = 8080;
    // 默认配置文件路径
    private static final String DEFAULT_CONFIG_FILE_PATH = "config.yml";

    private static Map<String,Object> config;

    public static Map<String, Object> getConfig() {
        return config;
    }

    private ConfigCenter(){}

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
